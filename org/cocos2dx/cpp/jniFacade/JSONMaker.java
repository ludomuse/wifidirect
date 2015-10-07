package org.cocos2dx.cpp.jniFacade;

import java.util.List;
import java.lang.String;

/**
 * This is the JSONMaker class. Its aim is to create the JSON Strings, no more,
 * no less.
 */

public class JSONMaker {

	public static String MakeJSONFromDeviceList(List<String> deviceList)
	{
		int n = deviceList.size();
		String sJSONDeviceList = "{\"deviceArray\":[";
		for (int i = 0; i < n; i++)
		{
			sJSONDeviceList = sJSONDeviceList + "{\"name\":";
			sJSONDeviceList = sJSONDeviceList + deviceList.get(i);
			sJSONDeviceList = sJSONDeviceList + "\"}";
			if (i != (n - 1))
			{
				sJSONDeviceList = sJSONDeviceList + ",";
			}
		}
		sJSONDeviceList = sJSONDeviceList + "]}";
		return sJSONDeviceList;
	}

}