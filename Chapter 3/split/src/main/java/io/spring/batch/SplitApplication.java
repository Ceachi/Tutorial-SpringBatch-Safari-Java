package io.spring.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * Can rulezi aplicatia asta, ai sa observi cum stepurile sunt
 * executate pe threaduri diferite
 */
@SpringBootApplication
@EnableBatchProcessing
public class SplitApplication {

    public static void main(String[] args) {
        SpringApplication.run(SplitApplication.class, args);
    }
}
