package org.cocos2dx.cpp.sockets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.ExecutionException;

import org.cocos2dx.cpp.DebugManager;
import org.cocos2dx.cpp.wifiDirect.WifiDirectManager;

import android.os.AsyncTask;
import android.util.Log;

class ConnectTask2 implements Runnable
{
	private String host;
	private int port;
	private Socket socket;

	public ConnectTask2(String host, int port, Socket socket)
	{
		this.host = host;
		this.port = port;
		this.socket = socket;
	}

	public void execute()
	{
		new Thread(this).start();
	}

	public void run()
	{

		try
		{

			socket.bind(null);
			DebugManager.print("Trying to reach address " + host + ":" + port,
					WifiDirectManager.DEBUGGER_CHANNEL);
			socket.connect(new InetSocketAddress(host, port), 500);
			// socket.connect((new InetSocketAddress(host, port)), 500);
			// DebugManager.print(socket.isConnected() ? "connected to server" :
			// "connection to server fail", WifiDirectManager.DEBUGGER_CHANNEL);
		}
		catch (IllegalArgumentException e)
		{
			// Log.d("A", e.getMessage() + "");
			DebugManager.print(
					"argument exception occure during connection to server at "
							+ host + ":" + port,
					WifiDirectManager.DEBUGGER_CHANNEL);
		}
		catch (IOException e)
		{
			// Log.d("A", e.getMessage() + "");
			DebugManager.print(
					"IO Exception occure during connection to server at "
							+ host + ":" + port,
					WifiDirectManager.DEBUGGER_CHANNEL);
		}
	}
}

//class ConnectTask extends AsyncTask<Void, Void, Void> {
//
//	private String host;
//	private int port;
//	private Socket socket;
//
//	public ConnectTask(String host, int port, Socket socket)
//	{
//		super();
//		this.host = host;
//		this.port = port;
//		this.socket = socket;
//	}
//
//	@Override
//	protected Void doInBackground(Void... args)
//	{
//
//		try
//		{
//
//			socket.bind(null);
//			DebugManager.print("Trying to reach address " + host + ":" + port,
//					WifiDirectManager.DEBUGGER_CHANNEL);
//			socket.connect(new InetSocketAddress(host, port), 500);
//			// socket.connect((new InetSocketAddress(host, port)), 500);
//			// DebugManager.print(socket.isConnected() ? "connected to server" :
//			// "connection to server fail", WifiDirectManager.DEBUGGER_CHANNEL);
//		}
//		catch (IllegalArgumentException e)
//		{
//			// Log.d("A", e.getMessage() + "");
//			DebugManager.print(
//					"argument exception occure during connection to server at "
//							+ host + ":" + port,
//					WifiDirectManager.DEBUGGER_CHANNEL);
//		}
//		catch (IOException e)
//		{
//			// Log.d("A", e.getMessage() + "");
//			DebugManager.print(
//					"IO Exception occure during connection to server at "
//							+ host + ":" + port,
//					WifiDirectManager.DEBUGGER_CHANNEL);
//		}
//		return null;
//	}
//
//}

public class ClientSocketHandler {

	private Socket socket;

	private byte[] buf;

	public ClientSocketHandler(int len)
	{
		super();
		buf = new byte[len];
		socket = new Socket();

	}

	public boolean isConnected()
	{
		return socket.isConnected();
	}

	public void connect(String host, int port)
	{
		DebugManager.print("connecting to " + host + ":" + port,
				WifiDirectManager.DEBUGGER_CHANNEL);
		
		  ConnectTask2 connectTask = new ConnectTask2(host, port, socket);
		  connectTask.execute();
	}

	private byte[] concat(PACKET_TYPE entete, byte[] message)
	{
		byte[] res = new byte[message.length + 1];
		res[0] = (byte) entete.toInt();
		System.arraycopy(message, 0, res, 1, message.length);
		return res;
	}

	private byte[] toByte(String str)
	{
		int len = str.length();
		byte[] res = new byte[len + 1];
		for (int i = 0; i < len; i++)
		{
			res[i] = (byte) str.charAt(i);
		}
		res[len] = (byte) '\0';
		return res;
	}

	public void send(String str)
	{
		DebugManager.print("sending string " + str,
				WifiDirectManager.DEBUGGER_CHANNEL);
		byte[] res = concat(PACKET_TYPE.STRING, toByte(str));
		InputStream stream = new ByteArrayInputStream(res);
		send(stream);
	}

	public void sendAccuse()
	{
		DebugManager.print("sending accuse...",
				WifiDirectManager.DEBUGGER_CHANNEL);
		byte[] res = new byte[] { (byte) PACKET_TYPE.ACCUSE.toInt() };
		InputStream stream = new ByteArrayInputStream(res);
		send(stream);
	}

	public void sendIP(int clientListenningPort)
	{
		DebugManager.print("sending IP...", WifiDirectManager.DEBUGGER_CHANNEL);
		String address = getClientIpAddress() + "!" + clientListenningPort;
		byte[] res = concat(PACKET_TYPE.IP, toByte(address));
		InputStream stream = new ByteArrayInputStream(res);
		send(stream);
	}

	public void notifyServer()
	{
		DebugManager.print("Sending alive packet...", WifiDirectManager.DEBUGGER_CHANNEL);
		byte[] res = new byte[]{(byte) PACKET_TYPE.KEEP_ALIVE.toInt()};
		InputStream stream = new ByteArrayInputStream(res);
		send(stream);
	}
	
	public String getClientIpAddress()
	{
		return SocketHandler.getThisDeviceIpAddress();
	}

	public void send(InputStream inputStream)
	{
		try
		{
			OutputStream outputStream = socket.getOutputStream();

			int len;
			while ((len = inputStream.read(buf)) != -1)
			{
				outputStream.write(buf, 0, len);
			}
			outputStream.close();
			inputStream.close();
		}
		catch (Exception e)
		{

		}
	}



}