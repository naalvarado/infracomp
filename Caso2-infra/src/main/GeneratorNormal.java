package main;

import uniandes.gload.core.LoadGenerator;
import uniandes.gload.core.Task;

public class GeneratorNormal {
	
	private LoadGenerator generator;
	
	public GeneratorNormal(){
		Task work = createTask();
		int numberOfTasks = 20;
		int gapBetweenTasks = 100;
		generator = new LoadGenerator("Client - Server Load Test", numberOfTasks, work, gapBetweenTasks);
	    generator.generate();
	}
	
	private Task createTask(){
		return new ClientServerTaskNormal();
	}
	
	public static void main(String... args)
	{
		GeneratorNormal gen = new GeneratorNormal();
	}

}
