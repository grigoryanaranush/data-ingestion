package com.dataindestion.dataingestion.service;

import com.dataindestion.dataingestion.domain.Person;

import java.util.List;

public interface PersonService {
    void savePeople(List<? extends Person> items);
}
