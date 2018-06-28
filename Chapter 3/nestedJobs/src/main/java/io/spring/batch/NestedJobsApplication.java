package io.spring.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/*
 * 
 * Vrem sa executam un job in interiorul altuia.
 * 
 * vezi sa ai un XAMPP ca sa vezi rezultatul
 * 
 */
@SpringBootApplication
@EnableBatchProcessing
public class NestedJobsApplication {

    public static void main(String[] args) {
        SpringApplication.run(NestedJobsApplication.class, args);
    }
}
