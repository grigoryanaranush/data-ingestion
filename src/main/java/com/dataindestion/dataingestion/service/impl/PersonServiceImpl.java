package com.dataindestion.dataingestion.service.impl;

import com.dataindestion.dataingestion.domain.Person;
import com.dataindestion.dataingestion.repository.PersonRepository;
import com.dataindestion.dataingestion.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    public void savePeople(List<? extends Person> people) {
        personRepository.saveAll(people);
    }
}
