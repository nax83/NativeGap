package com.phonegap.plugins.NativeControls;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class NativeControls extends Plugin {

	private CallbackContext clickCallback;
	public static final String ACTION_INSTRUMENT = "instrument";
	public static final String ACTION_ADDBTN = "addbutton";
	public static final String ACTION_ADDTEXT = "addtextview";
	public static final String ACTION_REMOVEBTN = "removebutton";
	public static final String ACTION_SHOWBTN = "showbutton";
	public static final String ACTION_HIDEBTN = "hidebutton";
	
	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		System.out.println("called " + action+" "+args.getString(0));
		//JSONObject obj = (JSONObject) args.get(0);
		//System.out.println("Mmmm" + obj.getString("id"));
		if(ACTION_INSTRUMENT.equals(action)){
			if(callbackContext == null)System.out.println("why null??");
			else System.out.println("ok");
			clickCallback = callbackContext;
			//this.webView.postMessage("nativeheader", "show");
			
			this.webView.postMessage("nc_instrument", "exit");
			PluginResult pr = new PluginResult(PluginResult.Status.NO_RESULT);
			pr.setKeepCallback(true);
			clickCallback.sendPluginResult(pr);
			
		}else if(ACTION_ADDBTN.equals(action)){
			
			this.webView.postMessage("nc_add_button", args.getString(0));
			callbackContext.success("ok");
			
		} else if(ACTION_SHOWBTN.equals(action)){
			System.out.println("action showbtn");
			this.webView.postMessage("showbtn", args.getString(0));
			callbackContext.success("ok");
			
		} else if(ACTION_HIDEBTN.equals(action)){
			this.webView.postMessage("hidebtn", args.getString(0));
			callbackContext.success("ok");
			
		} else if(ACTION_ADDTEXT.equals(action)){
			
			String[] arg = new String[2];
			
			arg[0] = new String(args.getString(0));
			arg[1] = new String(args.getString(1));
			
			System.out.println("into addtext, text is: "+args.getString(1));
			this.webView.postMessage("nc_add_text", arg);
			callbackContext.success("ok");
			
		}
		
		return true;
	}
	
	@Override
	public Object onMessage(String id, Object data) {
		// TODO Auto-generated method stub
		if("nativeheader".equals(id))return null;
		if ("nc_click".equals(id)) {
			System.out.println("nativeheader nc_clicked");
			String d = (String) data;
			JSONObject obj = new JSONObject();
			try {
				obj.put("id", d);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.webView.loadUrl("javascript:window.plugins.nativeControls.clicked('" + d +"');");
		
		}
		
		return null;
	}

	@Override
	public PluginResult execute(String arg0, JSONArray arg1, String arg2) {
		// TODO Auto-generated method stub
		return null;
	}
}
