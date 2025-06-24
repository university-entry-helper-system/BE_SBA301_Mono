package com.example.SBA_M;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication

@EnableJpaRepositories(basePackages = "com.example.SBA_M.repository.commands")
@EnableMongoRepositories(basePackages = "com.example.SBA_M.repository.queries")
@EnableElasticsearchRepositories(basePackages = "com.example.SBA_M.repository.elasticsearch")

public class SbaMApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbaMApplication.class, args);
	}

}
