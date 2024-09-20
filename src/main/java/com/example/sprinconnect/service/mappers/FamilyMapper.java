package com.example.sprinconnect.service.mappers;

import com.example.sprinconnect.Dto.FamilyDto;
import com.example.sprinconnect.models.Family;

public class FamilyMapper {

    public static Family familyMapper(Family family, FamilyDto familyDto){

        family.setEmail(familyDto.getEmail());
        family.setPhoneNumber(familyDto.getPhoneNumber());
        family.setFirstName(familyDto.getFirstName());
        family.setLastName(familyDto.getLastName());
        family.setRelationship(familyDto.getRelationship());

        return family;
    }
}
