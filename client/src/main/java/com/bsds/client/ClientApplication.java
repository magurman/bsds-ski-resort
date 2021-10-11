package com.bsds.client;

import java.util.concurrent.CyclicBarrier;


import com.bsds.client.threads.SkierThread;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class ClientApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {


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

		SkierThread skierThread = new SkierThread(domain, port, 0, 100, 1, 1, (float) .1, new CyclicBarrier(10));

		skierThread.run();
	}

}
