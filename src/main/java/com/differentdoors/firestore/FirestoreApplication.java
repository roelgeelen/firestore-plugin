package com.differentdoors.firestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class FirestoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirestoreApplication.class, args);
	}

}
