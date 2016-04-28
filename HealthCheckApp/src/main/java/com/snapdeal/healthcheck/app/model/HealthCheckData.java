package com.snapdeal.healthcheck.app.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.snapdeal.healthcheck.app.bo.EndpointDetailsBO;
import static com.snapdeal.healthcheck.app.constants.AppConstant.TOMCAT_ENDPOINT;
import com.snapdeal.healthcheck.app.enums.Component;

public class HealthCheckData {

	@Autowired
	private EndpointDetailsBO endpointDetails;
	
	private String camsEndPoint;
	private String cocofsEndPoint;
	private String ipmsEndPoint;
	private String omsEndPoint;
	private String opmsEndPoint;
	private String opsEndPoint;
	private String promoEndPoint;
	private String scoreEndPoint;
	
	public String getPromoEndPoint() {
		return promoEndPoint;
	}

	public String getCamsEndPoint() {
		return camsEndPoint;
	}

	public String getCocofsEndPoint() {
		return cocofsEndPoint;
	}

	public String getIpmsEndPoint() {
		return ipmsEndPoint;
	}

	public String getOmsEndPoint() {
		return omsEndPoint;
	}

	public String getOpmsEndPoint() {
		return opmsEndPoint;
	}

	public String getOpsEndPoint() {
		return opsEndPoint;
	}

	public String getScoreEndPoint() {
		return scoreEndPoint;
	}

	public void get() {
		List<EndpointDetails> listDetails = endpointDetails.getAllEndpointDetails();
		for(EndpointDetails detail : listDetails) {
			if(detail.getKeyName().equals(Component.CAMS.code()+TOMCAT_ENDPOINT))
				this.camsEndPoint = detail.getKeyValue();
			else if(detail.getKeyName().equals(Component.COCOFS.code()+TOMCAT_ENDPOINT))
				this.cocofsEndPoint = detail.getKeyValue();
			else if(detail.getKeyName().equals(Component.IPMS.code()+TOMCAT_ENDPOINT))
				this.ipmsEndPoint = detail.getKeyValue();
			else if(detail.getKeyName().equals(Component.OMS.code()+TOMCAT_ENDPOINT))
				this.omsEndPoint = detail.getKeyValue();
			else if(detail.getKeyName().equals(Component.OPMS.code()+TOMCAT_ENDPOINT))
				this.opmsEndPoint = detail.getKeyValue();
			else if(detail.getKeyName().equals(Component.OPS.code()+TOMCAT_ENDPOINT))
				this.opsEndPoint = detail.getKeyValue();
			else if(detail.getKeyName().equals(Component.PROMO.code()+TOMCAT_ENDPOINT))
				this.promoEndPoint = detail.getKeyValue();
			else if(detail.getKeyName().equals(Component.SCORE.code()+TOMCAT_ENDPOINT))
				this.scoreEndPoint = detail.getKeyValue();
		}
	}
}