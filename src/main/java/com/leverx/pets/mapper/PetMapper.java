package com.leverx.pets.mapper;

import com.leverx.pets.dto.pet.PetDto;
import com.leverx.pets.dto.pet.ResponsePetDto;
import com.leverx.pets.model.Person;
import com.leverx.pets.model.pet.Pet;
import com.leverx.pets.service.PersonService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.leverx.pets.factory.PetFactory.getPet;
import static com.leverx.pets.model.pet.enums.PetType.valueOf;

@Component
public class PetMapper {

    private final ModelMapper modelMapper;
    private final PersonService personService;

    @Autowired
    public PetMapper(ModelMapper modelMapper, PersonService personService) {
        this.modelMapper = modelMapper;
        this.personService = personService;
    }

    public ResponsePetDto convertToResponsePetDto(Pet pet) {

        ResponsePetDto responsePetDto = modelMapper.map(pet, ResponsePetDto.class);
        responsePetDto.setPetType(valueOf(pet.getPetType()));

        return responsePetDto;
    }

    public Pet convertToEntity(PetDto petDto) {

        Pet pet = getPet(petDto.getPetType());

        Person person = personService.getById(petDto.getPersonId());

        pet.setName(petDto.getName());
        pet.setPerson(person);

        return pet;
    }

}
