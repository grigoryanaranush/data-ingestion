package com.dataindestion.dataingestion.processor;

import com.dataindestion.dataingestion.domain.Person;
import com.dataindestion.dataingestion.domain.PersonInput;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;

@Service
public class PersonItemProcessor implements ItemProcessor<PersonInput, Person> {
    public Person process(PersonInput person) {
        return Person.builder()
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .date(formatDate(person.getDate()))
                .build();
    }

    private LocalDate formatDate(String date) {
        try {
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .appendOptional(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    .appendOptional(DateTimeFormatter.ofPattern("MMMM d'st', yyyy"))
                    .appendOptional(DateTimeFormatter.ofPattern("MMMM d'nd', yyyy"))
                    .appendOptional(DateTimeFormatter.ofPattern("MMMM d'rd', yyyy"))
                    .appendOptional(DateTimeFormatter.ofPattern("MMMM d'th', yyyy"))
                    .toFormatter();

            return LocalDate.parse(date, formatter);
        }catch (DateTimeParseException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }
}
