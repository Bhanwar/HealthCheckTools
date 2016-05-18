<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html lang="en">
<head>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<link
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"
	rel="stylesheet" />
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-validator/0.5.3/css/bootstrapValidator.css"
	rel="stylesheet" />
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-validator/0.5.3/js/bootstrapValidator.js"></script>
<spring:url value="/css/home.css" var="webAppCss" />
<link href="${webAppCss}" rel="stylesheet" />
<spring:url value="/javascript/home.js" var="webAppJs" />
<script src="${webAppJs}"></script>
<spring:url value="/javascript/serializeJson.js" var="serializeJson" />
<script src="${serializeJson}"></script>
<title>Health Check App</title>
<link rel=icon type=image/ico href="http://i3.sdlcdn.com/img/icons/favicon.ico" />
</head>

<body>
	<div id="header" class="pointer"></div>
	<div id="main-content">
		<div class="container">
			<div class="row clearfix">
				<!-- Modal Start here-->
				<div class="modal fade bs-example-modal-sm" id="myPleaseWait" role="dialog" aria-hidden="true" data-backdrop="static">
					<div class="modal-dialog modal-sm">
						<div class="modal-content">
							<div class="modal-header">
								<h4 class="modal-title">
									<span class="glyphicon glyphicon-time"> </span> Processing...
								</h4>
							</div>
							<div class="modal-body">
								<div style="width: 100%; height: 200px;">
									<div style="left: 50%; top: 50%" class="dots-loader"></div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="modal fade bs-example-modal-sm" id="myLoading" role="dialog" aria-hidden="false" data-backdrop="static">
					<div class="modal-dialog modal-sm">
						<div class="modal-content">
							<div class="modal-body">
								<span class="glyphicon glyphicon-hourglass"> Loading...</span>
							</div>
						</div>
					</div>
				</div>

				<div class="col-md-2 column"></div>
				<div class="col-md-8 column">
					<form id="addUpdateCompForm" class="form-horizontal" role="form">
						<div id="compNameDiv" class="form-group">
							<label class="col-md-3 control-label" for="compName">Component</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="compName" placeholder="Enter component name">
							</div>
						</div>
						<div id="qmSpocDiv" class="form-group">
							<label class="col-md-3 control-label" for="qmSpoc">QM SPOC</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="qmSpoc" placeholder="Enter qm spoc">
							</div>
						</div>
						<div id="qaSpocDiv" class="form-group">
							<label class="col-md-3 control-label" for="qaSpoc">QA SPOC</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="qaSpoc" placeholder="Enter qa spoc with comma , separated">
							</div>
						</div>
						<div id="endpointDiv" class="form-group">
							<label class="col-md-3 control-label" for="endpoint">Endpoint</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="endpoint" placeholder="http://{ip / host}:{port}">
							</div>
						</div>
						
						<hr>
						<div class="form-group">
							<label class="control-label col-sm-6">Health Check</label>
							<div class="col-sm-6"></div>
						</div>
						<div id="hcApiUrlDiv" class="form-group">
							<label class="control-label col-sm-3" for="hcApiUrl">API URL</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="hcApiUrl" placeholder="Enter health check api url">
							</div>
						</div>
						<div id="hcApiCallTypeDiv" class="form-group">
							<label class="control-label col-sm-3" for="hcApiCallType">Call Type</label>
							<div class="col-sm-9">
								<select name="hcApiCallType" class="form-control" id="hcApiCallType-select">
									<option value="GET">GET</option>
									<option value="POST">POST</option>
								</select>
							</div>
						</div>
						<div id="hcApiHeadersJsonDiv" class="form-group">
 							<label class="control-label col-sm-3" for="hcApiHeadersJson">Headers as JSON</label>
 							<div class="col-sm-9">
 								<textarea id="txtarea" class="form-control" name="hcApiHeadersJson" placeholder="Enter headers in json format"></textarea>
 							</div>
 						</div>
						<div id="hcApiReqJsonDiv" class="form-group" style="display:none">
							<label class="control-label col-sm-3" for="hcApiReqJson">Request JSON</label>
							<div class="col-sm-9">
								<textarea id="txtarea" class="form-control" name="hcApiReqJson" placeholder="Enter request json payload"></textarea>
							</div>
						</div>
						<div id="hcApiRespDiv" class="form-group">
							<label class="control-label col-sm-3" for="hcApiResp">Expected Response</label>
							<div class="col-sm-9">
								<textarea id="txtarea" class="form-control" name="hcApiResp" placeholder="Enter expected response string"></textarea>
							</div>
						</div>

						<hr>
						<div class="form-group">
							<label class="control-label col-sm-6">1st Get API</label>
							<div class="col-sm-6"></div>
						</div>
