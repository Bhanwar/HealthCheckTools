package com.snapdeal.healthcheck.app.services.impl;

import static com.snapdeal.healthcheck.app.constants.AppConstant.CONNECTION_TIMED_OUT;
import static com.snapdeal.healthcheck.app.constants.AppConstant.currentExecDate;
import static com.snapdeal.healthcheck.app.constants.AppConstant.healthResult;
import static com.snapdeal.healthcheck.app.constants.AppConstant.sshKeyLocation;
import static com.snapdeal.healthcheck.app.constants.AppConstant.sshUser;
import static com.snapdeal.healthcheck.app.constants.Formatter.dateFormatter;
import static com.snapdeal.healthcheck.app.constants.Formatter.timeFormatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.Callable;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapdeal.healthcheck.app.comp.token.SFMobileToken;
import com.snapdeal.healthcheck.app.comp.token.SellerServicesToken;
import com.snapdeal.healthcheck.app.constants.AppConstant;
import com.snapdeal.healthcheck.app.enums.ComponentType;
import com.snapdeal.healthcheck.app.enums.ServerStatus;
import com.snapdeal.healthcheck.app.enums.TokenComponent;
import com.snapdeal.healthcheck.app.model.ComponentDetails;
import com.snapdeal.healthcheck.app.model.HealthCheckResult;
import com.snapdeal.healthcheck.app.model.HttpCallResponse;
import com.snapdeal.healthcheck.app.model.TokenApiDetails;
import com.snapdeal.healthcheck.app.mongo.repositories.MongoRepoService;
import com.snapdeal.healthcheck.app.utils.HttpUtil;
import com.snapdeal.healthcheck.app.utils.RestUtil;
import com.snapdeal.healthcheck.app.utils.SSHUtil;


public class EnvHealthCheckImpl implements Callable<HealthCheckResult> {

	private static final Logger log = LoggerFactory.getLogger(EnvHealthCheckImpl.class);

	private ComponentDetails compDetails;
	private MongoRepoService mongoService;

	public EnvHealthCheckImpl(ComponentDetails compDetails, MongoRepoService mongoService) {
		this.compDetails = compDetails;
		this.mongoService = mongoService;
	}

	@Override
	public HealthCheckResult call() throws Exception {
		TokenApiDetails tokenApidetails = null;
		String compName = compDetails.getComponentName();
		TokenComponent tokenComp = TokenComponent.getValueOf(compName);
		if(!tokenComp.equals(TokenComponent.INV)) {
			tokenApidetails = mongoService.getTokenDetails().getTokenApiDetails(compName);
		}
		return checkServerHealth(compDetails, tokenApidetails);
	}

