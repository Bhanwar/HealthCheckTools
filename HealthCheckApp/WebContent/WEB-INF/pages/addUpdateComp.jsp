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
							<label class="col-md-3 control-label" for="updateComp">Update?</label>
							<div id="updateCompCheck" class="col-md-9">
								<label class="radio-inline"> <input type="radio"
									class="updateCompClass" name="updateComp" value="true">Yes
								</label> <label class="radio-inline"> <input type="radio"
									class="updateCompClass" name="updateComp" value="false" checked="checked"> No
								</label>
							</div>
						</div>
						
						<div id="updateCompDiv" class="form-group" style="display: none">
							<label class="col-md-3 control-label" for="updateCompName">Select Component</label>
							<div class="col-sm-9">
								<select class="form-control" id="updateComp-select"
									name="updateCompName">
									<option disabled selected>--Select Component--</option>
								</select>
							</div>
						</div>
						<div id="compText" class="form-group">
							<label class="col-md-3 control-label" for="compName">Component</label>
							<div class="col-sm-9">
								<input id="compNameInputDiv" type="text" class="form-control" name="compName"
									placeholder="Enter component name">
							</div>
						</div>
						<div id="compDropDown" class="form-group" style="display: none">
							<label class="col-md-3 control-label" for="tokenCompName">Component</label>
							<div class="col-sm-9">
								<select class="form-control" id="comp-select"
									name="tokenCompName">

								</select>
							</div>
						</div>

						<div id="compTypeDropDown" class="form-group">
							<label class="col-md-3 control-label" for="compType">Component
								Type</label>
							<div class="col-sm-9">
								<select class="form-control" id="compType-select"
									name="compType">

								</select>
							</div>
						</div>

						<div class="form-group">
							<label class="col-md-3 control-label" for="enabled">Enabled</label>
							<div id="enabledCheck" class="col-md-9">
								<label class="radio-inline"> <input type="radio" id="enabledInputYes"
									class="enabledClass" name="enabled" value="true"
									checked="checked">Yes
								</label> <label class="radio-inline"> <input type="radio" id="enabledInputNo"
									class="enabledClass" name="enabled" value="false"> No
								</label>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-3 control-label" for="qmSpoc">QM
								SPOC</label>
							<div class="col-sm-9">
								<input id="qmSpocInputDiv" type="text" class="form-control" name="qmSpoc"
									placeholder="Enter qm spoc">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-3 control-label" for="qaSpoc">QA
								SPOC</label>
							<div class="col-sm-9">
								<input id="qaSpocInputDiv" type="text" class="form-control" name="qaSpoc"
									placeholder="Enter qa spoc with comma , separated">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-3 control-label" for="endpoint">Endpoint</label>
							<div class="col-sm-9">
								<input id="endpointInputDiv" type="text" class="form-control" name="endpoint"
									placeholder="http://{ip / host}:{port}">
							</div>
						</div>

						<div id="tomcatDataDiv">
							<div id="tokenRequiredDiv" class="form-group">
								<label class="control-label col-sm-3" for="tokenRequired">Token
									required?</label>
								<div class="col-md-9">
									<label class="radio-inline"> <input type="radio" id=""
										name="tokenRequired" value="true">Yes
									</label> <label class="radio-inline"> <input type="radio"
										name="tokenRequired" value="false" checked="checked">
										No
									</label>
								</div>
							</div>

							<div id="loginApiDiv" style="display: none">
								<hr>
								<div class="form-group">
									<label class="control-label col-sm-6">Login API</label>
									<div class="col-sm-6"></div>
								</div>
								<div class="form-group">
									<label class="control-label col-sm-3" for="loginApiUrl">API
										URL</label>
									<div class="col-sm-9">
										<input type="text" class="form-control" name="loginApiUrl"
											placeholder="Enter login api url">
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-sm-3" for="loginApiCallType">Call
										Type</label>
									<div class="col-sm-9">
										<select name="loginApiCallType" class="form-control"
											id="loginApiCallType-select">
											<option value="GET">GET</option>
											<option value="POST">POST</option>
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-sm-3" for="loginApiReqJson">Request
										JSON</label>
									<div class="col-sm-9">
										<textarea id="txtarea" class="form-control"
											name="loginApiReqJson"
											placeholder="Enter request json payload"></textarea>
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-sm-3" for="loginInvalidCredMsg">Invalid
										Credentials Message</label>
									<div class="col-sm-9">
										<textarea id="txtarea" class="form-control"
											name="loginInvalidCredMsg"
											placeholder="Enter invalid credentials message"></textarea>
									</div>
								</div>
							</div>

							<hr>
							<div class="form-group">
								<label class="control-label col-sm-6">Health Check API</label>
								<div class="col-sm-6"></div>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-3" for="hcApiUrl">API
									URL</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="hcApiUrl" id="hcApiUrlInputDiv"
										placeholder="Enter health check api url">
								</div>
							</div>
							<div id="hcApiCallTypeDiv" class="form-group">
								<label class="control-label col-sm-3" for="hcApiCallType">Call
									Type</label>
								<div class="col-sm-9">
									<select name="hcApiCallType" class="form-control"
										id="hcApiCallType-select">
										<option value="GET">GET</option>
										<option value="POST">POST</option>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-3" for="hcApiHeadersJson">Headers
									as JSON</label>
								<div class="col-sm-9">
									<textarea id="hcApiHeadersJsonInputDiv" class="form-control"
										name="hcApiHeadersJson"
										placeholder="Enter headers in json format"></textarea>
								</div>
							</div>
							<div id="hcApiReqJsonDiv" class="form-group"
								style="display: none">
								<label class="control-label col-sm-3" for="hcApiReqJson">Request
									JSON</label>
								<div class="col-sm-9">
									<textarea id="hcApiReqJsonInputDiv" class="form-control" name="hcApiReqJson"
										placeholder="Enter request json payload"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-3" for="hcApiResp">Expected
									Response</label>
								<div class="col-sm-9">
									<textarea id="hcApiRespInputDiv" class="form-control" name="hcApiResp"
										placeholder="Enter expected response string"></textarea>
								</div>
							</div>

							<hr>
							<div class="form-group">
								<label class="control-label col-sm-6">1st Get API</label>
								<div class="col-sm-6"></div>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-3" for="fgApiUrl">API
									URL</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="fgApiUrl" id="fgApiUrlInputDiv"
										placeholder="Enter 1st getter api url">
								</div>
							</div>
							<div id="fgApiCallTypeDiv" class="form-group">
								<label class="control-label col-sm-3" for="fgApiCallType">Call
									Type</label>
								<div class="col-sm-9">
									<select name="fgApiCallType" class="form-control"
										id="fgApiCallType-select">
										<option value="GET">GET</option>
										<option value="POST">POST</option>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-3" for="fgApiHeadersJson">Headers
									as JSON</label>
								<div class="col-sm-9">
									<textarea id="fgApiHeadersJsonInputDiv" class="form-control"
										name="fgApiHeadersJson"
										placeholder="Enter headers in json format">{"Content-Type":"application/json"}</textarea>
								</div>
							</div>
							<div id="fgApiReqJsonDiv" class="form-group"
								style="display: none">
								<label class="control-label col-sm-3" for="fgApiReqJson">Request
									JSON</label>
								<div class="col-sm-9">
									<textarea id="fgApiReqJsonInputDiv" class="form-control" name="fgApiReqJson"
										placeholder="Enter request json payload"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-3" for="fgApiResp">Expected
									Response</label>
								<div class="col-sm-9">
									<textarea id="fgApiRespInputDiv" class="form-control" name="fgApiResp"
										placeholder="Enter expected response string"></textarea>
								</div>
							</div>

							<hr>
							<div class="form-group">
								<label class="control-label col-sm-6">2nd Get API</label>
								<div class="col-sm-6"></div>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-3" for="sgApiUrl">API
									URL</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="sgApiUrl" id="sgApiUrlInputDiv"
										placeholder="Enter 2nd getter api url">
								</div>
							</div>
							<div id="sgApiCallTypeDiv" class="form-group">
								<label class="control-label col-sm-3" for="sgApiCallType">Call
									Type</label>
								<div class="col-sm-9">
									<select name="sgApiCallType" class="form-control"
										id="sgApiCallType-select">
										<option value="GET">GET</option>
										<option value="POST">POST</option>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-3" for="sgApiHeadersJson">Headers
									as JSON</label>
								<div class="col-sm-9">
									<textarea id="sgApiHeadersJsonInputDiv" class="form-control"
										name="sgApiHeadersJson"
										placeholder="Enter headers in json format">{"Content-Type":"application/json"}</textarea>
								</div>
							</div>
							<div id="sgApiReqJsonDiv" class="form-group"
								style="display: none">
								<label class="control-label col-sm-3" for="sgApiReqJson">Request
									JSON</label>
								<div class="col-sm-9">
									<textarea id="sgApiReqJsonInputDiv" class="form-control" name="sgApiReqJson"
										placeholder="Enter request json payload"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-3" for="sgApiResp">Expected
									Response</label>
								<div class="col-sm-9">
									<textarea id="sgApiRespInputDiv" class="form-control" name="sgApiResp"
										placeholder="Enter expected response string"></textarea>
								</div>
							</div>
						</div>
						<div id="jarDataDiv" style="display: none">
						
						</div>

						<div class="form-group">
							<label class="control-label col-sm-3" for="authKey">Admin Auth
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
var updateCompDetails;
	$(window)
			.load(
					function() {
						$('#myLoading').modal('show');
						$
								.ajax({
									url : "/healthCheck/getCompEnums",
									type : "GET",
									contentType : "application/json",
									success : function(data) {
										console.log(data);
										updateCompList(data);
									},
									error : function(jqXHR, textStatus,
											errorThrown) {
										$('#myLoading').modal('hide');
										console.log(textStatus + " : "
												+ errorThrown);
										alert("Could not populate component list, please contact rajeev.agile@snapdeal.com!");
									}
								});
					});

	function updateCompList(enumMap) {
		console.log("Creating component list..");
		var selectList = document.getElementById('comp-select');
		var updateList = enumMap['tokenApiEnum'];
		for ( var i in updateList) {
			var option = document.createElement('option');
			option.value = updateList[i];
			option.text = updateList[i];
			selectList.appendChild(option);
		}
		selectList = document.getElementById('compType-select');
		updateList = enumMap['compTypesEnum'];
		for ( var i in updateList) {
			var option = document.createElement('option');
			option.value = updateList[i];
			option.text = updateList[i];
			selectList.appendChild(option);
		}
		$('#myLoading').modal('hide');
	};

	$(document).on('change', '#enabledCheck', function() {
		var checked = $('.enabledClass:checked').val();
		enabledRadio(checked);
	});
	
	$(document).on('change', '#updateCompCheck', function() {
		var checked = $('.updateCompClass:checked').val();
		if (checked === 'true') {
			if(updateCompDetails == null) {
				$('#myLoading').modal('show');
				$.ajax({
					url : "/healthCheck/admin/getCompsForUpdate",
					type : "GET",
					contentType : "application/json",
					success : function(data) {
						updateCompDetails = data;
						createUpdateCompList(updateCompDetails);
						$('#myLoading').modal('hide');
					},
					error : function(jqXHR, textStatus,
							errorThrown) {
						$('#myLoading').modal('hide');
						console.log(textStatus + " : "
								+ errorThrown);
						alert("Could not populate component list, please contact rajeev.agile@snapdeal.com!");
					}
				});
			}
		} else {
			document.getElementById("addUpdateCompForm").reset();
		}
		
		$('#updateCompDiv').toggle();
	});
	
	function createUpdateCompList(updateCompMap) {
		var selectList = document.getElementById('updateComp-select');
		for(var key in updateCompMap) {
			var option = document.createElement('option');
			option.value = key;
			option.text = key;
			selectList.appendChild(option);
		}
	}

	$(document).on('change', '#tokenRequiredDiv', function() {
		toggleTokenDivs();
	});

	$(document).on('change', '#hcApiCallTypeDiv', function() {
		var selectedOption = $("#hcApiCallTypeDiv option:selected").val();
		hcApiCallType(selectedOption);
	});

	$(document).on('change', '#compTypeDropDown', function() {
		var selectedOption = $("#compTypeDropDown option:selected").val();
		componentTypeDivsToggle(selectedOption);
	});

	$(document).on('change', '#fgApiCallTypeDiv', function() {
		var selectedOption = $("#fgApiCallTypeDiv option:selected").val();
		fgApiCallType(selectedOption);
	});

	$(document).on('change', '#sgApiCallTypeDiv', function() {
		var selectedOption = $("#sgApiCallTypeDiv option:selected").val();
		sgApiCallType(selectedOption);
	});
	
	$(document).on('change', '#updateComp-select', function() {
		var selectedOption = $("#updateComp-select option:selected").val();
		populateCompDetails(selectedOption);
	});
	
	function populateCompDetails(compName) {
		var uiData = updateCompDetails[compName];
		$('#compNameInputDiv').val(uiData.compName);
		selectItemByValue('compType-select',uiData.compDetails.componentType);
		componentTypeDivsToggle(uiData.compDetails.componentType);
		if(uiData.compDetails.enabled === true)
			selectRadioButton('enabledInputYes');
		else
			selectRadioButton('enabledInputNo');
		enabledRadio(uiData.compDetails.enabled);
		$('#qmSpocInputDiv').val(getData(uiData.compDetails.qmSpoc));
		$('#qaSpocInputDiv').val(getData(uiData.compDetails.qaSpoc));
		$('#endpointInputDiv').val(getData(uiData.compDetails.endpoint));
		
		$('#hcApiUrlInputDiv').val(getData(uiData.compDetails.healthCheckApi));
		selectItemByValue('hcApiCallType-select',uiData.compDetails.healthCheckApiCallType);
		hcApiCallType(uiData.compDetails.healthCheckApiCallType);
		$('#hcApiHeadersJsonInputDiv').val(getData(uiData.compDetails.healthCheckHeaders));
		$('#hcApiReqJsonInputDiv').val(getData(uiData.compDetails.healthCheckApiReqJson));
		$('#hcApiRespInputDiv').val(getData(uiData.compDetails.healthCheckApiResp));
		
		$('#fgApiUrlInputDiv').val(getData(uiData.compDetails.firstGetApi));
		selectItemByValue('fgApiCallType-select',uiData.compDetails.firstGetApiCallType);
		fgApiCallType(uiData.compDetails.firstGetApiCallType);
		$('#fgApiHeadersJsonInputDiv').val(getData(uiData.compDetails.firstGetHeaders));
		$('#fgApiReqJsonInputDiv').val(getData(uiData.compDetails.firstGetApiReqJson));
		$('#fgApiRespInputDiv').val(getData(uiData.compDetails.firstGetApiResp));
		
		$('#sgApiUrlInputDiv').val(getData(uiData.compDetails.secondGetApi));
		selectItemByValue('sgApiCallType-select',uiData.compDetails.secondGetApiCallType);
		sgApiCallType(uiData.compDetails.secondGetApiCallType);
		$('#sgApiHeadersJsonInputDiv').val(getData(uiData.compDetails.secondGetHeaders));
		$('#sgApiReqJsonInputDiv').val(getData(uiData.compDetails.secondGetApiReqJson));
		$('#sgApiRespInputDiv').val(getData(uiData.compDetails.secondGetApiResp));
		
	}
	
	function getData(data) {
		if(data == null)
			return '';
		return data;
	}
	
	function selectItemByValue(elmntId, value) {
		var elmnt = document.getElementById(elmntId);
		for(var i=0; i < elmnt.options.length; i++) {
			if(elmnt.options[i].value == value) {
				elmnt.selectedIndex = i;
				return;
			}
		}
	}
	
	function selectRadioButton(id) {
		document.getElementById(id).checked = true;
	}
	
	function enabledRadio(enabled) {
		console.log(enabled);
		if (enabled === 'false') {
			console.log("Enabling..")
			$('#compBttn').prop('disabled', false);
		} else {
			console.log("Disabling..")
			$('#compBttn').prop('disabled', true);
		}
	}
	
	function componentTypeDivsToggle(compType) {
		if (compType === "TOMCAT") {
			$('#tomcatDataDiv').show();
			$('#jarDataDiv').hide();
		}
		if (compType === "JAR") {
			$('#tomcatDataDiv').hide();
			$('#jarDataDiv').show();
		}
	}
	
	function toggleTokenDivs() {
		$('#loginApiDiv').toggle();
		$('#compDropDown').toggle();
		$('#compText').toggle();
	}
	
	function hcApiCallType(callType) {
		if (callType === "POST") {
			$('#hcApiReqJsonDiv').show();
		} else {
			$('#hcApiReqJsonDiv').hide();
		}
	}
	
	function fgApiCallType(callType) {
		if (callType === "POST") {
			$('#fgApiReqJsonDiv').show();
		} else {
			$('#fgApiReqJsonDiv').hide();
		}
	}
	
	function sgApiCallType(callType) {
		if (callType === "POST") {
			$('#sgApiReqJsonDiv').show();
		} else {
			$('#sgApiReqJsonDiv').hide();
		}
	}
</script>
</html>