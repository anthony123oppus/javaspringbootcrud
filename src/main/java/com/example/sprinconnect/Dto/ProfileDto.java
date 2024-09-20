package com.example.sprinconnect.Dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {

    private Long id;

    private String permanentAddress;

    private String currentAddress;

    private String phoneNumber;

    private long userId;
}
