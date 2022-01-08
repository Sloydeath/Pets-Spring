package com.leverx.pets.dto.pet;

import com.leverx.pets.annotations.ValidName;
import com.leverx.pets.model.Person;
import com.leverx.pets.model.pet.enums.PetType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ResponsePetDto {

    private Long id;

    @ValidName
    private String name;

    @NotNull
    private PetType petType;

    @NotNull
    private Person person;
}
