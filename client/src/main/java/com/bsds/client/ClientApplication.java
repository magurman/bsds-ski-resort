package com.bsds.client;

import com.bsds.client.http.HttpCounter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class ClientApplication {

	private static final int MAX_NUM_THREADS = 256;
	private static final int MAX_NUM_SKIERS = 50000;
	private static final int MIN_NUM_LIFTS = 5;
	private static final int MAX_NUM_LIFTS = 60;
	private static final int DEFAULT_NUM_LIFTS = 40;
	private static final int DEFAULT_NUM_RUNS = 10;
	private static final int MAX_NUM_RUNS = 20;

	public static void main(String[] args) throws InterruptedException {

		ConfigurableApplicationContext ctx = 
           SpringApplication.run(ClientApplication.class, args);
		
		runClient(ctx.getEnvironment());
	}

	private static void runClient(Environment env) {

		String domain = env.getProperty("server.domain"); 
		int port = Integer.parseInt(env.getProperty("port"));

		int numRuns;
		String numRunsProperty = env.getProperty("client.numRuns");
		if (numRunsProperty == null) {
			numRuns = DEFAULT_NUM_RUNS;
		} else {
			numRuns = Integer.parseInt(numRunsProperty);
			if (numRuns > MAX_NUM_RUNS) {
				throw new IllegalArgumentException("numRuns greater than max number of runs (" + MAX_NUM_RUNS + ")!");
			}
		}

		int numLifts;
		String numLiftsProperty = env.getProperty("client.numLifts");
		if (numLiftsProperty == null) {
			numLifts = DEFAULT_NUM_LIFTS;
		} else {
			numLifts = Integer.parseInt(numLiftsProperty);
			if (numLifts < MIN_NUM_LIFTS) {
				throw new IllegalArgumentException("numLifts less than min number of lifts (" + MIN_NUM_LIFTS + ")!");
			} else if (numLifts > MAX_NUM_LIFTS) {
				throw new IllegalArgumentException("numLifts greater than max number of lifts (" + MAX_NUM_LIFTS + ")!");
			}
		}

		int numSkiers = Integer.parseInt(env.getProperty("client.numSkiers"));
		if (numSkiers > MAX_NUM_SKIERS) {
			throw new IllegalArgumentException("numSkiers greater than max number of skiers (" + MAX_NUM_SKIERS + ")!");
		}

		int numThreads = Integer.parseInt(env.getProperty("client.numThreads"));
		if (numThreads > MAX_NUM_THREADS) {
			throw new IllegalArgumentException("numThreads greater than max number of threads (" + MAX_NUM_THREADS + ")!");
		}

		PhasedSkiersClient client = new PhasedSkiersClient(numThreads, 1,
				numSkiers, numLifts, numRuns, domain, port);

		long begTimeStamp = System.currentTimeMillis();
		try {
			client.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long endTimeStamp = System.currentTimeMillis();

		System.out.println("Wall time: " + (float) (endTimeStamp - begTimeStamp) / 1000 + " seconds");
		System.out.println("Num Succ Requests: " + HttpCounter.getInstance().getSucc());
		System.out.println("Num Failed Requests: " + HttpCounter.getInstance().getFailed());
	}
}
