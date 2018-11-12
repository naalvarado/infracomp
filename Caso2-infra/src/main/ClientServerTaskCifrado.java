package main;

import uniandes.gload.core.Task;

public class ClientServerTaskCifrado extends Task{

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
		ClienteCifrado client = new ClienteCifrado();
	    client.run();
		
	}

}
