package com.leverx.pets.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Pet not found")
public class PetNotFoundException extends EntityNotFoundException {

    public PetNotFoundException(String message) {
        super(message);
    }
}
