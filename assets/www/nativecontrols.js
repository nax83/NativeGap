function NativeControls() {
};

NativeControls.prototype.controls = [];
/*
 * button json example: {
 * id: 'mybutton'
 * label: 'back'
 * clickedCb: function
 * }
 */
NativeControls.prototype.init = function(mmm) {
	
    if(arguments.length === 0) {
    	console.log('no controls');
    	return;
    }
	this.controls = arguments; 
	
	var tmp = [];
	for (var i=0; i < arguments.length; i++){
		console.log("funziona?" + arguments[i].id);
		tmp[i] = {'id': arguments[i].id};
	}
	var that = this;
	cordova.exec(function(i) {
		console.log('called ' + i.id);
		if(typeof that.controls[i.id] != undefined){
			that.controls[0].clickedCb();
		}
	}, undefined, "NativeControls", "instrument", tmp);
};

NativeControls.prototype.addButton = function(button) {
	
	console.log("into addbutton "+button.id);
	
	this.controls[button.id] = button;
	cordova.exec(undefined, undefined, "NativeControls", "addbutton", [button.id]);
};

NativeControls.prototype.addText = function(textview) {
	
	console.log("into addtextview "+textview.id+", text is: "+textview.text);
	
	this.controls[textview.id] = textview;
	cordova.exec(undefined, undefined, "NativeControls", "addtextview", [textview.id, textview.text]);
};

NativeControls.prototype.removeButton = function(button) {
	
    if(this.controls[button.id] != undefined){
    	delete this.controls[button.id];
		cordova.exec(undefined, undefined, "NativeControls", "removebutton", [button.id]);
    }
	
};

NativeControls.prototype.clicked = function(id) {
	console.log(id +" "+ this.controls[id]);
	if(this.controls[id] != undefined){
		this.controls[id].clickedCb();
	}
}

NativeControls.prototype.showButton = function(button) {
    cordova.exec(undefined, undefined, "NativeControls", "showbutton", [button.id]);
};

NativeControls.prototype.hideButton = function(button) {
    cordova.exec(undefined, undefined, "NativeControls", "hidebutton", [button.id]);
};

if (!window.plugins) {
	window.plugins = {};
}

if (!window.plugins.nativeControls) {
	window.plugins.nativeControls = new NativeControls();
}
