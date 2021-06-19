package com.leverx.pets.service.impl;

import com.leverx.pets.dto.pet.SwappingPetsDto;
import com.leverx.pets.dto.pet.UpdatePetDto;
import com.leverx.pets.exception.custom.PetNotFoundException;
import com.leverx.pets.exception.custom.SimilarPersonException;
import com.leverx.pets.model.Person;
import com.leverx.pets.model.pet.Pet;
import com.leverx.pets.repository.PetRepository;
import com.leverx.pets.service.PetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.leverx.pets.util.ExceptionMessageUtil.PET_ERROR_PATTERN;
import static com.leverx.pets.util.ExceptionMessageUtil.SIMILAR_PEOPLE_MESSAGE;
import static java.lang.String.format;
import static java.util.Arrays.asList;

@Service
@Transactional
@Slf4j
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;

    @Autowired
    public PetServiceImpl(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public Pet create(Pet pet) {
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
                .orElseThrow(() -> {
                    log.debug(format(PET_ERROR_PATTERN, id));
                    return new PetNotFoundException(format(PET_ERROR_PATTERN, id));
                });
    }

    @Override
    public void deleteById(Long id) {
        petRepository.deleteById(id);
    }

    @Override
    public Pet update(UpdatePetDto petDto, Long id) {
        Pet pet = petRepository
                .findById(id)
                .orElseThrow(() -> {
                    log.debug(format(PET_ERROR_PATTERN, id));
                    return new PetNotFoundException(format(PET_ERROR_PATTERN, id));
                });

        pet.setName(petDto.getName());
        petRepository.save(pet);
        return pet;
    }

    @Override
    public List<Pet> swappingPets(SwappingPetsDto swappingPetsDto) {

        Pet firstPet = getById(swappingPetsDto.getFirstPetId());
        Pet secondPet = getById(swappingPetsDto.getSecondPetId());

        if (!Objects.equals(firstPet.getPerson(), secondPet.getPerson())) {
            return executeChangingOwners(firstPet, secondPet);
        }
        else {
            throw new SimilarPersonException(SIMILAR_PEOPLE_MESSAGE);
        }
    }

    private List<Pet> executeChangingOwners(Pet firstPet, Pet secondPet) {

        Person tempPetOwner = firstPet.getPerson();
        firstPet.setPerson(secondPet.getPerson());
        secondPet.setPerson(tempPetOwner);

        petRepository.save(firstPet);
        petRepository.save(secondPet);

        return asList(firstPet, secondPet);
    }

}