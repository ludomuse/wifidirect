package org.cocos2dx.cpp.sockets;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.cocos2dx.cpp.DebugManager;
import org.cocos2dx.cpp.wifiDirect.WifiDirectManager;

import android.os.AsyncTask;
import android.os.Environment;

class Communication implements Runnable {
	private Socket client;
	private SocketHandler master;
	public static boolean isWaitingForAccuse = false;

	public Communication(Socket client, SocketHandler master)
	{
		super();
		this.client = client;
		this.master = master;
	}

	private char readChar(InputStream is)
	{
		try
		{
			return (char) is.read();
		}
		catch (IOException e)
		{
			DebugManager.print("error reading char in inputstream",
					WifiDirectManager.DEBUGGER_CHANNEL);
			return '#';
		}
	}

	// convert InputStream to String
	private String getStringFromInputStream(InputStream is)
	{

		String res = "";
		char tps;
		while ((tps = readChar(is)) != '\0')
		{
			res += tps;
		}
		return res;
	}

	// convert is to float
	private Float getFloatFromInputStream(InputStream is)
	{
		byte[] bytes = new byte[] { (byte) read(is), (byte) read(is),
				(byte) read(is), (byte) read(is) };
		return ByteBuffer.wrap(bytes).getFloat();
	}

	// convert is to double
	private Double getDoubleFromInputStream(InputStream is)
	{
		byte[] bytes = new byte[] { (byte) read(is), (byte) read(is),
				(byte) read(is), (byte) read(is), (byte) read(is),
				(byte) read(is), (byte) read(is), (byte) read(is) };
		return ByteBuffer.wrap(bytes).getDouble();
	}

	// convert is to long
	private Long getLongFromInputStream(InputStream is)
	{
		byte[] bytes = new byte[Long.SIZE];
		for (int i = 0; i < Long.SIZE; i++)
		{
			bytes[i] = (byte) read(is);

		}
		return ByteBuffer.wrap(bytes).getLong();
	}

	// convert is to bytes
	private byte[] getBytesFromInputStream(InputStream is)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int tps;
		while ((tps = read(is)) != -1)
		{
			bos.write(tps);
		}