	public static HealthCheckResult checkServerHealth(ComponentDetails component, TokenApiDetails tokenApi) {
		boolean isServerUp = true;
		boolean apiExist = false;
		boolean currentState = true;
		String compName = component.getComponentName();
		String endpoint = component.getEndpoint();
		if(healthResult.containsKey(compName))
			currentState = healthResult.get(compName);
		String url = null;
		String callType = null;
		String headersJson = null;
		String reqJson = null;
		String actualStatusCode = null;
		String actualResp = null;
		String expectedResp = null;
		String expStatusCode = "200 OK";
		String htmlCallException = null;
		String token = null;

		HealthCheckResult result = new HealthCheckResult(compName);
		HttpCallResponse response = null;
		Date resultDate = currentExecDate;
		String logSuffix = compName + ": ";

		log.debug("Checking health for component: " + compName);
		log.debug(logSuffix + "Comp details - " + component);

		if(component.getComponentType().equals(ComponentType.TOMCAT)) {
			if(tokenApi != null) {
				TokenComponent tokenComp = TokenComponent.getValueOf(compName);
				switch (tokenComp) {
				case SF_MOBILE:
					token = SFMobileToken.fetchTokenFromHeader(tokenApi, endpoint).getToken();
					break;
				case SELLER_SERVICES:
					token = SellerServicesToken.fetchTokenFromBody(tokenApi, endpoint).getToken();
					break;
				default:
					break;
				}
			}	
			
			// Health Check API
			if (component.getHealthCheckApi() != null && component.getHealthCheckApiCallType() != null
					&& component.getHealthCheckApiResp() != null) {
				apiExist = true;
				response = null;

				url = endpoint + component.getHealthCheckApi();
				callType = component.getHealthCheckApiCallType();
				headersJson = component.getHealthCheckHeaders();
				reqJson = component.getHealthCheckApiReqJson();
				if(component.getHealthCheckHeaders() != null && token != null)
					headersJson = component.getHealthCheckHeaders().replace(AppConstant.ADMIN_UI_HEADER_TOKEN, token);
				if(component.getHealthCheckApiReqJson() != null && token != null)
					reqJson = component.getHealthCheckApiReqJson().replace(AppConstant.ADMIN_UI_REQ_TOKEN, token);
				log.debug(logSuffix + "Health Check API URL - " + url);

				response = HttpUtil.fetchResponse(url, callType, headersJson, reqJson);
				if(currentState && (response.getStatusCode() == null || !response.getStatusCode().equals(expStatusCode))) {
					log.debug(logSuffix + "Retrying Http Call GET..! Status Code: " + response.getStatusCode() + " Call Exception: " + response.getHttpCallException());
					response = retryHttpCall(endpoint, url, callType, headersJson, reqJson, logSuffix, response.getHttpCallException());
				}
				expectedResp = component.getHealthCheckApiResp();
				actualResp = response.getResponseBody();
				actualStatusCode = response.getStatusCode();
				htmlCallException = response.getHttpCallException();
				if (response.getStatusCode() != null && response.getStatusCode().equals(expStatusCode)
						&& response.getResponseBody() != null && response.getResponseBody().contains(expectedResp))
					isServerUp = true;
				else
					isServerUp = false;

				log.debug(logSuffix + "Health Check API Status code - " + response.getStatusCode());
			} else {
				log.warn(logSuffix + "Health Check API details not present!");
			}

			// First Get API
			if (isServerUp && component.getFirstGetApi() != null && component.getFirstGetApiCallType() != null
					&& component.getFirstGetApiResp() != null) {
				apiExist = true;
				response = null;

				url = endpoint + component.getFirstGetApi();
				callType = component.getFirstGetApiCallType();
				headersJson = component.getFirstGetHeaders();
				reqJson = component.getFirstGetApiReqJson();
				if(component.getFirstGetHeaders() != null && token != null)
					headersJson = component.getFirstGetHeaders().replace(AppConstant.ADMIN_UI_HEADER_TOKEN, token);
				if(component.getFirstGetApiReqJson() != null && token != null)
					reqJson = component.getFirstGetApiReqJson().replace(AppConstant.ADMIN_UI_REQ_TOKEN, token);
				log.debug(logSuffix + "First Get API URL - " + url);

				response = HttpUtil.fetchResponse(url, callType, headersJson, reqJson);
				if(currentState && (response.getStatusCode() == null || !response.getStatusCode().equals(expStatusCode))) {
					log.debug(logSuffix + "Retrying Http Call GET..! Status Code: " + response.getStatusCode() + " Call Exception: " + response.getHttpCallException());
					response = retryHttpCall(endpoint, url, callType, headersJson, reqJson, logSuffix, response.getHttpCallException());
				}
				expectedResp = component.getFirstGetApiResp();
				actualResp = response.getResponseBody();
				actualStatusCode = response.getStatusCode();
				htmlCallException = response.getHttpCallException();
				if (response.getStatusCode() != null && response.getStatusCode().equals(expStatusCode)
						&& response.getResponseBody() != null && response.getResponseBody().contains(expectedResp))
					isServerUp = true;
				else
					isServerUp = false;

				log.debug(logSuffix + "First Get API Status code - " + response.getStatusCode());
			} else {
				if (isServerUp)
					log.warn(logSuffix + "First Get API details not present!");
			}

			// Second Get API
			if (isServerUp && component.getSecondGetApi() != null && component.getSecondGetApiCallType() != null
					&& component.getSecondGetApiResp() != null) {
				apiExist = true;
				response = null;

				url = endpoint + component.getSecondGetApi();
				callType = component.getSecondGetApiCallType();
				headersJson = component.getSecondGetHeaders();
				reqJson = component.getSecondGetApiReqJson();
				if(component.getSecondGetHeaders() != null && token != null)
					headersJson = component.getSecondGetHeaders().replace(AppConstant.ADMIN_UI_HEADER_TOKEN, token);
				if(component.getSecondGetApiReqJson() != null && token != null)
					reqJson = component.getSecondGetApiReqJson().replace(AppConstant.ADMIN_UI_REQ_TOKEN, token);
				log.debug(logSuffix + "Second Get API URL - " + url);

				response = HttpUtil.fetchResponse(url, callType, headersJson, reqJson);
				if(currentState && (response.getStatusCode() == null || !response.getStatusCode().equals(expStatusCode))) {
					log.debug(logSuffix + "Retrying Http Call GET..! Status Code: " + response.getStatusCode() + " Call Exception: " + response.getHttpCallException());
					response = retryHttpCall(endpoint, url, callType, headersJson, reqJson, logSuffix, response.getHttpCallException());
				}
				expectedResp = component.getSecondGetApiResp();
				actualResp = response.getResponseBody();
				actualStatusCode = response.getStatusCode();
				htmlCallException = response.getHttpCallException();
				if (response.getStatusCode() != null && response.getStatusCode().equals(expStatusCode)
						&& response.getResponseBody() != null && response.getResponseBody().contains(expectedResp))
					isServerUp = true;
				else
					isServerUp = false;

				log.debug(logSuffix + "Second Get API Status code - " + response.getStatusCode());
			} else {
				if (isServerUp)
					log.warn(logSuffix + "Second Get API details not present!");
			}

			result.setServerUp(isServerUp);
			if(isServerUp)
				result.setServerStatus(ServerStatus.UP);
			else
				result.setServerStatus(ServerStatus.DOWN);

			if (!isServerUp) {
				// Check for netwrok issues
				if (htmlCallException != null) {
					if(htmlCallException.equals(AppConstant.NETWORK_UNREACHABLE) || htmlCallException.equals(AppConstant.NO_ROUTE_TO_HOST)) {
						result.setServerStatus(ServerStatus.NTWRK_ISSUE);
						result.setServerUp(currentState);
						log.warn(logSuffix + "Connect to server failing with network issue: " + htmlCallException);
					} else if(htmlCallException.equals(CONNECTION_TIMED_OUT)) {
						result.setServerStatus(ServerStatus.CONN_TIMED_OUT);
						result.setServerUp(currentState);
						log.warn(logSuffix + "Connection timed out!!");
					} else
						log.debug(logSuffix + "Server Down!!");
				}

				result.setFailedURL(url);
				result.setFailedReqJson(reqJson);
				result.setFailedHttpCallException(htmlCallException);
				result.setFailedActualResp(actualResp);
				result.setFailedStatusCode(actualStatusCode);
				result.setFailedExpResp(expectedResp);
				log.debug(logSuffix + "URL: " + url);
				log.debug(logSuffix + "Request JSON: " + reqJson);
				log.debug(logSuffix + "Status Code: " + actualStatusCode);
				log.debug(logSuffix + "Response: " + actualResp);
				log.debug(logSuffix + "HTTP Call Exception: " + htmlCallException);
			}
			String date = dateFormatter.format(resultDate);
			String time = timeFormatter.format(resultDate);
			result.setExecDate(date);
			result.setExecTime(time);
			result.setExecDateTime(resultDate);

			if(!apiExist)
				result.setServerUp(false);
		} else if(component.getComponentType().equals(ComponentType.JAR)) {
			log.debug(logSuffix + "Checking if JAR is running..");
			result = healthCheckJAR(component, result);
		}
		
		return result;
	}

