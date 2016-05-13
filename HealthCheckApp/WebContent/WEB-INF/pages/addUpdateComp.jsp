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
<link rel=icon type=image/ico
	href="http://i3.sdlcdn.com/img/icons/favicon.ico" />
</head>
<body>

	<div id="header" class="pointer"></div>
	<div id="main-content">
		<div class="container">



			<div class="row clearfix">
				<!-- Modal Start here-->
				<div class="modal fade bs-example-modal-sm" id="myPleaseWait"
					role="dialog" aria-hidden="true" data-backdrop="static">
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
				<div class="modal fade bs-example-modal-sm" id="myLoading"
					role="dialog" aria-hidden="false" data-backdrop="static">
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
						<div class="form-group">
							<label class="col-md-3 control-label" for="comp">Component</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="comp"
									placeholder="Enter component name">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-3 control-label" for="qmSpoc">QM SPOC</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="qmSpoc"
									placeholder="Enter qm spoc">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-3 control-label" for="qaSpoc">QA SPOC</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="qaSpoc"
									placeholder="Enter qa spoc with comma , separated">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-3 control-label" for="endpoint">Endpoint</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="endpoint"
									placeholder="http://{ip / host}:{port}">
							</div>
						</div>
						
						<hr>
						<div class="form-group">
							<label class="control-label col-sm-6">Health Check</label>
							<div class="col-sm-6"></div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-3" for="hcApi">API</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="hcApi"
									placeholder="Enter health check api">
							</div>
						</div>
						<div id="hcCallType" class="form-group">
							<label class="control-label col-sm-3" for="hcCallType">Call
								Type</label>
							<div class="col-sm-9">
								<select name="hcCallType" class="form-control"
									id="hcCallType-select">
									<option value="GET">GET</option>
									<option value="GETJSON">GET with JSON</option>
								</select>
							</div>
						</div>
<!-- 						<div id="hcReqJsonDiv" class="form-group" style="display: none"> -->
<!-- 							<label class="control-label col-sm-3" for="hcReqJson">Request -->
<!-- 								JSON</label> -->
<!-- 							<div class="col-sm-9"> -->
<!-- 								<textarea id="txtarea" class="form-control" name="hcReqJson" -->
<!-- 									placeholder="Enter Request JSON"></textarea> -->
<!-- 							</div> -->
<!-- 						</div> -->
						<div class="form-group">
							<label class="control-label col-sm-3" for="hcExpResp">Expected
								Resp</label>
							<div class="col-sm-9">
								<textarea id="txtarea" class="form-control" name="hcExpResp"
									placeholder="Enter expected response"></textarea>
							</div>
						</div>

						<hr>
						<div class="form-group">
							<label class="control-label col-sm-6">1st Getter API</label>
							<div class="col-sm-6"></div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-3" for="fgApi">API</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="fgApi"
									placeholder="Enter health check api">
							</div>
						</div>
						<div id="fgCallType" class="form-group">
							<label class="control-label col-sm-3" for="fgCallType">Call
								Type</label>
							<div class="col-sm-9">
								<select name="fgCallType" class="form-control"
									id="fgCallType-select">
									<option value="GET">GET</option>
									<option value="GETJSON">GET with JSON</option>
									<option value="POST">POST</option>
								</select>
							</div>
						</div>
						<div id="fgReqJsonDiv" class="form-group" style="display: none">
							<label class="control-label col-sm-3" for="fgReqJson">Request
								JSON</label>
							<div class="col-sm-9">
								<textarea id="txtarea" class="form-control" name="fgReqJson"
									placeholder="Enter Request JSON"></textarea>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-3" for="fgExpResp">Expected
								Resp</label>
							<div class="col-sm-9">
								<textarea id="txtarea" class="form-control" name="fgExpResp"
									placeholder="Enter expected response"></textarea>
							</div>
						</div>

						<hr>
						<div class="form-group">
							<label class="control-label col-sm-6">2nd Getter API</label>
							<div class="col-sm-6"></div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-3" for="sgApi">API</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="sgApi"
									placeholder="Enter health check api">
							</div>
						</div>
						<div id="sgCallType" class="form-group">
							<label class="control-label col-sm-3" for="sgCallType">Call
								Type</label>
							<div class="col-sm-9">
								<select name="sgCallType" class="form-control"
									id="sgCallType-select">
									<option value="GET">GET</option>
									<option value="GETJSON">GET with JSON</option>
									<option value="POST">POST</option>
								</select>
							</div>
						</div>
						<div id="sgReqJsonDiv" class="form-group" style="display: none">
							<label class="control-label col-sm-3" for="sgReqJson">Request
								JSON</label>
							<div class="col-sm-9">
								<textarea id="txtarea" class="form-control" name="sgReqJson"
									placeholder="Enter Request JSON"></textarea>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-3" for="sgExpResp">Expected
								Resp</label>
							<div class="col-sm-9">
								<textarea id="txtarea" class="form-control" name="sgExpResp"
									placeholder="Enter expected response"></textarea>
							</div>
						</div>

						<div class="form-group">
							<label class="control-label col-sm-3" for="authKey">Auth
								Key</label>
							<div class="col-sm-9">
								<input type="password" class="form-control" name="authKey"
									placeholder="Enter authorization key">
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
	$(document).on('change', '#hcCallType', function() {

		var selectedOption = $("#hcCallType option:selected").val();

		if (selectedOption === "POST") {
			$('#hcReqJsonDiv').show();
		} else {
			$('#hcReqJsonDiv').hide();
		}
	});

	$(document).on('change', '#fgCallType', function() {

		var selectedOption = $("#fgCallType option:selected").val();

		if (selectedOption === "POST") {
			$('#fgReqJsonDiv').show();
		} else {
			$('#fgReqJsonDiv').hide();
		}
	});

	$(document).on('change', '#sgCallType', function() {

		var selectedOption = $("#sgCallType option:selected").val();

		if (selectedOption === "POST") {
			$('#sgReqJsonDiv').show();
		} else {
			$('#sgReqJsonDiv').hide();
		}
	});
	
</script>
</html>