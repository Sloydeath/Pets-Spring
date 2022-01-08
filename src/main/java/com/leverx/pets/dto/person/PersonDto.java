package com.leverx.pets.dto.person;

import com.leverx.pets.annotations.ValidName;
import lombok.Data;

@Data
public class PersonDto {

    private Long id;

    @ValidName
    private String name;
}
