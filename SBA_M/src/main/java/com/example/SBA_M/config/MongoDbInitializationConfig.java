package com.example.SBA_M.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoDbInitializationConfig {

    private static final Logger logger = LoggerFactory.getLogger(MongoDbInitializationConfig.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${spring.data.mongodb.drop-on-startup:false}")
    private boolean dropOnStartup;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeDatabase() {
        if (dropOnStartup) {
            try {
                logger.info("Dropping MongoDB database: {}", databaseName);
                mongoTemplate.getDb().drop();
                logger.info("Successfully dropped MongoDB database: {}", databaseName);

                logger.info("MongoDB database will be recreated automatically on first data insertion");

            } catch (Exception e) {
                logger.error("Error occurred while dropping MongoDB database: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to drop MongoDB database", e);
            }
        } else {
            logger.info("MongoDB database drop on startup is disabled");
        }
    }
}