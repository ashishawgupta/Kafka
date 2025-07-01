package com.learnkafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryEventsProducerApplication {

	public static void main(String[] args) {

		try {
			SpringApplication.run(LibraryEventsProducerApplication.class, args);
		}catch (Exception e)	{
			e.printStackTrace();
		}

	}

}
