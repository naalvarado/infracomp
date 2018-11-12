package main;

import uniandes.gload.core.LoadGenerator;
import uniandes.gload.core.Task;

public class GeneratorCifrado {
	
	private LoadGenerator generator;
	
	public GeneratorCifrado(){
		Task work = createTask();
		int numberOfTasks = 20;
		int gapBetweenTasks = 100;
		generator = new LoadGenerator("Client - Server Load Test", numberOfTasks, work, gapBetweenTasks);
	    generator.generate();
	}
	
	private Task createTask(){
		return new ClientServerTaskCifrado();
	}
	
	public static void main(String... args)
	{
		GeneratorCifrado gen = new GeneratorCifrado();
	}

}
