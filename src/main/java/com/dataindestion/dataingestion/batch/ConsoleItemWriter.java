package com.dataindestion.dataingestion.batch;

import com.dataindestion.dataingestion.domain.Person;
import com.dataindestion.dataingestion.service.PersonService;
import com.dataindestion.dataingestion.service.impl.PersonServiceImpl;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class ConsoleItemWriter<T extends Person> implements ItemWriter<Person> {

    @Autowired
    private PersonService personService;

    public void write(List<? extends Person> people) {
        personService.savePeople(people);
    }

    @Bean
    public PersonService personService() {
        return new PersonServiceImpl();
    }
}