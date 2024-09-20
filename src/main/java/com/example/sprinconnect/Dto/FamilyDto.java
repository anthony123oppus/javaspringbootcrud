package com.example.sprinconnect.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FamilyDto {

    private long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String relationship;

    private long userId;
}
