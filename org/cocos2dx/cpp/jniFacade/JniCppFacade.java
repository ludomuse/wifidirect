package org.cocos2dx.cpp.jniFacade;

import java.io.File;
import java.util.List;


public class JniCppFacade {

	public static final int DEVICE_LIST_CHANGE = 0;
	
	public static native boolean notify(int tag);

	public static void onGettingPeers(List<String> peers)
	{
		// TODO Auto-generated method stub
		
	}

	public static void onBeingConnected()
	{
		// TODO Auto-generated method stub
		
	}

	public static void onReceiving(String s)
	{
		// TODO Auto-generated method stub
		
	}

	public static void onReceiving(int i)
	{
		// TODO Auto-generated method stub
		
	}

	public static void onReceiving(boolean b)
	{
		// TODO Auto-generated method stub
		
	}

	public static void onReceiving(long l)
	{
		// TODO Auto-generated method stub
		
	}

	public static void onReceiving(File f)
	{
		// TODO Auto-generated method stub
		
	}

	public static void onReceiving(float f)
	{
		// TODO Auto-generated method stub
		
	}

	public static void onReceiving(double d)
	{
		// TODO Auto-generated method stub
		
	}

	public static void onReceiving(byte[] bytes)
	{
		// TODO Auto-generated method stub
		
	}
	
		
}
