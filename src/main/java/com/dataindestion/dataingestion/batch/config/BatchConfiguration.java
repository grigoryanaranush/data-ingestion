package com.dataindestion.dataingestion.batch.config;

import com.dataindestion.dataingestion.batch.ConsoleItemWriter;
import com.dataindestion.dataingestion.batch.ZipMultiResourceItemReader;
import com.dataindestion.dataingestion.domain.Person;
import com.dataindestion.dataingestion.domain.PersonInput;
import com.dataindestion.dataingestion.processor.PersonItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    public final JobBuilderFactory jobBuilderFactory;

    public final StepBuilderFactory stepBuilderFactory;

    public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job readCSVFilesJob() {
        return jobBuilderFactory
                .get("readCSVFilesJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory
                .get("step1")
                .<PersonInput, Person>chunk(100)
                .reader(zipMultiResourceItemReader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    @StepScope
    public ZipMultiResourceItemReader<PersonInput> zipMultiResourceItemReader() {
        return new ZipMultiResourceItemReader<>();
    }

    @Bean
    @StepScope
    public ConsoleItemWriter<Person> writer()
    {
        return new ConsoleItemWriter<>();
    }

    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }
}
