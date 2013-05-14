$(document).ready(function() {
	document.addEventListener('deviceready', onDeviceReady, false);
});

function onDeviceReady() {
	backB = {
		id : 'exit',
		label : 'exit',
		clickedCb : function() {
			console.log("native button handler");
			var e = jQuery.Event("backbutton");
		}
	};
	logoutB = {
		id : 'logout',
		label : 'logout',
		clickedCb : function() {
			$('body').append('<p>Click</p>');
		}
	};

	window.plugins.nativeControls.addButton(backB);
	window.plugins.nativeControls.addButton(logoutB);
	
	window.plugins.nativeControls.showButton(backB);
	window.plugins.nativeControls.showButton(logoutB);

}