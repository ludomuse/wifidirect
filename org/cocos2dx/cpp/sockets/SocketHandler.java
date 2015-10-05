package org.cocos2dx.cpp.sockets;

import java.lang.reflect.InvocationTargetException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.cocos2dx.cpp.DebugManager;
import org.cocos2dx.cpp.wifiDirect.WifiDirectManager;

import android.os.Handler;
import android.util.Log;

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

	private Handler handler = new Handler();
	private Runnable worker;

	/*private String remoteIp;
	private int remotePort;
	boolean wasConnectedAtLeastOneTime = false;*/
	
	public void connect(final String ip, final int port, final boolean sendIpToServer)
	{
		/*remoteIp = ip;
		remotePort = port;*/
		
		worker = new Runnable() {

			@Override
			public void run()
			{
				if (client.isConnected())
				{
					DebugManager.print("Connected to server !", WifiDirectManager.DEBUGGER_CHANNEL);
					if(sendIpToServer)
					{
						sendIP();
					}
					else
					{
						sendAccuse();
					}
				}
				else
				{
					DebugManager.print("Connection to server fail. Trying again...", WifiDirectManager.DEBUGGER_CHANNEL);
					client.connect(ip, port);
					handler.postDelayed(worker, 500);
				}
			}

		};
		handler.post(worker);
	}

	public void stop()
	{
		if (server != null)
			server.stop();
	}

	public void listen(int port)
	{
		server = new ServerSocketHandler(port, this);
		server.listen();
	}

	public void send(String str)
	{
		
		this.resetServerNotificator();
		lastMethodName = "send";
		args = new Object[] { str };
		parameterTypes = new Class<?>[] { String.class };
		server.waitForAccuse();
		client.send(str);
		this.rearmServerNotificator();
	}

	public void sendIP()
	{
		this.resetServerNotificator();
		lastMethodName = "sendIP";
		args = new Object[] {};
		parameterTypes = new Class<?>[] {};
		server.waitForAccuse();
		client.sendIP(listenningPort);
		this.rearmServerNotificator();
	}

	public void sendAccuse()
	{
		this.resetServerNotificator();
		client.sendAccuse();
		this.rearmServerNotificator();
	}

	private Handler notificator = new Handler();
	
	private Runnable notificatorTask = new Runnable()
	{

		@Override
		public void run()
		{
			client.notifyServer();
			notificator.postDelayed(notificatorTask, 1000);
		}
		
	};
	
	public void resetServerNotificator()
	{
		notificator.removeCallbacks(notificatorTask);
	}
	
	public void rearmServerNotificator()
	{
		notificator.postDelayed(notificatorTask, 1000);
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
}
