package org.cocos2dx.cpp.jniFacade;


/**
 * This class define all the native methods to be called
 * by the java part of the WifiDirectFacade when receiving data, 
 * in order to notifiy cpp part of the WifiDirectFacade object.
 * 
 * @author Gregoire
 *
 */
public class JniCppFacade {

	
	public static native void onGettingPeers(Object peers);
	
	public static native void onReceivingString(String s);

	public static native void onReceivingInt(int i);

	public static native void onReceivingBool(boolean b);

	public static native void onReceivingLong(long l);

	public static native void onReceivingFile(String path);

	public static native void onReceivingFloat(float f);
	
	public static native void onReceivingDouble(double d);

	public static native void onReceivingChar(char d);
	
	public static native void onReceivingByte(byte d);
	
	public static native void onReceivingBytes(byte[] bytes);
	
		
}
