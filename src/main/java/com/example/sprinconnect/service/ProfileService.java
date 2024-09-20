package com.example.sprinconnect.service;

import com.example.sprinconnect.Dto.ProfileDto;
import com.example.sprinconnect.models.Profile;
import com.example.sprinconnect.models.ProfilePagination;

import java.util.List;
import java.util.Map;

public interface ProfileService {

    public ProfilePagination<Profile> getAllProfiles(int page, int size,String phoneNumber, String currentAddress, String permanentAddress);

    public Map<String, Object> createProfile(ProfileDto profileDto);

    public Map<String, Object> deleteProfile(long id);

    public Map<String, Object> getProfileById(long id);

    public Map<String, Object> updateProfile(ProfileDto profileDto);
}
