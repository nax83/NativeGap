package it.ale.nativegap;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.LOG;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MyActivity extends Activity implements CordovaInterface {
	
	public static final String MSG_INSTRUMENT = "nc_instrument";
	public static final String MSG_ADDBTN = "nc_add_button";
	public static final String MSG_SHOWBTN = "showbtn";
	public static final String MSG_HIDEBTN = "hidebtn";
	public static final String MSG_ADDTEXT = "nc_add_text";
	public static final String MSG_LOADFINISH = "onPageFinished";
	
	CordovaWebView mainView;
	
	private Dialog dialog;
	private Object activityResultKeepRunning;
	private Object keepRunning;
	private CordovaPlugin activityResultCallback;
	private final String TAG = "MyHdActivityNew";
	private final ExecutorService threadPool = Executors.newCachedThreadPool();

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		
		
    // creo il dialog per lo splash screen
	dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar);
	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setCancelable(false);
		
    dialog.setContentView(R.layout.splash);
       
    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
    lp.copyFrom(dialog.getWindow().getAttributes());
    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        
    System.out.println("before dialog");
    
    dialog.show();
    dialog.getWindow().setAttributes(lp);
			
    System.out.println("before mainview");
	mainView = (CordovaWebView) findViewById(R.id.mainview);
	mainView.loadUrl("file:///android_asset/www/index.html",1000);
		
	}

	protected void onPause() {
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
        
     // Send pause event to JavaScript
 		this.mainView
 				.loadUrl("javascript:try{cordova.fireDocumentEvent('pause');}catch(e){console.log('exception firing pause event from native');};");

 		// Forward to plugins
 		if (this.mainView.pluginManager != null) {
 			this.mainView.pluginManager.onPause(true);
 		}
        
    }
	
	@Override
	/**
	 * Called when the activity will start interacting with the user.
	 */
	protected void onResume() {
		super.onResume();

		if (this.mainView == null) {
			return;
		}

		// Send resume event to JavaScript
		this.mainView
				.loadUrl("javascript:try{cordova.fireDocumentEvent('resume');}catch(e){console.log('exception firing resume event from native');};");

		// Forward to plugins
		if (this.mainView.pluginManager != null) {
			this.mainView.pluginManager.onResume(true);
		}

	}
	
    @Override
    protected void onStop() {
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
    }	
    
    @Override
    public void onDestroy() {
        if (this.mainView != null) {

			// Send destroy event to JavaScript
			this.mainView
					.loadUrl("javascript:try{cordova.require('cordova/channel').onDestroy.fire();}catch(e){console.log('exception firing destroy event from native');};");

			// Load blank page so that JavaScript onunload is called
			this.mainView.loadUrl("about:blank");
			mainView.handleDestroy();
		}
        
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
    		mainView.loadUrl("javascript: console.log('calling javascript'); var e = jQuery.Event('backbutton'); jQuery('body').trigger( e );");
    		return true;
    	}
    	return super.onKeyDown(keyCode, event);
    }
    
    @Override
   	public Object onMessage(String id, Object data) {
   		LOG.d("MHA", "onMessage(" + id + "," + data + ")");
   		
   		if (MSG_ADDBTN.equals(id)) {
   			System.out.println("Adding button with Id " + id);
   			final String name = (String)data;
   			   			
   			int resId = getResources().getIdentifier(name, "id", getPackageName());
   			ImageView b = (ImageView) findViewById(resId);
   			b.setOnClickListener(new View.OnClickListener() {
   			
   				@Override
   				public void onClick(View v) {
   					System.out.println("clicked?");
   					mainView.postMessage("nc_click", name);
   					
   				}
   			});
   			
   		} else if (MSG_SHOWBTN.equals(id)) {
   			final String name = (String)data;
   			
   			runOnUiThread(new Runnable() {
   	            public void run() {
   	            	int resId = getResources().getIdentifier(name, "id", getPackageName());
   	            	View b = findViewById(resId);
   	            	b.setVisibility(View.VISIBLE);
   	    		}
   	        });
   			
   		} else if (MSG_HIDEBTN.equals(id)) {
   			final String name = (String)data;
   			runOnUiThread(new Runnable() {
   	            public void run() {
   	            	int resId = getResources().getIdentifier(name, "id", getPackageName());
   	            	View b = findViewById(resId);
   	            	b.setVisibility(View.GONE);
   	    		}
   	        });
   			
   		} else if (MSG_ADDTEXT.equals(id)) {
   			
   			final String[] arg = (String[])data;
   			final String name = arg[0];
   			final String text = arg[1];
   			
   			System.out.println("Adding text with Id " + id + " and name "+name+" and text: "+text);
   			
   			runOnUiThread(new Runnable() {
   	            public void run() {
   	            	int resId = getResources().getIdentifier(name, "id", getPackageName());
   	            	TextView b = (TextView) findViewById(resId);
   	            	b.setText(text);
   	    		}
   	        });
   		} else if(MSG_LOADFINISH.equals(id)) {
   			// nascondo lo spash screen
   			Log.d(TAG, "Caricamento Web View Ultimato.");
   			dialog.dismiss();   			
   		}
   		
   		if ("exit".equals(id)) {
   			super.finish();
   		}
   		
   		return null;
   	}
   	@Override
   	public void setActivityResultCallback(CordovaPlugin plugin) {
   		this.activityResultCallback = plugin;

   	}

   	@Override
   	public void startActivityForResult(CordovaPlugin command, Intent intent,
   			int requestCode) {
   		this.activityResultCallback = command;
   		this.activityResultKeepRunning = this.keepRunning;

   		// If multitasking turned on, then disable it for activities that return
   		// results
   		if (command != null) {
   			this.keepRunning = false;
   		}

   		// Start activity
   		super.startActivityForResult(intent, requestCode);

   	}

   	@Override
   	protected void onActivityResult(int requestCode, int resultCode,
   			Intent intent) {
   		super.onActivityResult(requestCode, resultCode, intent);
   		CordovaPlugin callback = this.activityResultCallback;
   		if (callback != null) {
   			callback.onActivityResult(requestCode, resultCode, intent);
   		}
   	}
   	
    
	@Override
	public void cancelLoadUrl() {
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	public Context getContext() {
		return this;
	}

	@Override
	public ExecutorService getThreadPool() {
		return threadPool;
	}

}
