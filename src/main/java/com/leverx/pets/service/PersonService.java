package com.leverx.pets.service;

import com.leverx.pets.dto.person.PersonDto;
import com.leverx.pets.model.Person;

import java.util.List;

public interface PersonService {

    Person create(PersonDto personDto);
    List<Person> getAll();
    Person getById(Long id);
    void deleteById(Long id);
    Person update(PersonDto personDto, Long id);

}
