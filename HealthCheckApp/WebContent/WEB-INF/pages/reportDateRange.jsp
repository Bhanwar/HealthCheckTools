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

				<div class="col-md-12 column">
					<form id="reportGenForm" class="form-horizontal" role="form">


						<div class="form-group">
							<label class="col-md-2 control-label" for="startDate">Start
								Date</label>
							<div class="col-md-3">
								<input type="text" class="form-control" name="startDate"
									placeholder="dd-mm-yyy">
							</div>
							<label class="col-md-2 control-label" for="endDate">End
								Date</label>
							<div class="col-md-3">
								<input type="text" class="form-control" name="endDate"
									placeholder="dd-mm-yyy">
							</div>
							<div class="col-md-2">
								<button id="reportGenSubmitBttn" type="submit" class="btn"
									value="reportGen">Submit</button>
							</div>
						</div>


					</form>
				</div>
			</div>
			<div class="row clearfix"></div>
			<br><hr><br>
			<div class="col-md-12 column">
				<div id="reportGenTableDiv" style="text-align: center;"></div>
			</div>
		</div>
	</div>
</body>
<script>

$(window).load(function() {
	$('#reportGenSubmitBttn').click(function(e) {
		e.preventDefault(); // STOP default action

		var api = $('#reportGenSubmitBttn').val();
		var form = "#" + api + "Form";
		var MyForm = $(form).serializeJSON();
		$("#result").empty();
		$('#myPleaseWait').modal('show');
		$.ajax({
			url : "/healthCheck/" + api,
			type : "POST",
			contentType : "application/json",
			data : JSON.stringify(MyForm),
			success : function(data) {
				$('#myPleaseWait').modal('hide');
				populateTable(data)
			},
			error : function(jqXHR, textStatus, errorThrown) {
				$('#myPleaseWait').modal('hide');
				console.log(textStatus + " : " + errorThrown);
			}
		});

		return false;
	});
});

function populateTable(data) {
	$("#reportGenTableDiv").empty();
	$("#reportGenTableDiv").append(data);
}

function sticky_relocate() {	
	var scroll = $(window).scrollTop();
    var anchor_top = $("#reportGenTable").offset().top;
    var anchor_bottom = $("#bottom_anchor").offset().top;
    if (scroll>anchor_top && scroll<anchor_bottom) {
    	$("#cloneTable tr th").each(function(index){
    		var index2 = index;
    	    $(this).width(function(index2){
    	    	return $("#reportGenTable tr th").eq(index).width();
    	    });
    	});
    	$('#cloneTable').show();
    } else {
    	$('#cloneTable').hide();
    }	
}

$(function () {
	$(window).scroll(sticky_relocate);
    sticky_relocate();
});
</script>
</html>