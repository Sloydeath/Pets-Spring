package com.leverx.pets.controller;

import com.leverx.pets.dto.PetDto;
import com.leverx.pets.dto.UpdatePetDto;
import com.leverx.pets.model.pet.Pet;
import com.leverx.pets.service.PetService;
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

import static com.leverx.pets.util.ControllerConstantUtil.PET_CONTROLLER_ENDPOINT;
import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(PET_CONTROLLER_ENDPOINT)
public class PetController {

    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Pet>> getAllPets() {

        List<Pet> pets = petService.getAll();
        return !pets.isEmpty()
                ? new ResponseEntity<>(pets, OK)
                : new ResponseEntity<>(NOT_FOUND);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Pet> getPetById(@PathVariable Long id) {

        Pet pet = petService.getById(id);
        return nonNull(pet)
                ? new ResponseEntity<>(pet, OK)
                : new ResponseEntity<>(NOT_FOUND);
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Pet> savePet(@Valid @RequestBody PetDto petDto) {

        Pet pet = petService.create(petDto);
        return nonNull(pet)
                ? new ResponseEntity<>(pet, OK)
                : new ResponseEntity<>(NOT_FOUND);
    }

    @PutMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Pet> updatePet(@Valid @RequestBody UpdatePetDto petDto,
                                               @PathVariable Long id) {

        Pet pet = petService.update(petDto, id);
        return nonNull(pet)
                ? new ResponseEntity<>(pet, OK)
                : new ResponseEntity<>(NOT_FOUND);
    }

    @DeleteMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deletePet(@PathVariable Long id) {
        petService.deleteById(id);
        return new ResponseEntity<>(OK);
    }
}
