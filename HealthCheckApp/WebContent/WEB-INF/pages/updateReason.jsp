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
					<form id="updateReasonForm" class="form-horizontal" role="form">
						<div class="form-group">
							<label class="col-md-3 control-label" for="comp">Server
								Down </label>
							<div class="col-md-9">
								<select name="comp" class="form-control" id="update-select">

								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-3" for="reason">Reason
								Code</label>
							<div class="col-sm-9">
								<select name="reason" class="form-control" id="reason-select">

								</select>
							</div>
						</div>

						<div class="form-group">
							<label class="control-label col-sm-3" for="description">Description</label>
							<div class="col-sm-9">
								<textarea rows="8" class="form-control clearInput"
									name="description" placeholder="Enter description"></textarea>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-3" for="authKey">Auth
								Key</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="authKey"
									placeholder="Enter authorization key">
							</div>
						</div>

						<div class="form-group">
							<div class="col-sm-offset-3 col-sm-9">
								<button id="submitBttn" type="submit" class="btn"
									value="updateReason">Submit</button>
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
<script type="text/javascript">
	$(window)
			.load(
					function() {
						$('#myLoading').modal('show');
						$
								.ajax({
									url : "/healthCheck/getUpdateList",
									type : "GET",
									contentType : "application/json",
									success : function(data) {
										console.log(data);
										updateOptions(data);
									},
									error : function(jqXHR, textStatus,
											errorThrown) {
										$('#myLoading').modal('hide');
										console.log(textStatus + " : "
												+ errorThrown);
										alert("Could not populate update list, please contact rajeev.agile@snapdeal.com!");
									}
								});
					});

	$(window)
			.load(
					function() {
						$
								.ajax({
									url : "/healthCheck/getReasonCodes",
									type : "GET",
									contentType : "application/json",
									success : function(data) {
										console.log(data);
										reasonOptions(data);
									},
									error : function(jqXHR, textStatus,
											errorThrown) {
										$('#myLoading').modal('hide');
										console.log(textStatus + " : "
												+ errorThrown);
										alert("Could not populate reason codes list, please contact rajeev.agile@snapdeal.com!");
									}
								});
					});

	function updateOptions(updateMap) {
		console.log("Creating update list..");
		var selectList = document.getElementById('update-select');
		for ( var key in updateMap) {
			var option = document.createElement('option');
			option.value = key;
			option.text = updateMap[key];
			selectList.appendChild(option);
		}
		$('#myLoading').modal('hide');
	};

	function reasonOptions(reasonList) {
		console.log("Creating reason list..");
		var selectList = document.getElementById('reason-select');
		console.log(reasonList.length);
		for ( var i in reasonList) {
			var option = document.createElement('option');
			option.value = reasonList[i];
			option.text = reasonList[i];
			selectList.appendChild(option);
		}
	};
</script>
</html>