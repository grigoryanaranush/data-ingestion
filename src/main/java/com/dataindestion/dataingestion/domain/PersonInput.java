package com.dataindestion.dataingestion.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonInput {
    private Long id;

    private String firstName;

    private String lastName;

    private String date;
}
