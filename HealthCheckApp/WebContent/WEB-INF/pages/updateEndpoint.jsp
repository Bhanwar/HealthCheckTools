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
					<form id="updateEndpointForm" class="form-horizontal" role="form">
						
						<div id="updateCompDiv" class="form-group">
							<label class="col-md-3 control-label" for="updateCompName">Select Component</label>
							<div class="col-sm-9">
								<select class="form-control" id="updateComp-select"
									name="updateCompName">
									<option disabled selected>--Select Component--</option>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-3 control-label" for="endpoint">Endpoint</label>
							<div class="col-sm-9">
								<input id="endpointInputDiv" type="text" class="form-control" name="endpoint"
									placeholder="http://{ip / host}:{port}">
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
								<button id="testEPBttn" type="submit" class="btn">Test</button>
							</div>
							<div class="col-sm-6">
								<button id="compEPBttn" type="submit" class="btn">Submit</button>
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
	
	$(window).load(function() {
		$('#myLoading').modal('show');
		$.ajax({
			url : "/healthCheck/admin/getCompsForEndpointUpdate",
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
		
		
		$('#compEPBttn').prop('disabled', true);
		
		// AJAX call for test button
		$('#testEPBttn').click(function(e) {
			e.preventDefault(); // STOP default action
			
			var form = "#updateEndpointForm";
			var MyForm = $(form).serializeJSON();
			
			$("#result").empty();
			$('#myPleaseWait').modal('show');
			$.ajax({
				url : "/healthCheck/checkCompForEndpointUpdate",
				type : "POST",
				contentType : "application/json",
				data : JSON.stringify(MyForm),
				success : function(data) {
					$("#result").empty();
					$('#myPleaseWait').modal('hide');
					testCompEP(data);
				},
				error : function(jqXHR, textStatus, errorThrown) {
					$("#result").empty();
					$('#myPleaseWait').modal('hide');
					$("#result").html(textStatus + " : " + errorThrown);
					document.getElementById('result').scrollIntoView()
				}
			});
			
			return false;
		});
		
		// AJAX call for submit button
		$('#compEPBttn').click(function(e) {
			e.preventDefault(); // STOP default action
			
			var form = "#updateEndpointForm";
			var MyForm = $(form).serializeJSON();
			
			$("#result").empty();
			$('#myPleaseWait').modal('show');
			$.ajax({
				url : "/healthCheck/updateEndpoint",
				type : "POST",
				contentType : "application/json",
				data : JSON.stringify(MyForm),
				success : function(data) {
					$("#result").empty();
					$('#myPleaseWait').modal('hide');
					$("#result").html(data);
				},
				error : function(jqXHR, textStatus, errorThrown) {
					$("#result").empty();
					$('#myPleaseWait').modal('hide');
					$("#result").html(textStatus + " : " + errorThrown);
					document.getElementById('result').scrollIntoView()
				}
			});
			
			return false;
		});
	});
	
	function testCompEP(data) {
		console.log(data);
		for (var key in data) {
			$("#result").html(data[key]);
			if(key == "true") {
				$('#compEPBttn').prop('disabled', false);
			} else {
				$('#compEPBttn').prop('disabled', true);
			}
		}
		document.getElementById('result').scrollIntoView()
	};
	
	function createUpdateCompList(updateCompMap) {
		var selectList = document.getElementById('updateComp-select');
		for(var key in updateCompMap) {
			var option = document.createElement('option');
			option.value = key;
			option.text = key;
			selectList.appendChild(option);
		}
	}

	$(document).on('change', '#updateComp-select', function() {
		var selectedOption = $("#updateComp-select option:selected").val();
		$('#endpointInputDiv').val(updateCompDetails[selectedOption]);
	});
</script>
</html>