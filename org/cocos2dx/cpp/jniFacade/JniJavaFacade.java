package org.cocos2dx.cpp.jniFacade;

import java.io.File;
import java.lang.String;

/**
 * These method are called by c++, when the cpp part of the WifiDirectFacade
 * want to send data or launch a service (request peer and connection)
 * 
 * @author Rani, Gregoire
 * 
 */
public class JniJavaFacade {

	public static WifiDirectFacade _wifiDirectFacade;

	public static void send(String s)
	{
		_wifiDirectFacade.send(s);
	}

	public static void send(boolean b)
	{
		_wifiDirectFacade.send(b);
	}

	public static void send(int i)
	{
		_wifiDirectFacade.send(i);
	}

	public static void send(long l)
	{
		_wifiDirectFacade.send(l);
	}

	public static void send(char c)
	{
		_wifiDirectFacade.send(c);
	}

	public static void send(double d)
	{
		_wifiDirectFacade.send(d);
	}

	public static void sendFile(String path)
	{
		_wifiDirectFacade.send(new File(path));
	}

	public static void sendByte(byte b)
	{
		_wifiDirectFacade.send(b);
	}

	public static void sendBytes(byte[] bytes)
	{
		_wifiDirectFacade.send(bytes);
	}

	public static void send(float f)
	{
		_wifiDirectFacade.send(f);
	}

	public static void discoverPeers()
	{
		_wifiDirectFacade.discoverPeers();
	}

	public static void connectTo(String deviceName)
	{
		_wifiDirectFacade.connectTo(deviceName);
	}

}