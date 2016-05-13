function sticky_relocate() {
	var scroll = $(window).scrollTop();
	var anchor_top = $("#dashDataTable").offset().top;
	var anchor_bottom = $("#bottom_anchor").offset().top;
	if (scroll > anchor_top && scroll < anchor_bottom) {
		$("#cloneTable tr th").each(function(index) {
			var index2 = index;
			$(this).width(function(index2) {
				return $("#dashDataTable tr th").eq(index).width();
			});
		});
		$('#cloneTable').show();
	} else {
		$('#cloneTable').hide();
	}
}

function updateReason() {
	window.location = "/healthCheck/updateReasonPage";
}

$(function() {
	$('.toolTipster').tooltipster({
		contentAsHTML : true,
		animation : 'grow'
	});
	$(window).scroll(sticky_relocate);
	sticky_relocate();
});

$(window).load(function() {
	// AJAX call for submit button
	$('#submitBttn').click(function(e) {
		e.preventDefault(); // STOP default action

		var api = $('#submitBttn').val();
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
				$("#result").empty();
				$('#myPleaseWait').modal('hide');
				$("#result").html(data);
				document.getElementById('result').scrollIntoView()
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
	
	$('#compBttn').prop('disabled', true);
	
	// AJAX call for test button
	$('#testBttn').click(function(e) {
		e.preventDefault(); // STOP default action
		
		var form = "#addUpdateCompForm";
		var MyForm = $(form).serializeJSON();
		
		$("#result").empty();
		$('#myPleaseWait').modal('show');
		$.ajax({
			url : "/healthCheck/checkComp",
			type : "POST",
			contentType : "application/json",
			data : JSON.stringify(MyForm),
			success : function(data) {
				$("#result").empty();
				$('#myPleaseWait').modal('hide');
				testComp(data);
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
	$('#compBttn').click(function(e) {
		e.preventDefault(); // STOP default action
		
		var form = "#addUpdateCompForm";
		var MyForm = $(form).serializeJSON();
		
		$("#result").empty();
		$('#myPleaseWait').modal('show');
		$.ajax({
			url : "/healthCheck/addUpdateComp",
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


function testComp(data) {
	console.log(data);
	for (var key in data) {
		$("#result").html(data[key]);
		if(key == "true") {
			$('#compBttn').prop('disabled', false);
		} else {
			$('#compBttn').prop('disabled', true);
		}
	}
	document.getElementById('result').scrollIntoView()
};