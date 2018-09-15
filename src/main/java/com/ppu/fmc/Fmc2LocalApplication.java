package com.ppu.fmc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Fmc2LocalApplication implements CommandLineRunner{

	private static final Logger log = LoggerFactory.getLogger(Fmc2LocalApplication.class);
	public static final String POM_LOCATION = "/META-INF/maven/com.ppu.fmc/fmc2local/pom.properties";

	public static void main(String[] args) {
		new SpringApplicationBuilder(Fmc2LocalApplication.class).web(false).run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("FMC to LOCAL ready");

	}

}