<						<div id="fgApiUrlDiv" class="form-group">
							<label class="control-label col-sm-3" for="fgApiUrl">API URL</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="fgApiUrl" placeholder="Enter 1st getter api url">
							</div>
						</div>
						<div id="fgApiCallTypeDiv" class="form-group">
							<label class="control-label col-sm-3" for="fgApiCallType">Call Type</label>
							<div class="col-sm-9">
								<select name="fgApiCallType" class="form-control" id="fgApiCallType-select">
									<option value="GET">GET</option>
									<option value="POST">POST</option>
								</select>
							</div>
						</div>
						<div id="fgApiHeadersJsonDiv" class="form-group">
 							<label class="control-label col-sm-3" for="fgApiHeadersJson">Headers as JSON</label>
 							<div class="col-sm-9">
 								<textarea id="txtarea" class="form-control" name="fgApiHeadersJson" placeholder="Enter headers in json format"></textarea>
 							</div>
 						</div>
						<div id="fgApiReqJsonDiv" class="form-group" style="display:none">
							<label class="control-label col-sm-3" for="fgApiReqJson">Request JSON</label>
							<div class="col-sm-9">
								<textarea id="txtarea" class="form-control" name="fgApiReqJson" placeholder="Enter request json payload"></textarea>
							</div>
						</div>
						<div id="fgApiRespDiv" class="form-group">
							<label class="control-label col-sm-3" for="fgApiResp">Expected Response</label>
							<div class="col-sm-9">
								<textarea id="txtarea" class="form-control" name="fgApiResp" placeholder="Enter expected response string"></textarea>
							</div>
						</div>

						<hr>
						<div class="form-group">
							<label class="control-label col-sm-6">2nd Get API</label>
							<div class="col-sm-6"></div>
						</div>
						<div id="sgApiUrlDiv" class="form-group">
							<label class="control-label col-sm-3" for="sgApiUrl">API URL</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="sgApiUrl" placeholder="Enter 2nd getter api url">
							</div>
						</div>
						<div id="sgApiCallTypeDiv" class="form-group">
							<label class="control-label col-sm-3" for="sgApiCallType">Call Type</label>
							<div class="col-sm-9">
								<select name="sgApiCallType" class="form-control" id="sgApiCallType-select">
									<option value="GET">GET</option>
									<option value="POST">POST</option>
								</select>
							</div>
						</div>
						
						<div id="sgApiHeadersJsonDiv" class="form-group">
 							<label class="control-label col-sm-3" for="sgApiHeadersJson">Headers as JSON</label>
 							<div class="col-sm-9">
 								<textarea id="txtarea" class="form-control" name="sgApiHeadersJson" placeholder="Enter headers in json format"></textarea>
 							</div>
 						</div>
						<div id="sgApiReqJsonDiv" class="form-group" style="display:none">
							<label class="control-label col-sm-3" for="sgApiReqJson">Request JSON</label>
							<div class="col-sm-9">
								<textarea id="txtarea" class="form-control" name="sgApiReqJson" placeholder="Enter request json payload"></textarea>
							</div>
						</div>
						<div id="sgApiRespDiv" class="form-group">
							<label class="control-label col-sm-3" for="sgApiResp">Expected Response</label>
							<div class="col-sm-9">
								<textarea id="txtarea" class="form-control" name="sgApiResp" placeholder="Enter expected response string"></textarea>
							</div>
						</div>

						<div id="authKeyDiv" class="form-group">
							<label class="control-label col-sm-3" for="authKey">Auth Key</label>
							<div class="col-sm-9">
								<input type="password" class="form-control" name="authKey" placeholder="Enter authorization key">
							</div>
						</div>

						<div class="form-group">
							<div class="col-sm-offset-3 col-sm-3">
								<button id="testBttn" type="submit" class="btn">Test</button>
							</div>
							<div class="col-sm-6">
								<button id="compBttn" type="submit" class="btn">Submit</button>
							</div>
						</div>
					</form>
				</div>
				<div class="col-md-2 column"></div>
			</div>
			<div class="row clearfix"></div>
			<div class="col-md-12 column">
				<legend>Result</legend>
				<div id="resultContainer" class="well">
					<div style="color: #2093BB" id="result"></div>
					<span style="color: #2093BB"></span>
				</div>
			</div>
		</div>
	</div>
</body>
<script>
	$(document).on('change', '#hcApiCallType', function() {
		var selectedOption = $("#hcApiCallType option:selected").val();

		if (selectedOption === "POST") {
			$('#hcApiReqJsonDiv').show();
		} else {
			$('#hcApiReqJsonDiv').hide();
		}
	});

	$(document).on('change', '#fgApiCallType', function() {
		var selectedOption = $("#fgApiCallType option:selected").val();

		if (selectedOption === "POST") {
			$('#fgApiReqJsonDiv').show();
		} else {
			$('#fgApiReqJsonDiv').hide();
		}
	});

	$(document).on('change', '#sgApiCallType', function() {
		var selectedOption = $("#sgApiCallType option:selected").val();

		if (selectedOption === "POST") {
			$('#sgApiReqJsonDiv').show();
		} else {
			$('#sgApiReqJsonDiv').hide();
		}
	});
</script>
</html>