		return bos.toByteArray();
	}

	// convert is to bytes
	private File getFileFromInputStream(InputStream is)
	{
		File f = new File("/sdcard/Screenshots/tps");
		try
		{
			f.createNewFile();
		}
		catch (IOException e1)
		{
			DebugManager.print("error while creating new file", WifiDirectManager.DEBUGGER_CHANNEL);
		}
		
		FileOutputStream fos;
		try
		{
			fos = new FileOutputStream(f);
			int tps;
			while ((tps = read(is)) != -1)
			{
				fos.write(tps);
			}
			fos.close();
			return f;
		}
		catch (Exception e)
		{
			DebugManager.print("Error while reading file. Default directory is: " + new File(".").getAbsolutePath(),
					WifiDirectManager.DEBUGGER_CHANNEL);
			return null;
		}
	}

	private InputStream getClientData(Socket client)
	{
		InputStream is = null;
		try
		{
			is = client.getInputStream();
		}
		catch (IOException e)
		{
			DebugManager.print("error while getting client data",
					WifiDirectManager.DEBUGGER_CHANNEL);
		}
		return is;
	}

	private void closeInputStream(InputStream is)
	{
		try
		{
			if (is == null)
			{
				DebugManager.print("Inputstream is null",
						WifiDirectManager.DEBUGGER_CHANNEL);
			}
			else
			{
				is.close();
			}
		}
		catch (IOException e)
		{
			DebugManager.print("error while closing inputstream",
					WifiDirectManager.DEBUGGER_CHANNEL);
		}
	}

	private void receiveIP(InputStream stream)
	{
		String clientIP = getStringFromInputStream(stream);

		DebugManager.print("client address is : " + clientIP,
				WifiDirectManager.DEBUGGER_CHANNEL);

		master.setRemoteHost(clientIP.split("!")[0],
				Integer.parseInt(clientIP.split("!")[1]));

		if (onReceiveIPCallBack == null)
		{
			master.sendAccuse();
		}
		else
		{
			onReceiveIPCallBack.Do();
			onReceiveIPCallBack = null;
		}

	}

	private void receiveString(InputStream stream)
	{
		String res = getStringFromInputStream(stream);
		DebugManager.print("we receive string : " + res,
				WifiDirectManager.DEBUGGER_CHANNEL);
		if (onReceiveString != null)
			onReceiveString.Do(res);
		master.sendAccuse();
	}

	private void receiveAccuse()
	{
		DebugManager.print("Accuse received !",
				WifiDirectManager.DEBUGGER_CHANNEL);
		if (onReceiveAccuseCallBack != null)
		{
			onReceiveAccuseCallBack.Do();
			onReceiveAccuseCallBack = null;
		}
		isWaitingForAccuse = false;
	}

	private void receiveBool(InputStream is)
	{
		boolean res = read(is) == 0 ? false : true;
		DebugManager.print("we received bool: " + res,
				WifiDirectManager.DEBUGGER_CHANNEL);
		if (onReceiveBool != null)
			onReceiveBool.Do(res);
		master.sendAccuse();
	}

	private void receiveInt(InputStream is)
	{
		int res = read(is);
		DebugManager.print("we received int: " + res,
				WifiDirectManager.DEBUGGER_CHANNEL);
		if (onReceiveInt != null)
			onReceiveInt.Do(res);
		master.sendAccuse();
	}

	private void receiveByte(InputStream is)
	{
		byte res = (byte) read(is);
		DebugManager.print("we received byte: " + res,
				WifiDirectManager.DEBUGGER_CHANNEL);
		if (onReceiveByte != null)
			onReceiveByte.Do(res);
		master.sendAccuse();
	}

	private void receiveChar(InputStream is)
	{
		char res = (char) read(is);
		DebugManager.print("we received char: " + res,
				WifiDirectManager.DEBUGGER_CHANNEL);
		if (onReceiveChar != null)
			onReceiveChar.Do(res);
		master.sendAccuse();
	}

	private void receiveFloat(InputStream is)
	{
		float res = getFloatFromInputStream(is);
		DebugManager.print("we received float: " + res,
				WifiDirectManager.DEBUGGER_CHANNEL);
		if (onReceiveFloat != null)
			onReceiveFloat.Do(res);
		master.sendAccuse();
	}

	private void receiveDouble(InputStream is)
	{
		double res = getDoubleFromInputStream(is);
		DebugManager.print("we received double: " + res,
				WifiDirectManager.DEBUGGER_CHANNEL);
		if (onReceiveDouble != null)
			onReceiveDouble.Do(res);
		master.sendAccuse();
	}

	private void receiveLong(InputStream is)
	{
		long res = getLongFromInputStream(is);
		DebugManager.print("we received long: " + res,
				WifiDirectManager.DEBUGGER_CHANNEL);
		if (onReceiveLong != null)
			onReceiveLong.Do(res);
		master.sendAccuse();
	}

	private void receiveBytes(InputStream is)
	{
		byte[] res = getBytesFromInputStream(is);
		/* debug */
		String bytesStr = "";
		for (int i = 0; i < res.length; i++)
		{
			bytesStr += res[i] + "-";
		}

		DebugManager.print("we received bytes: " + bytesStr,
				WifiDirectManager.DEBUGGER_CHANNEL);
		/* end debug */
		if (onReceiveByteArray != null)
			onReceiveByteArray.Do(res);
		master.sendAccuse();
	}

	private void receiveFile(InputStream is)
	{
		File res = getFileFromInputStream(is);
		/* debug */
		DebugManager.print("new file received " + res,
				WifiDirectManager.DEBUGGER_CHANNEL);
		/* end debug */
		if (onReceiveFile != null)
			onReceiveFile.Do(res);
		master.sendAccuse();
	}

	private int read(InputStream is)
	{
		if (is != null)
		{
			try
			{
				return is.read();
			}
			catch (IOException e)
			{
				DebugManager.print(
						"error when reading inputstream "
								+ e.getLocalizedMessage(),
						WifiDirectManager.DEBUGGER_CHANNEL);
				return -1;
			}
		}
		return -1;
	}

	private PACKET_TYPE getPacketType(InputStream is)
	{
		int value = -1;
		int available = -1;
		try
		{
			available = is.available();
			value = read(is);
			return PACKET_TYPE.get(value);
		}
		catch (IOException e)
		{
			DebugManager.print("error while getting packet type " + available,
					WifiDirectManager.DEBUGGER_CHANNEL);
			return PACKET_TYPE.DEFAULT;
		}
		catch (Exception e)
		{
			DebugManager.print("unknow exception : " + value,
					WifiDirectManager.DEBUGGER_CHANNEL);
			return PACKET_TYPE.DEFAULT;
		}
	}

	@Override
	public void run()
	{
		InputStream is = getClientData(client);

		PACKET_TYPE type = getPacketType(is);

		switch (type)
		{
			case KEEP_ALIVE:
				DebugManager.print("Client is alive !",
						WifiDirectManager.DEBUGGER_CHANNEL);
				break;
			case DEFAULT:
				break;
			case BOOL:
				receiveBool(is);
				break;
			case BYTE:
				receiveByte(is);
				break;
			case BYTE_ARRAY:
				receiveBytes(is);
				break;
			case CHAR:
				receiveChar(is);
				break;
			case DOUBLE:
				receiveDouble(is);
				break;
			case FILE:
				receiveFile(is);
				break;
			case FLOAT:
				receiveFloat(is);
				break;
			case INT:
				receiveInt(is);
				break;
			case IP:
				receiveIP(is);
				break;
			case LONG:
				receiveLong(is);
				break;
			case STRING:
				receiveString(is);
				break;
			case ACCUSE:
				receiveAccuse();
				break;
		// default:
		// break;
		}

		if (type != PACKET_TYPE.DEFAULT && isWaitingForAccuse)
		{
			DebugManager.print("resending packet...",
					WifiDirectManager.DEBUGGER_CHANNEL);
			master.resend();
		}

		closeInputStream(is);

		DebugManager.print("Request is answered",
				WifiDirectManager.DEBUGGER_CHANNEL);
	}

	private static CallBackMethod onReceiveIPCallBack = null;

	public static void setOnReceiveIPCallBack(CallBackMethod cm)
	{
		onReceiveIPCallBack = cm;
	}

	private static CallBackMethod onReceiveAccuseCallBack = null;

	public static void setOnReceiveAccuseCallBack(CallBackMethod cm)
	{
		onReceiveAccuseCallBack = cm;
	}

	private static CallBackMethod onReceiveString = null;
	private static CallBackMethod onReceiveInt = null;
	private static CallBackMethod onReceiveBool = null;
	private static CallBackMethod onReceiveFloat = null;
	private static CallBackMethod onReceiveDouble = null;
	private static CallBackMethod onReceiveByte = null;
	private static CallBackMethod onReceiveLong = null;
	private static CallBackMethod onReceiveFile = null;
	private static CallBackMethod onReceiveByteArray = null;
	private static CallBackMethod onReceiveChar = null;

	public static void registerCallBackReceiver(CallBackMethod onReceiveString,
			CallBackMethod onReceiveInt, CallBackMethod onReceiveBool,
			CallBackMethod onReceiveFloat, CallBackMethod onReceiveDouble,
			CallBackMethod onReceiveByte, CallBackMethod onReceiveLong,
			CallBackMethod onReceiveFile, CallBackMethod onReceiveByteArray,
			CallBackMethod onReceiveChar)
	{
		Communication.onReceiveString = onReceiveString;
		Communication.onReceiveInt = onReceiveInt;
		Communication.onReceiveBool = onReceiveBool;
		Communication.onReceiveFloat = onReceiveFloat;
		Communication.onReceiveDouble = onReceiveDouble;
		Communication.onReceiveByte = onReceiveByte;
		Communication.onReceiveLong = onReceiveLong;
		Communication.onReceiveFile = onReceiveFile;
		Communication.onReceiveByteArray = onReceiveByteArray;
		Communication.onReceiveChar = onReceiveChar;
	}

}

