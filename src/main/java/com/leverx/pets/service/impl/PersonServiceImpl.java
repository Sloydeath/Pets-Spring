package com.leverx.pets.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leverx.pets.dto.PersonDto;
import com.leverx.pets.exception.custom.PersonNotFoundException;
import com.leverx.pets.model.Person;
import com.leverx.pets.repository.PersonRepository;
import com.leverx.pets.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.leverx.pets.util.ExceptionMessageUtil.PERSON_ERROR_PATTERN;
import static java.lang.String.format;

@Service
@Transactional
@Slf4j
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository, ObjectMapper objectMapper) {
        this.personRepository = personRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Person create(PersonDto personDto) {

        Person person = objectMapper.convertValue(personDto, Person.class);
        personRepository.save(person);
        return person;
    }

    @Override
    public List<Person> getAll() {
        return personRepository.findAll();
    }

    @Override
    public Person getById(Long id) {

        return personRepository
                .findById(id)
                .orElseThrow(() -> {
                    log.debug(format(PERSON_ERROR_PATTERN, id));
                    return new PersonNotFoundException(format(PERSON_ERROR_PATTERN, id));
                });
    }

    @Override
    public void deleteById(Long id) {
        personRepository.deleteById(id);
    }

    @Override
    public Person update(PersonDto personDto, Long id) {

        Person person = personRepository
                .findById(id)
                .orElseThrow(() -> {
                    log.debug(format(PERSON_ERROR_PATTERN, id));
                    return new PersonNotFoundException(format(PERSON_ERROR_PATTERN, id));
                });

        person.setName(personDto.getName());
        personRepository.save(person);
        return person;
    }
}
