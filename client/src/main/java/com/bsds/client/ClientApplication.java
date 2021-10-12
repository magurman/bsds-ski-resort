package com.bsds.client;

import com.bsds.client.http.HttpCounter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class ClientApplication {

	public static void main(String[] args) throws InterruptedException {

		ConfigurableApplicationContext ctx = 
           SpringApplication.run(ClientApplication.class, args);
		
		runClient(ctx.getEnvironment());
	}

	private static void runClient(Environment env) {

		String domain = env.getProperty("server.domain"); 
		int port = Integer.parseInt(env.getProperty("port"));

		int numRuns = Integer.parseInt(env.getProperty("client.numRuns"));
		int numLifts = Integer.parseInt(env.getProperty("client.numLifts"));
		int numSkiers = Integer.parseInt(env.getProperty("client.numSkiers"));
		int numThreads = Integer.parseInt(env.getProperty("client.numThreads"));

		PhasedSkiersClient client = new PhasedSkiersClient(numThreads, 1,
				numSkiers, numLifts, numRuns, domain, port);

		long begTimeStamp = System.currentTimeMillis();
		try {
			client.start();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long endTimeStamp = System.currentTimeMillis();
		
		System.out.println("Wall time: " + (float) (endTimeStamp - begTimeStamp) / 1000 + " seconds");
		System.out.println("Num Succ Requests: " + HttpCounter.getInstance().getSucc());
		System.out.println("Num Failed Requests: " + HttpCounter.getInstance().getFailed());
	}
}
