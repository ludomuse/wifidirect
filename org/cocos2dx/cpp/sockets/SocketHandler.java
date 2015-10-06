package org.cocos2dx.cpp.sockets;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class SocketHandler {

	private ClientSocketHandler client;
	private ServerSocketHandler server;

	private String lastMethodName;
	private Object[] args;
	private Class<?>[] parameterTypes;

	private int listenningPort;

	public SocketHandler(int bufferLen, int listenningPort)
	{
		client = new ClientSocketHandler(bufferLen);
		this.listenningPort = listenningPort;
	}

	public static void registerCallBackReceiver(CallBackMethod onReceiveString,
			CallBackMethod onReceiveInt, CallBackMethod onReceiveBool,
			CallBackMethod onReceiveFloat, CallBackMethod onReceiveDouble,
			CallBackMethod onReceiveByte, CallBackMethod onReceiveLong,
			CallBackMethod onReceiveFile, CallBackMethod onReceiveByteArray,
			CallBackMethod onReceiveChar)
	{
		ServerSocketHandler.registerCallBackReceiver(onReceiveString,
				onReceiveInt, onReceiveBool, onReceiveFloat, onReceiveDouble,
				onReceiveByte, onReceiveLong, onReceiveFile,
				onReceiveByteArray, onReceiveChar);
	}

	public void resend()
	{
		try
		{
			getClass().getMethod(lastMethodName, parameterTypes).invoke(this,
					args);
		}
		catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (NoSuchMethodException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isConnected()
	{
		return client.isConnected();
	}

	public void stop()
	{
		if (server != null)
			server.stop();
	}

	public boolean isDettachedFromRemoteHost()
	{
		return client.isDettachedFromRemoteHost();
	}

	public void dettachFromRemoteHost()
	{
		client.dettachFromRemoteHost();
	}

	public void notifyIsDisconnectedFromNetwork()
	{
		stop();
		dettachFromRemoteHost();
	}

	public void listen(int port)
	{
		server = new ServerSocketHandler(port, this);
		server.listen();
	}

	public void send(File f)
	{
		lastMethodName = "send";
		args = new Object[] { f };
		parameterTypes = new Class<?>[] { File.class };
		server.waitForAccuse();
		client.send(f);
	}
	
	public void send(byte[] bytes)
	{
		lastMethodName = "send";
		args = new Object[] { bytes };
		parameterTypes = new Class<?>[] { byte[].class };
		server.waitForAccuse();
		client.sendBytes(bytes);
	}
	
	public void send(double d)
	{
		lastMethodName = "send";
		args = new Object[] { d };
		parameterTypes = new Class<?>[] { Double.class };
		server.waitForAccuse();
		client.send(d);
	}
	
	public void send(long l)
	{
		lastMethodName = "send";
		args = new Object[] { l };
		parameterTypes = new Class<?>[] { Long.class };
		server.waitForAccuse();
		client.send(l);
	}
	
	public void send(float f)
	{
		lastMethodName = "send";
		args = new Object[] { f };
		parameterTypes = new Class<?>[] { Float.class };
		server.waitForAccuse();
		client.send(f);
	}

	public void send(byte b)
	{
		lastMethodName = "send";
		args = new Object[] { b };
		parameterTypes = new Class<?>[] { Byte.class };
		server.waitForAccuse();
		client.send(b);
	}

	public void send(char c)
	{
		lastMethodName = "send";
		args = new Object[] { c };
		parameterTypes = new Class<?>[] { Character.class };
		server.waitForAccuse();
		client.send(c);
	}

	public void send(int i)
	{
		lastMethodName = "send";
		args = new Object[] { i };
		parameterTypes = new Class<?>[] { Integer.class };
		server.waitForAccuse();
		client.send(i);
	}

	public void send(boolean b)
	{
		lastMethodName = "send";
		args = new Object[] { b };
		parameterTypes = new Class<?>[] { Boolean.class };
		server.waitForAccuse();
		client.send(b);
	}

	public void send(String str)
	{
		lastMethodName = "send";
		args = new Object[] { str };
		parameterTypes = new Class<?>[] { String.class };
		server.waitForAccuse();
		client.send(str);
	}

	public void sendIP()
	{
		lastMethodName = "sendIP";
		args = new Object[] {};
		parameterTypes = new Class<?>[] {};
		server.waitForAccuse();
		client.sendIP(listenningPort);
	}

	public void sendAccuse()
	{
		client.sendAccuse();
	}

	public void keepAlive()
	{
		client.notifyServer();
	}

	public static String getThisDeviceIpAddress()
	{
		return getDottedDecimalIP(getLocalIPAddress());
	}

	private static byte[] getLocalIPAddress()
	{
		try
		{
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();)
			{
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();)
				{
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress())
					{
						if (inetAddress instanceof Inet4Address)
						{ // fix for Galaxy Nexus. IPv4 is easy to use :-)
							return inetAddress.getAddress();
						}
						// return inetAddress.getHostAddress().toString(); //
						// Galaxy Nexus returns IPv6
					}
				}
			}
		}
		catch (SocketException ex)
		{
			// Log.e("AndroidNetworkAddressFactory", "getLocalIPAddress()", ex);
		}
		catch (NullPointerException ex)
		{
			// Log.e("AndroidNetworkAddressFactory", "getLocalIPAddress()", ex);
		}
		return null;
	}

	private static String getDottedDecimalIP(byte[] ipAddr)
	{
		// convert to dotted decimal notation:
		String ipAddrStr = "";
		for (int i = 0; i < ipAddr.length; i++)
		{
			if (i > 0)
			{
				ipAddrStr += ".";
			}
			ipAddrStr += ipAddr[i] & 0xFF;
		}
		return ipAddrStr;
	}

	public void setOnReceiveIPCallBack(CallBackMethod cm)
	{
		server.setOnReceiveIPCallBack(cm);
	}

	public void setRemoteHost(String hostAddress, int listenningPort2)
	{
		client.setRemoteHost(hostAddress, listenningPort);

	}

	public void setOnReceiveAccuseCallBack(CallBackMethod cm)
	{
		server.setOnReceiveAccuseCallBack(cm);
	}

	public void stopHandlers()
	{
		if (server != null)
			server.stopHandlers();
		if (client != null)
			client.stopHandlers();
	}
}
