package org.cocos2dx.cpp.jniFacade;

import android.app.Activity;
import org.cocos2dx.cpp.AppActivity;
import org.cocos2dx.cpp.JSONMaker;
import java.lang.String;

/**
 * This is the Java side of the JNI Interface for LudoMuse. For each method that
 * uses the JNI methods in the C++ class LmJniJavaFacade, there should be an
 * associated Java method here (the method that is called). There might be more
 * methods (such as the internal methods that set the JSON Strings, and so
 * on...), that are not necessarily linked with a C++ method. No bijection here.
 */

public class JniJavaFacade {

	private static Activity activity = AppActivity.getInstance();

	public static String GetDiscoveredPeers()
	{
		return JSONMaker.MakeJSONFromDeviceList(((AppActivity)activity).getDeviceList());
	}
	

	/**
	 * Calls the DiscoverPeers() method from AppActivity gets the device list it
	 * returns and then makes the JSON String using JSONMaker.java
	 * 
	 * @return
	 */
	public static void DiscoverPeers()
	{

		AppActivity.DiscoverPeers();
		/*activity.runOnUiThread(new Runnable() {
			@Override
			public void run()
			{
				List<String> deviceList = new Vector<String>();
				String sJSONDevices;
				deviceList = AppActivity.DiscoverPeers();
				sJSONDevices = JSONMaker.MakeJSONFromDeviceList(deviceList);
				setmsJSONDevices(sJSONDevices);
			}
		});
		return msJSONDevices;*/
		
	}


	/**
	 * calls the ConnectToDevice method from AppActivity using deviceName as a
	 * parameter. Doesn't return anything yet, but we might want to consider it
	 * returning a boolean or something to tell us if everything went well or
	 * not.
	 * 
	 * @param deviceName
	 */
	public static void ConnectToDevice(String deviceName)
	{
		AppActivity.ConnectToDevice(deviceName);
	}


}