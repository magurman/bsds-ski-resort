package com.bsds.client;

import java.util.concurrent.CyclicBarrier;

import com.bsds.client.threads.SkierThread;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication {

	public static void main(String[] args) {

		SkierThread skierThread = new SkierThread("localhost", 8080, 0, 100, 1, 1, (float) .1, new CyclicBarrier(10));
		skierThread.run();

		SpringApplication.run(ClientApplication.class, args);
	}

}
