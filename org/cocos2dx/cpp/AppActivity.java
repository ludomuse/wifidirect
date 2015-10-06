//package com.IHMTEK.LudoMuse;
package org.cocos2dx.cpp;

import android.app.Activity;

import org.cocos2dx.cpp.wifiDirect.WifiDirectManager;
import org.cocos2dx.lib.Cocos2dxActivity;
import android.os.Bundle;

import java.lang.Override;
import java.util.List;

import android.annotation.TargetApi;
import android.os.Build;


/**
 * This is the main activity for LudoMuse. THERE MUST NOT BE ANY GRAPHICS
 * COMPONENTS (Buttons, TextFiels...) IN THE WHOLE JAVA PART OF LUDOMUSE, BUT
 * ESPECIALLY IN THIS CLASS. If there are any graphics component, they might
 * show up upon LudoMuse startup, regardless of the AppDelegate or any C++ entry
 * point This class is to replace the MainActivity.java class, but should not
 * depend on any graphics component as stated earlier.
 */

public class AppActivity extends Cocos2dxActivity {
	private static AppActivity instance;

	private WifiDirectManager _wifiDirectManager;



	public static Activity getInstance()
	{
		return instance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// DebugManager.printInfo();
		instance = this;
		super.onCreate(savedInstanceState);
		
		_wifiDirectManager = new WifiDirectManager(this);
		_wifiDirectManager.initialize();
	}

	/**
	 * This method calls the discoverPeers method of the WifiP2pManager and gets
	 * all the Wifi Direct equipped devices that can be found as a list of their
	 * names.
	 * 
	 * @return List<String> containing all the peers discovered
	 */
	public static void DiscoverPeers()
	{
		instance._wifiDirectManager.launchServicePeersDiscovering(null);
	}

	/**
	 * No idea what this shit does. But it seems like you need to discover
	 * services before attempting to connect to another devices, otherwise you
	 * get a code 3 error.
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public static void startRegistrationAndDiscovery()
	{
		instance._wifiDirectManager.startRegistrationAndDiscovery();
	}

	/**
	 * Gets the address associated with the name entered as a parameter and
	 * proceeds to connect to said device.
	 * 
	 * @param name
	 */
	public static void ConnectToDevice(String name)
	{
		instance._wifiDirectManager.connectToPeer(name, null);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
	}

	/* register the broadcast receiver with the intent values to be matched */
	@Override
	protected void onResume()
	{
		super.onResume();
		_wifiDirectManager.resume();
	}

	/* unregister the broadcast receiver */
	@Override
	protected void onPause()
	{
		super.onPause();
		_wifiDirectManager.pause();
	}

	public List<String> getDeviceList()
	{
		return _wifiDirectManager.getDeviceList();
	}



	// ---------------------------------------------------------------------------------------------------------------//

}