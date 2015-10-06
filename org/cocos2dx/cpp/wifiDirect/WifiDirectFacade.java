package org.cocos2dx.cpp.wifiDirect;

import java.io.File;
import java.util.List;

import org.cocos2dx.cpp.jniFacade.JniCppFacade;
import org.cocos2dx.cpp.sockets.CallBackMethod;

import android.app.Activity;

public class WifiDirectFacade {

	private WifiDirectManager _manager;
	private CallBackMethod cmOnGettingsPeers = new CallBackMethod() {
		@Override
		public void Do(Object... vars)
		{
			onGettingPeers(_manager.getDeviceList());
		}
	};
	private CallBackMethod cmOnBeingConnect = new CallBackMethod() {
		@Override
		public void Do(Object... vars)
		{
			onBeingConnected();
		}
	};
	private CallBackMethod cmOnReceiveString = new CallBackMethod() {
		@Override
		public void Do(Object... vars)
		{
			onReceiving((String) vars[0]);
		}
	};
	private CallBackMethod cmOnReceiveInt = new CallBackMethod() {
		@Override
		public void Do(Object... vars)
		{
			onReceiving((int) vars[0]);
		}
	};
	private CallBackMethod cmOnReceiveBool = new CallBackMethod() {
		@Override
		public void Do(Object... vars)
		{
			onReceiving((boolean) vars[0]);
		}
	};
	private CallBackMethod cmOnReceiveFloat = new CallBackMethod() {
		@Override
		public void Do(Object... vars)
		{
			onReceiving((float) vars[0]);
		}
	};
	private CallBackMethod cmOnReceiveDouble = new CallBackMethod() {
		@Override
		public void Do(Object... vars)
		{
			onReceiving((double) vars[0]);
		}
	};
	private CallBackMethod cmOnReceiveByte = new CallBackMethod() {
		@Override
		public void Do(Object... vars)
		{
			onReceiving((byte) vars[0]);
		}
	};
	private CallBackMethod cmOnReceiveLong = new CallBackMethod() {
		@Override
		public void Do(Object... vars)
		{
			onReceiving((long) vars[0]);
		}
	};
	private CallBackMethod cmOnReceiveFile = new CallBackMethod() {
		@Override
		public void Do(Object... vars)
		{
			onReceiving((File) vars[0]);
		}
	};
	private CallBackMethod cmOnReceiveByteArray = new CallBackMethod() {
		@Override
		public void Do(Object... vars)
		{
			onReceiving((byte[]) vars[0]);
		}
	};
	private CallBackMethod cmOnReceiveChar = new CallBackMethod() {
		@Override
		public void Do(Object... vars)
		{
			onReceiving((char) vars[0]);
		}
	};

	public WifiDirectFacade(Activity activity)
	{
		_manager = new WifiDirectManager(activity);
		WifiDirectManager.registerCallBackReceiver(cmOnReceiveString, cmOnReceiveInt,
				cmOnReceiveBool, cmOnReceiveFloat, cmOnReceiveDouble,
				cmOnReceiveByte, cmOnReceiveLong, cmOnReceiveFile,
				cmOnReceiveByteArray, cmOnReceiveChar);
	}

	public void onGettingPeers(List<String> peers)
	{
		JniCppFacade.onGettingPeers(peers);
	}

	public void onBeingConnected()
	{
		JniCppFacade.onBeingConnected();
	}

	public void onReceiving(String s)
	{
		JniCppFacade.onReceiving(s);
	}

	public void onReceiving(int i)
	{
		JniCppFacade.onReceiving(i);
	}

	public void onReceiving(boolean b)
	{
		JniCppFacade.onReceiving(b);
	}

	public void onReceiving(long l)
	{
		JniCppFacade.onReceiving(l);
	}

	public void onReceiving(File f)
	{
		JniCppFacade.onReceiving(f);
	}

	public void onReceiving(double d)
	{
		JniCppFacade.onReceiving(d);
	}

	public void onReceiving(float f)
	{
		JniCppFacade.onReceiving(f);
	}

	public void onReceiving(char c)
	{
		JniCppFacade.onReceiving(c);
	}

	public void onReceiving(byte b)
	{
		JniCppFacade.onReceiving(b);
	}

	public void onReceiving(byte[] bytes)
	{
		JniCppFacade.onReceiving(bytes);
	}

	public void discoverPeers()
	{
		_manager.launchServicePeersDiscovering(cmOnGettingsPeers);
	}

	public void connectTo(String deviceName)
	{
		_manager.connectToPeer(deviceName, cmOnBeingConnect);
	}

	public void send(String s)
	{
		_manager.send(s);
	}

	public void send(int i)
	{
		_manager.send(i);
	}

	public void send(boolean b)
	{
		_manager.send(b);
	}

	public void send(long l)
	{
		_manager.send(l);
	}

	public void send(File f)
	{
		_manager.send(f);
	}

	public void send(double d)
	{
		_manager.send(d);
	}

	public void send(float f)
	{
		_manager.send(f);
	}

	public void send(char c)
	{
		_manager.send(c);
	}

	public void send(byte b)
	{
		_manager.send(b);
	}

	public void send(byte[] bytes)
	{
		_manager.send(bytes);
	}

}
