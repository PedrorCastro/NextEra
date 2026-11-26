package com.nextera;

import org.springframework.boot.SpringApplication;

public class TestNexteraApplication {

	public static void main(String[] args) {
		SpringApplication.from(NexteraApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
