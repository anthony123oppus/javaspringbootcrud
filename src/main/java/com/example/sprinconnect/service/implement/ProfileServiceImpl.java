package com.example.sprinconnect.service.implement;

import com.example.sprinconnect.Dto.ProfileDto;
import com.example.sprinconnect.models.Profile;
import com.example.sprinconnect.models.ProfilePagination;
import com.example.sprinconnect.models.User;
import com.example.sprinconnect.repository.ProfileRepository;
import com.example.sprinconnect.repository.UserRepository;
import com.example.sprinconnect.service.ProfileService;
import com.example.sprinconnect.service.filter.ProfileFilter;
import com.example.sprinconnect.service.mappers.ProfileMapper;
import com.example.sprinconnect.service.mappers.ResponseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProfileServiceImpl(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ProfilePagination<Profile> getAllProfiles(int page, int size, String phoneNumber, String currentAddress, String permanentAddress) {

        Specification<Profile> specs = ProfileFilter.filterBySearch(phoneNumber, currentAddress, permanentAddress);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");

        Page<Profile> profiles = profileRepository.findAll(specs, pageable);

//        ----------------------kung tanan data iapil ug display---------------------------
        List<Profile> profileList = profiles.getNumberOfElements() == 0 ? Collections.emptyList() : profiles.getContent();

//        ----------------------mappers---------------------kung naa kay gusto na dili iapil na data sa response data---------------------------
//        List<Profile> profileList = profiles.getNumberOfElements() == 0 ? Collections.emptyList() : profiles.getContent().stream().map(item -> {
//            Profile profile = new Profile();
//            profile.setId(item.getId());
//            profile.setCurrentAddress(item.getCurrentAddress());
//            profile.setPermanentAddress(item.getPermanentAddress());
//            profile.setPhoneNumber(item.getPhoneNumber());
//            return profile;
//        }).collect(Collectors.toList());

        return new ProfilePagination<>(profileList, profiles.getNumber(), profiles.getSize(), profiles.getTotalElements(), profiles.getTotalPages(), profiles.isLast());
    }

    @Override
    public Map<String, Object> createProfile(ProfileDto profileDto) {

        Optional<User> userExist = userRepository.findById((long)profileDto.getUserId());

        Map<String, Object> serviceRes;

        if(userExist.isPresent()){
            User userUpdate = userExist.get();

            if(userUpdate.getUserProfile() != null && userUpdate.getUserProfile().getId() != null){
                serviceRes = ResponseMapper.responseMapper(null,
                                "User with an id of " + profileDto.getUserId() + " already have profile try to update",
                                false,
                                HttpStatus.BAD_REQUEST.value()
                                );

            }else{
                Profile profiles = new Profile();
                Profile profileMapper = ProfileMapper.profileMapper(profiles, profileDto);
                Profile profileSave = profileRepository.save(profileMapper);

                userUpdate.setUserProfile(profileSave);

                User updatedUser = userRepository.save(userUpdate);

                serviceRes = ResponseMapper.responseMapper(updatedUser,
                                "Profile Added successfully",
                                true,
                                HttpStatus.OK.value()
                                );

            }

        }else{
            serviceRes = ResponseMapper.responseMapper(null,
                            "User with an id of " + profileDto.getUserId() + " not found.",
                            false,
                            HttpStatus.NOT_FOUND.value()
                            );

        }

        return serviceRes;
    }

    @Override
    public Map<String, Object> deleteProfile(long id) {
        Optional<Profile> profileExist = profileRepository.findById(id);

        Map<String, Object> serviceRes;

        if(profileExist.isPresent()){
            Profile profile = profileExist.get();

            Optional<User> user = userRepository.findByProfileId(id);

            if(user.isPresent()){
                User userProfile = user.get();

                userProfile.setUserProfile(null);
                userRepository.save(userProfile);

                profileRepository.delete(profile);
                serviceRes = ResponseMapper.responseMapper(null,
                        "Profile with an id of " + id + " deleted successfully",
                        true,
                        HttpStatus.OK.value()
                );

            }else{
                serviceRes = ResponseMapper.responseMapper(null,
                                "User with an id of " + id + " not found",
                                false,
                                HttpStatus.NOT_FOUND.value()
                                );
            }

        }else{
            serviceRes = ResponseMapper.responseMapper(null,
                            "Profile with an id of " + id + " not found",
                            false,
                            HttpStatus.NOT_FOUND.value()
                            );

        }

        return serviceRes;
    }

    @Override
    public Map<String, Object> getProfileById(long id) {
        Optional<Profile> profileExist = profileRepository.findById(id);

        Map<String, Object> serviceRes;

//        if(profileExist.isPresent()){
//            serviceRes = ResponseMapper.responseMapper(profileExist.get(),
//                            "Profile retrieved successfully",
//                            true,
//                            HttpStatus.OK.value()
//                            );
//
//        }else{
//            serviceRes = ResponseMapper.responseMapper(null,
//                            "Profile with an id of " + id + " not found",
//                            false,
//                            HttpStatus.NOT_FOUND.value()
//                            );
//
//        }
//      ----------------------------------OR-----------------------------------
//      ----------------------enhance if else statement---------------------------
        serviceRes = profileExist.map(profile -> ResponseMapper.responseMapper(profile,
                "Profile retrieved successfully",
                true,
                HttpStatus.OK.value()
        )).orElseGet(() -> ResponseMapper.responseMapper(null,
                "Profile with an id of " + id + " not found",
                false,
                HttpStatus.NOT_FOUND.value()
        ));

        return serviceRes;
    }

    @Override
    public Map<String, Object> updateProfile(ProfileDto profileDto) {

        Map<String, Object> serviceRes;

            Optional<Profile> profileExist = profileRepository.findById(profileDto.getId());

            if(profileExist.isPresent()){
                Profile profile = profileExist.get();
                Profile profileMapper = ProfileMapper.profileMapper(profile, profileDto);

                Profile savedProfile = profileRepository.save(profileMapper);

                serviceRes = ResponseMapper.responseMapper(savedProfile,
                                "Profile updated successfully",
                                true,
                                HttpStatus.OK.value()
                                );

            }else{
                serviceRes = ResponseMapper.responseMapper(null,
                                "Profile with an id of " + profileDto.getId() + " not found.",
                                false,
                                HttpStatus.NOT_FOUND.value()
                            );

            }

        return serviceRes;
    }
}