public class ServerSocketHandler extends AsyncTask<Void, String, Void> {

	private int port;
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

	public void waitForAccuse()
	{
		Communication.isWaitingForAccuse = true;
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

	@Override
	protected Void doInBackground(Void... params)
	{

		openServerSocket();

		while (isRunning())
		{
			DebugManager.print("server is waiting for client...",
					WifiDirectManager.DEBUGGER_CHANNEL);
			Socket client = waitForClient();
			DebugManager.print("new client request...",
					WifiDirectManager.DEBUGGER_CHANNEL);
			new Thread(new Communication(client, master)).start();
		}

		closeServerSocket();

		return null;
	}

	public void setOnReceiveIPCallBack(CallBackMethod cm)
	{
		Communication.setOnReceiveIPCallBack(cm);
	}

	public void setOnReceiveAccuseCallBack(CallBackMethod cm)
	{
		Communication.setOnReceiveAccuseCallBack(cm);
	}

	public static void registerCallBackReceiver(CallBackMethod onReceiveString,
			CallBackMethod onReceiveInt, CallBackMethod onReceiveBool,
			CallBackMethod onReceiveFloat, CallBackMethod onReceiveDouble,
			CallBackMethod onReceiveByte, CallBackMethod onReceiveLong,
			CallBackMethod onReceiveFile, CallBackMethod onReceiveByteArray,
			CallBackMethod onReceiveChar)
	{
		Communication
				.registerCallBackReceiver(onReceiveString, onReceiveInt,
						onReceiveBool, onReceiveFloat, onReceiveDouble,
						onReceiveByte, onReceiveLong, onReceiveFile,
						onReceiveByteArray, onReceiveChar);
	}

	public void stopHandlers()
	{
		// TODO Auto-generated method stub

	}

}