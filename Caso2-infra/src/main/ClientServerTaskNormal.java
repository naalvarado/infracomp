package main;

import uniandes.gload.core.Task;

public class ClientServerTaskNormal extends Task{
	
	@Override
	public void fail() {
		System.out.println("FAIL_TEST");
		
	}

	@Override
	public void success() {
		System.out.println("OK_TEST");
		
	}

	@Override
	public void execute() {
		ClienteNormal client = new ClienteNormal();
	    client.run();
		
	}

}
