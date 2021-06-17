package com.leverx.pets.service;

import com.leverx.pets.dto.pet.PetDto;
import com.leverx.pets.dto.pet.SwappingPetsDto;
import com.leverx.pets.dto.pet.UpdatePetDto;
import com.leverx.pets.model.pet.Pet;

import java.util.List;

public interface PetService {

    Pet create(PetDto petDto);
    List<Pet> getAll();
    Pet getById(Long id);
    void deleteById(Long id);
    Pet update(UpdatePetDto petDto, Long id);
    List<Pet> swappingPets(SwappingPetsDto swappingPetsDto);
}
