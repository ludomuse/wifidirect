package org.cocos2dx.cpp.jniFacade;


import java.io.File;
import java.lang.String;

/**
 * This is the Java side of the JNI Interface for LudoMuse. For each method that
 * uses the JNI methods in the C++ class LmJniJavaFacade, there should be an
 * associated Java method here (the method that is called). There might be more
 * methods (such as the internal methods that set the JSON Strings, and so
 * on...), that are not necessarily linked with a C++ method. No bijection here.
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
	

	public static void ConnectTo(String deviceName)
	{
		_wifiDirectFacade.connectTo(deviceName);
	}


}