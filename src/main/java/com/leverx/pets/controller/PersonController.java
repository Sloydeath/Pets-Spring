package com.leverx.pets.controller;

import com.leverx.pets.dto.PersonDto;
import com.leverx.pets.model.Person;
import com.leverx.pets.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static com.leverx.pets.util.ControllerConstantUtil.PERSON_CONTROLLER_ENDPOINT;
import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(PERSON_CONTROLLER_ENDPOINT)
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Person>> getAllPeople() {

        List<Person> people = personService.getAll();
        return !people.isEmpty()
                ? new ResponseEntity<>(people, OK)
                : new ResponseEntity<>(NOT_FOUND);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {

        Person person = personService.getById(id);
        return nonNull(person)
                ? new ResponseEntity<>(person, OK)
                : new ResponseEntity<>(NOT_FOUND);
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> savePerson(@Valid @RequestBody PersonDto personDto) {

       Person person = personService.create(personDto);
       return nonNull(person)
               ? new ResponseEntity<>(person, OK)
               : new ResponseEntity<>(NOT_FOUND);
    }

    @PutMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> updatePerson(@Valid @RequestBody PersonDto personDto,
                                               @PathVariable Long id) {

        Person person = personService.update(personDto, id);
        return nonNull(person)
                ? new ResponseEntity<>(person, OK)
                : new ResponseEntity<>(NOT_FOUND);
    }

    @DeleteMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deletePerson(@PathVariable Long id) {
        personService.deleteById(id);
        return new ResponseEntity<>(OK);
    }
}
