package com.example.sprinconnect.service.mappers;

import com.example.sprinconnect.Dto.ProfileDto;
import com.example.sprinconnect.models.Profile;

public class ProfileMapper {

    public static Profile profileMapper (Profile profile, ProfileDto profileDto) {

        profile.setCurrentAddress(profileDto.getCurrentAddress());
        profile.setPhoneNumber(profileDto.getPhoneNumber());
        profile.setPermanentAddress(profileDto.getPermanentAddress());

        return profile;
    }
}
