package com.bsds.client;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.bsds.client.http.HttpCounter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties.RSocket.Client;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@SpringBootApplication
public class ClientApplication {

	private static final int MAX_NUM_THREADS = 256;
	private static final int MAX_NUM_SKIERS = 50000;
	private static final int MIN_NUM_LIFTS = 5;
	private static final int MAX_NUM_LIFTS = 60;
	private static final int DEFAULT_NUM_LIFTS = 40;
	private static final int DEFAULT_NUM_RUNS = 10;
	private static final int MAX_NUM_RUNS = 20;

	private static ConfigurableApplicationContext CTX;

	private static final Logger logger = LogManager.getLogger(TimedSkiersClient.class);


	public static void main(String[] args) throws InterruptedException {

		ConfigurableApplicationContext ctx = 
           SpringApplication.run(ClientApplication.class, args);
		   ClientApplication.CTX = ctx;
		runClient();
	}

	public static String getEnvProperty(String properyName) {
		return ClientApplication.CTX.getEnvironment().getProperty(properyName);
	}

	private static void runClient() {

		Environment env = ClientApplication.CTX.getEnvironment();

		String domain = getEnvProperty("server.domain");
		int port = Integer.parseInt(getEnvProperty("port"));

		int numRuns;
		String numRunsProperty = getEnvProperty("client.numRuns");
		if (numRunsProperty == null) {
			numRuns = DEFAULT_NUM_RUNS;
		} else {
			numRuns = Integer.parseInt(numRunsProperty);
			if (numRuns > MAX_NUM_RUNS) {
				throw new IllegalArgumentException("numRuns greater than max number of runs (" + MAX_NUM_RUNS + ")!");
			}
		}

		int numLifts;
		String numLiftsProperty = getEnvProperty("client.numLifts");
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

		int numSkiers = Integer.parseInt(getEnvProperty("client.numSkiers"));
		if (numSkiers > MAX_NUM_SKIERS) {
			throw new IllegalArgumentException("numSkiers greater than max number of skiers (" + MAX_NUM_SKIERS + ")!");
		}

		int numThreads = Integer.parseInt(getEnvProperty("client.numThreads"));
		if (numThreads > MAX_NUM_THREADS) {
			throw new IllegalArgumentException("numThreads greater than max number of threads (" + MAX_NUM_THREADS + ")!");
		}

		String clientType = getEnvProperty("client.type");

		SkiersClient client;
		if (clientType.equals("phased")) {
			client = new PhasedSkiersClient(numThreads, 1,
				numSkiers, numLifts, numRuns, domain, port);
		} else if (clientType.equals("timed")) {
			client = new TimedSkiersClient(numThreads, 1, numSkiers, numLifts, domain, port);
		} else {
			client = null;
		}

		long begTimeStamp = System.currentTimeMillis();
		try {
			client.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			logger.error("Incorrect client type specified in environment context");
			return;
		}
		
		long endTimeStamp = System.currentTimeMillis();

		System.out.println("Wall time: " + (float) (endTimeStamp - begTimeStamp) / 1000 + " seconds");
		System.out.println("Num Succ Requests: " + HttpCounter.getNumSuccessful());
		System.out.println("Num Failed Requests: " + HttpCounter.getNumFailed());

		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("getLatency.csv"));
			outputStream.writeObject(LatencyHistogram.histogramGet);
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}
}
