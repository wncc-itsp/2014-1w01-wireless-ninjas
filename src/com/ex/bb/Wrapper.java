package com.ex.bb;

public class Wrapper {
	public String host;
	public int port;
	public OnTaskCompleted listener;
	public Wrapper(OnTaskCompleted listener,String host,int port){
		this.listener=listener;
		this.host=host;
		this.port=port;
	}

}
