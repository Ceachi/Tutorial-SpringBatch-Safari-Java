/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.spring.batch.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Michael Minella
 * 
 *         https://docs.spring.io/spring-batch/trunk/reference/html/domain.html
 */
@Configuration
@EnableBatchProcessing // bootstrap all the infrastructure
public class JobConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean // functie ce intoarce (creeaza un bean)
	public Step step1() { // executa un tasklet (1 singura interfata cu 1 metoda ce este implementata sa faca 1 singura chestie)
		return stepBuilderFactory.get("step1").tasklet(new Tasklet() { // type of step
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println("Hello World!");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}

	public Step step2() { // executa un tasklet
		return stepBuilderFactory.get("step2").tasklet(new Tasklet() { // type of step
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println("This is step2");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}

	public Step step3() { // executa un tasklet
		return stepBuilderFactory.get("step3").tasklet(new Tasklet() { // type of step
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println("This is step3");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}

	@Bean
	public Job helloWorldJob() {
		return jobBuilderFactory.get("helloWorldJob")
				.start(step1())
				.build();
	}
}
