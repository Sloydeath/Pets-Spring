package com.leverx.pets.service.impl;

import com.leverx.pets.dto.PetDto;
import com.leverx.pets.dto.UpdatePetDto;
import com.leverx.pets.model.Person;
import com.leverx.pets.model.pet.Pet;
import com.leverx.pets.repository.PersonRepository;
import com.leverx.pets.repository.PetRepository;
import com.leverx.pets.service.PetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static com.leverx.pets.factory.PetFactory.getPet;

@Service
@Transactional
@Slf4j
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final PersonRepository personRepository;

    @Autowired
    public PetServiceImpl(PetRepository petRepository, PersonRepository personRepository) {
        this.petRepository = petRepository;
        this.personRepository = personRepository;
    }

    @Override
    public Pet create(PetDto petDto) {

        Pet pet = getPet(petDto.getPetType());
        Person person = personRepository
                .findById(petDto.getPersonId())
                .orElseThrow(() -> new EntityNotFoundException("Person doesn't exist"));
        pet.setName(petDto.getName());
        pet.setPerson(person);
        petRepository.save(pet);
        return pet;
    }

    @Override
    public List<Pet> getAll() {
        return petRepository.findAll();
    }

    @Override
    public Pet getById(Long id) {
        return petRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found"));
    }

    @Override
    public void deleteById(Long id) {
        petRepository.deleteById(id);
    }

    @Override
    public Pet update(UpdatePetDto petDto) {
        Pet pet = petRepository
                .findById(petDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Pet not found"));
        pet.setName(petDto.getName());
        petRepository.save(pet);
        return pet;
    }
}