	private static HttpCallResponse retryHttpCall(String endpoint, String url, String callType, String headers,
			String reqJson, String logSuffix, String httpException) {
		try {
			if (httpException != null) {
				pingIp(endpoint, logSuffix);
				connectSocket(endpoint, logSuffix);
				Thread.sleep(2000);
			} else {
				Thread.sleep(5000);
			}
		} catch (InterruptedException e) {
		}
		return HttpUtil.fetchResponse(url, callType, headers, reqJson);
	}

	private static void pingIp(String endpoint, String logSuffix) {
		try {
			URL url = new URL(endpoint);
			String host = url.getHost();
			log.debug(logSuffix + "Trying to ping " + host);
			String s = "";

			ProcessBuilder pb = new ProcessBuilder("ping", "-c", "4", host);
			Process process = pb.start();

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			// read the output from the command
			while ((s = stdInput.readLine()) != null) {
				log.debug(logSuffix + s);
			}

			// read any errors from the attempted command
			while ((s = stdError.readLine()) != null) {
				log.error(logSuffix + s);
			}

		} catch (Exception e) {
			log.error(logSuffix + "Exception occured while performing ping! " + e.getMessage(), e);
		}
	}

	private static void connectSocket(String endpoint, String logSuffix) {
		Socket sock = null;
		boolean connected = false;
		try {
			URL url = new URL(endpoint);
			String host = url.getHost();
			int port = url.getPort();
			log.debug(logSuffix + "Trying to connect to socket for host: " + host + " at port: " + port);
			InetAddress addr = InetAddress.getByName(host);
			SocketAddress sockaddr = new InetSocketAddress(addr, port);
			// Create an unbound socket
			sock = new Socket();
			// This method will block no more than timeoutMs.
			// If the timeout occurs, SocketTimeoutException is thrown.
			int timeoutMs = 2000; // 2 seconds
			sock.connect(sockaddr, timeoutMs);
			connected = sock.isConnected();
			log.debug(logSuffix + "Connection : " + connected);
		} catch (Exception e) {
			log.error(logSuffix + "Exception occured while trying to connect to socket: " + e.getMessage(), e);
		} finally {
			if (sock != null && !sock.isClosed()) {
				try {
					sock.close();
				} catch (IOException e) {}
			}
		}
		if(connected)
			log.debug(logSuffix + "Connection successful!!");
		else
			log.warn(logSuffix + "Could not establish connection!!");
	}
	
