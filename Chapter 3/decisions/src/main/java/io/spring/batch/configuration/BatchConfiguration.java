/*
 * Copyright 2015 the original author or authors.
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
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * te ajuta sa iei decizii despre urmatoarea stare a tranzitiei
 * 
 * deciziile nu sunt full step! nu au intrari in job repository,
 * se executa ca un State nu ca un step
 * @author Michael Minella
 */
@Configuration
public class BatchConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Step startStep() { // executam al inceputul jobului
		return stepBuilderFactory.get("startStep")
				.tasklet((contribution, chunkContext) -> {
					System.out.println("This is the start tasklet");
					return RepeatStatus.FINISHED;
				}).build();
	}

	@Bean
	public Step evenStep() { // executam dupa Decider, daca el are status even
		return stepBuilderFactory.get("evenStep")
				.tasklet((contribution, chunkContext) -> {
					System.out.println("This is the even tasklet");
					return RepeatStatus.FINISHED;
				}).build();
	}

	@Bean
	public Step oddStep() { // executam dupa Decider, daca el are status odd
		return stepBuilderFactory.get("oddStep")
				.tasklet((contribution, chunkContext) -> {
					System.out.println("This is the odd tasklet");
					return RepeatStatus.FINISHED;
				}).build();
	}

	@Bean
	public JobExecutionDecider decider() {
		return new OddDecider();
	}

	@Bean
	public Job job() {
		return jobBuilderFactory.get("job")
				.start(startStep()) // executam start
				.next(decider()) // apoi decidem
				.from(decider()).on("ODD").to(oddStep()) // daca deciderul returneaza statusul Odd adunci executam oddStep
				.from(decider()).on("EVEN").to(evenStep())
				.from(oddStep()).on("*").to(decider()) // indiferent de statusul dat, executa decider() inca o data si ar sari la "EVEN"
//				.from(decider()).on("ODD").to(oddStep())
//				.from(decider()).on("EVEN").to(evenStep())
				.end()
				.build();
	}

	public static class OddDecider implements JobExecutionDecider {

		private int count = 0; // sa tin cont de cate ori este apelat acest decider

		// metoda are 2 parametrii, joExecution-ul curent si ultimul step executat inaintea acestui Decider
		//FlowExecutionStatus returneaza un obiect ce contine state-ul flowului meu
		@Override
		public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
			count++;

			if(count % 2 == 0) {
				return new FlowExecutionStatus("EVEN");
			}
			else {
				return new FlowExecutionStatus("ODD");
			}
		}
	}
}
