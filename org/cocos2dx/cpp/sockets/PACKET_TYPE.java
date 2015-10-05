package org.cocos2dx.cpp.sockets;

public enum PACKET_TYPE {

	DEFAULT,
	ACCUSE,
	STRING,
	INT,
	BOOL,
	LONG,
	FILE,
	DOUBLE,
	FLOAT,
	CHAR,
	IP,
	BYTE,
	BYTE_ARRAY,
	KEEP_ALIVE;
	
	public int toInt()
	{
		PACKET_TYPE[] all = PACKET_TYPE.class.getEnumConstants();
		for(int i = 0; i < all.length; i++)
		{
			if(all[i] == this)
			{
				return i;
			}
		}
		return -1;
	}
	
	public static PACKET_TYPE get(int i)
	{
		PACKET_TYPE[] types = PACKET_TYPE.class.getEnumConstants();
		return i < 0 || i >= types.length ? PACKET_TYPE.DEFAULT : types[i];
	}
	
	public static void main(String[] args)
	{
		System.out.println(PACKET_TYPE.STRING.toInt());
	}
}
