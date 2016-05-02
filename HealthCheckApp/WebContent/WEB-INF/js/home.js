function sticky_relocate() {	
	var scroll = $(window).scrollTop();
    var anchor_top = $("#dashDataTable").offset().top;
    var anchor_bottom = $("#bottom_anchor").offset().top;
    if (scroll>anchor_top && scroll<anchor_bottom) {
    	$("#cloneTable tr th").each(function(index){
    		var index2 = index;
    	    $(this).width(function(index2){
    	    	return $("#dashDataTable tr th").eq(index).width();
    	    });
    	});
    	$('#cloneTable').show();
    } else {
    	$('#cloneTable').hide();
    }	
}

$(function () {
	$('.toolTipster').tooltipster({
		contentAsHTML : true,
		animation : 'grow'
	});
	$(window).scroll(sticky_relocate);
    sticky_relocate();
});
