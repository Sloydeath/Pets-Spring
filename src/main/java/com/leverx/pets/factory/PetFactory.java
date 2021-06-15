package com.leverx.pets.factory;

import com.leverx.pets.model.pet.Cat;
import com.leverx.pets.model.pet.Dog;
import com.leverx.pets.model.pet.Pet;
import com.leverx.pets.model.pet.enums.PetType;

import javax.persistence.EntityNotFoundException;

public class PetFactory {

    public static Pet getPet(PetType petType) {

        switch (petType) {
            case CAT:
                return new Cat();
            case DOG:
                return new Dog();
            default:
                throw new EntityNotFoundException("Pet not found");
        }
    }
}
