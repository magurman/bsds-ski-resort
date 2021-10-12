package com.bsds.client;

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

		try {
			client.start();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
