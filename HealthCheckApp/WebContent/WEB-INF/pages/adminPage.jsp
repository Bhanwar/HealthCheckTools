<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List"%>
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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Health Check Admin</title>
<link rel=icon type=image/ico
	href="http://i3.sdlcdn.com/img/icons/favicon.ico" />
</head>
<body>
	<!-- Modal Start here-->
			<div class="modal fade bs-example-modal-sm" id="myPleaseWait"
				role="dialog" aria-hidden="true" data-backdrop="static">
				<div class="modal-dialog modal-sm">
					<div class="modal-content">
						<div class="modal-header">
							<h4 class="modal-title">
								<span class="glyphicon glyphicon-time"> </span> Please wait...
							</h4>
						</div>
					</div>
				</div>
			</div>
	<div id="header" class="adminHdr pointer"></div>
	<div id="main-content">
		<div id="summary">
			<button type="button" class="btn btn-default btn-lg bttn-padding" id="updateEndpoint">Update Endpoint Details</button>
			<br>
			<button type="button" class="btn btn-default btn-lg bttn-padding" id="updateAuthKey">Update Auth Key</button>
			<br>
			<button type="button" class="btn btn-default btn-lg bttn-padding" id="updateReason">Update Server Down Time Reason</button>
			<br>
			<button type="button" class="btn btn-default btn-lg bttn-padding" id="getDateRangeReport">Get Report for Date Range</button>
			<br>
			<button type="button" class="btn btn-default btn-lg bttn-padding" id="resetAuthKey">Reset Comp Auth Key (Admin only)</button>
			<br>
			<button type="button" class="btn btn-default btn-lg bttn-padding" id="addComp">Add Or Update Components (Admin only)</button>
			<br>
			<button type="button" class="btn btn-default btn-lg bttn-padding" id="deleteComp">Delete Component (Admin only)</button>
			<br>
			
		</div>
	</div>
</body>
<script type="text/javascript">

	$(document).ready(function() {
		
		$('#header').click(function(e) {
			$('#myPleaseWait').modal('show');
			window.location = "/healthCheck";
		});
		
		$('#addComp').click(function() {
			$('#myPleaseWait').modal('show');
			window.location = "/healthCheck/admin/addUpdateComp";
		});

		$('#updateReason').click(function() {
			$('#myPleaseWait').modal('show');
			window.location = "/healthCheck/admin/updateReasonPage";
		});
		
		$('#updateAuthKey').click(function() {
			$('#myPleaseWait').modal('show');
			window.location = "/healthCheck/admin/updateAuthKey";
		});
		
		$('#updateEndpoint').click(function() {
			$('#myPleaseWait').modal('show');
			window.location = "/healthCheck/admin/endpointUpdate";
		});
		
		$('#getDateRangeReport').click(function() {
			$('#myPleaseWait').modal('show');
			window.location = "/healthCheck/admin/getReport";
		});
		
		$('#resetAuthKey').click(function() {
			$('#myPleaseWait').modal('show');
			window.location = "/healthCheck/admin/resetAuthKey";
		});
		
		$('#deleteComp').click(function() {
			$('#myPleaseWait').modal('show');
			window.location = "/healthCheck/admin/deleteComponent";
		});
		
		
	});
</script>
</html>