	private static HealthCheckResult healthCheckJAR(ComponentDetails component, HealthCheckResult result) {
		String logSuffix = component.getComponentName() + ": ";
		SSHUtil sshMgr = null;
		String processName = component.getHealthCheckApi();
		try {
			String command = "ps -ef | grep " + processName;
			URL url = new URL(component.getEndpoint());
			String host = url.getHost();
			sshMgr = new SSHUtil(sshUser, host, 22, sshKeyLocation);
			String[] processes = sshMgr.executeCommand(command).toString().split("\\n");
			if(processes.length > 0) {
				result.setServerUp(true);
				if(processes.length == 1) {
					String[] processData = processes[0].trim().split(" +");
					log.debug(logSuffix + "Process: " + processName + ", running with process id: " + processData[1]);
				}else {
					log.warn(logSuffix + "PLEASE CHECK!! More than one processes are running with the given process name: " + processName);
					log.warn(logSuffix + "Process count: " + processes.length);
					for(int i=0;i<processes.length;i++)
						log.warn(logSuffix + processes[i]);
				}
			} else {
				result.setServerUp(false);
			}
		}catch(Exception e) {
			log.error(logSuffix + "Exception occured while performing SSH: " + e.getMessage(), e);
			result.setServerUp(false);
			result.setFailedHttpCallException(e.getMessage());
		}finally {
			if(sshMgr != null)
				sshMgr.closeConnection();
		}
		return result;
	}
	
	private static HealthCheckResult healthCheckJarIcinga(ComponentDetails component, HealthCheckResult result) {
		String url = "https://10.41.80.63:5665/v1/objects/services/icinga-object-node.devops.dev.ggn1.ops.snapdeal.io!heckcheck_url";
		String headers = "{\"Content-Type\":\"application/json\"}";
		String payload = null;
		//HttpUtil.fetchResponse(url, "GET", headers, payload);
		HttpCallResponse resp = RestUtil.fetchResponseAuth(url, "GET", headers, payload, "root", "8d06861ebcb97647");
		//System.out.println(resp.getResponseBody());
		JSONObject jsonData = new JSONObject(resp.getResponseBody());
		JSONObject lastResult = jsonData.getJSONArray("results").getJSONObject(0).getJSONObject("attrs").getJSONObject("last_check_result");
		System.out.println(lastResult.toString());
		return result;
	}
	
	public static void main(String[] args) {
		healthCheckJarIcinga(null, null);
	}
}
