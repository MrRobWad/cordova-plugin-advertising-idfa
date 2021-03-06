package com.mrrobwad.cordova.android.idfa;

/**
 * Class AdvertisingIDFA
 * Description: Extends cordova plugin to get advertiser id (IDFA) for Android using Google Play API
 * Author: mrrobwad
 */

//Cordova imports
import org.apache.cordova.CordovaInterface; 
import org.apache.cordova.CallbackContext; 
import org.apache.cordova.CordovaPlugin; 
import org.apache.cordova.CordovaWebView;

//Android imports
import android.content.Context;
import android.app.Application;
import android.app.Activity;
//import android.telephony.TelephonyManager;
import android.util.Log;

//JSON Imports
import org.json.JSONArray; 
import org.json.JSONObject;
import org.json.JSONException;

//Java Imports
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

////PG Class Imports
//import com.personagraph.api.PGAgent;
//import com.personagraph.api.PGSensorState;
//import com.personagraph.api.PGSettings;
//import com.personagraph.api.PGUserProfileCallback;
//import com.personagraph.api.PGSourceType;

//pgcore import
import com.mrrobwad.cordova.android.idfa.CoreException;


public class AdvertisingIDFA extends CordovaPlugin {

    Context context;
    Application app;   
    boolean enableLog;
    Activity activity;
    public static final String TAG = "AdvertisingIDFA";

    //Constructor function.
    public void initialize(CordovaInterface cordova, CordovaWebView webView) { 
        super.initialize(cordova, webView);
        context = this.cordova.getActivity().getApplicationContext();
        app = this.cordova.getActivity().getApplication(); 
        activity = this.cordova.getActivity();
        enableLog =  false; 
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException { 
        try {
            if ("getAdInfo".equals(action)) {
                getAdInfo(callbackContext);
            } else if ("getAdId".equals(action)) {
                getAdId(callbackContext);
            } else if ("isTrackingEnabled".equals(action)) {
                isTrackingEnabled(callbackContext);
            }

            return true;
        }catch (Exception e){
            if(enableLog){
                e.printStackTrace();
                System.err.println("AdvertisingIDFA execute Exception: " + e.getMessage());
            }
            callbackContext.error(e.getMessage());
            return false;
        }
    }

    @Override
    public void onPause(boolean multitasking) {

        if(enableLog)
            Log.d(TAG, "Activity on pause is called");

        super.onPause(multitasking);
    }

    @Override
    public void onResume(boolean multitasking) {

        if(enableLog)
            Log.d(TAG, "Activity on resume is called");

        super.onResume(multitasking);
    }

    /**
     * Driver function to get advertiser info.
     * @param callbackContext
     * @param flag
     *  0: adId and trackingEnabled are returned (defaults).
     *  1: adId is returned
     *  2: trackingEnabled is returned
     */
    private void _getAdInfo(final CallbackContext callbackContext, final int flag) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    AdvertisingIdClient.AdInfo adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
                    String advertisingId = adInfo.getId();
                    boolean trackingEnabled = !adInfo.isLimitAdTrackingEnabled();

                    JSONObject result = new JSONObject();

                    switch(flag) {

                        case 1:
                            result.put("adId", advertisingId);
                            break;
                        case 2:

                            result.put("trackingEnabled", trackingEnabled);
                            break;

                        case 0:
                        default:

                            result.put("adId", advertisingId);
                            result.put("trackingEnabled", trackingEnabled);
                            break;
                    }
                    callbackContext.success(result);
                } catch (Exception e) {
                    callbackContext.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Function to get advertiser id info.
     * @param callbackContext
     */
    public void getAdInfo(final CallbackContext callbackContext) {
        final int flag = 0;
        _getAdInfo(callbackContext, flag);
    }

    /**
     * Function to get advertiser id.
     * @param callbackContext
     */
    public void getAdId(final CallbackContext callbackContext) {
        final int flag = 1;
        _getAdInfo(callbackContext, flag);
    }

    /**
     * Function to get limit advertisement flag.
     * @param callbackContext
     */
    public void isTrackingEnabled(final CallbackContext callbackContext) {
        final int flag = 2;
        _getAdInfo(callbackContext, flag);
    }

}