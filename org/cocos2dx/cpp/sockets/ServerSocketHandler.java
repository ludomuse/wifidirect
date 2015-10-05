package org.cocos2dx.cpp.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.cocos2dx.cpp.DebugManager;
import org.cocos2dx.cpp.wifiDirect.WifiDirectManager;

import android.os.AsyncTask;
import android.util.Log;

public class ServerSocketHandler extends AsyncTask<Void, String, Void> {

	private int port;
	private String clientIP;
	private SocketHandler master;

	public ServerSocketHandler(int port, SocketHandler master)
	{
		this.port = port;
		this.master = master;
	}

	private boolean run = true;

	public void stop()
	{
		run = false;
	}

	public void listen()
	{
		execute();
	}

	private void receiveIP(InputStream stream)
	{
		clientIP = getStringFromInputStream(stream);
		
		DebugManager.print("client address is : " + clientIP,
				WifiDirectManager.DEBUGGER_CHANNEL);
		
		master.connect(clientIP.split("!")[0],
				Integer.parseInt(clientIP.split("!")[1]), false);

//		master.sendAccuse();
	}

	private void receiveString(InputStream stream)
	{
		String res = getStringFromInputStream(stream);
		DebugManager.print("we receive string : " + res,
				WifiDirectManager.DEBUGGER_CHANNEL);
		master.sendAccuse();
	}

	private boolean waitForAccuse = false;

	public void waitForAccuse()
	{
		waitForAccuse = true;
	}

	public String getServerIpAddress()
	{
		return SocketHandler.getThisDeviceIpAddress();
	}

	private void openServerSocket()
	{
		/**
		 * Create a server socket and wait for client connections. This call
		 * blocks until a connection is accepted from a client
		 */

		try
		{
			serverSocket = new ServerSocket(port);
		}
		catch (Exception e)
		{
			try
			{
				serverSocket = new ServerSocket(0);
			}
			catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		String debugText = "launching server on " + getServerIpAddress() + ":";
		if (port != serverSocket.getLocalPort())
		{
			DebugManager.print("Port " + port + " was already in use. "
					+ debugText + serverSocket.getLocalPort(),
					WifiDirectManager.DEBUGGER_CHANNEL);
			port = serverSocket.getLocalPort();
		}
		else
		{
			DebugManager.print(debugText + port,
					WifiDirectManager.DEBUGGER_CHANNEL);
		}

	}

	private Socket waitForClient()
	{
		Socket client = null;

		try
		{
			client = serverSocket.accept();
		}
		catch (IOException e1)
		{
			DebugManager.print("server.accept() failed",
					WifiDirectManager.DEBUGGER_CHANNEL);
		}
		DebugManager.print("new client connected",
				WifiDirectManager.DEBUGGER_CHANNEL);

		run = true;

		return client;
	}

	private void closeServerSocket()
	{
		try
		{
			serverSocket.close();
		}
		catch (IOException e)
		{
			DebugManager.print("server.close() failed",
					WifiDirectManager.DEBUGGER_CHANNEL);
		}
		DebugManager.print("server is closed",
				WifiDirectManager.DEBUGGER_CHANNEL);
	}

	public boolean isRunning()
	{
		return run;
	}

	ServerSocket serverSocket;

	private InputStream getClientData(Socket client)
	{
		InputStream is = null;
		try
		{
			is = client.getInputStream();
		}
		catch (IOException e)
		{
			DebugManager.print("error while getting client data", WifiDirectManager.DEBUGGER_CHANNEL);
		}
		return is;
	}
	
	private void closeInputStream(InputStream is)
	{
		try
		{
			if(is == null)
			{
				DebugManager.print("Inputstream is null", WifiDirectManager.DEBUGGER_CHANNEL);
			}
			else
			{
				is.close();
			}
		}
		catch (IOException e)
		{
			DebugManager.print("error while closing inputstream", WifiDirectManager.DEBUGGER_CHANNEL);
		}
	}
	
	private PACKET_TYPE getPacketType(InputStream is)
	{
		int value = -1;
		try
		{
			value = is.read();
			return PACKET_TYPE.get(value);
		}
		catch (IOException e)
		{
			DebugManager.print("error while getting packet type", WifiDirectManager.DEBUGGER_CHANNEL);
			return PACKET_TYPE.DEFAULT;
		}
		catch(Exception e)
		{
			DebugManager.print("unknow exception : " + value, WifiDirectManager.DEBUGGER_CHANNEL);
			return PACKET_TYPE.DEFAULT;
		}
	}
	
	@Override
	protected Void doInBackground(Void... params)
	{

		openServerSocket();
		Socket client = waitForClient();

		InputStream is = getClientData(client);
		
		while (isRunning() && client != null)
		{

		
			
			
			PACKET_TYPE type = getPacketType(is);
			//DebugManager.print(type + "" , WifiDirectManager.DEBUGGER_CHANNEL);
			
			//if(type != PACKET_TYPE.DEFAULT)
			//{
				DebugManager.print("server is listenning...",
						WifiDirectManager.DEBUGGER_CHANNEL);
			//}
			
			switch (type)
			{
				case KEEP_ALIVE:
					DebugManager.print("Client is alive !", WifiDirectManager.DEBUGGER_CHANNEL);
					break;
				case DEFAULT:
					client = waitForClient();
					is = getClientData(client);
					break;
//				case BOOL:
//					break;
//				case BYTE:
//					break;
//				case BYTE_ARRAY:
//					break;
//				case CHAR:
//					break;
//				case DOUBLE:
//					break;
//				case FILE:
//					break;
//				case FLOAT:
//					break;
//				case INT:
//					break;
				case IP:
					receiveIP(is);
					break;
//				case LONG:
//					break;
//				case STRING:
//					receiveString(is);
//					break;
				case ACCUSE:
					DebugManager.print("Accuse received !", WifiDirectManager.DEBUGGER_CHANNEL);
					waitForAccuse = false;
					//master.wasConnectedAtLeastOneTime = true;
					break;
//				default:
//					break;
			}
			
			
			
			if (type != PACKET_TYPE.DEFAULT && waitForAccuse)
			{
				DebugManager.print("resending packet...", WifiDirectManager.DEBUGGER_CHANNEL);
				master.resend();
			}


		}

		closeInputStream(is);
		
		closeServerSocket();

		return null;
	}

	private char readChar(InputStream is)
	{
		try
		{
			return (char) is.read();
		}
		catch (IOException e)
		{
			DebugManager.print("error reading char in inputstream", WifiDirectManager.DEBUGGER_CHANNEL);
			return '#';
		}
	}
	
	// convert InputStream to String
	private String getStringFromInputStream(InputStream is)
	{

		String res = "";
		char tps;
		while((tps = readChar(is)) != '\0')
		{
			res += tps;
		}
		return res;
	}

}