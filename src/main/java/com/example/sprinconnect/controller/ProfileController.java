package com.example.sprinconnect.controller;

import com.example.sprinconnect.Dto.ProfileDto;
import com.example.sprinconnect.models.Profile;
import com.example.sprinconnect.models.ProfilePagination;
import com.example.sprinconnect.repository.ProfileRepository;
import com.example.sprinconnect.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping
    public ProfilePagination<Profile> getAllProfiles(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "phoneNumber", required = false, defaultValue = "") String phoneNumber,
            @RequestParam(name = "currentAddress", required = false, defaultValue = "") String currentAddress,
            @RequestParam(name = "permanentAddress", required = false, defaultValue = "") String permanentAddress
    ){
        return profileService.getAllProfiles(page, size, phoneNumber, currentAddress, permanentAddress);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createProfile(@Valid @RequestBody ProfileDto profileDto) {
        Map<String, Object> response = profileService.createProfile(profileDto);

        Boolean isSuccess = (Boolean) response.get("status");

        if(Boolean.FALSE.equals(isSuccess)) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<Map<String, Object>> getProfileById(@PathVariable int profileId) {

        Map<String, Object> response = profileService.getProfileById(profileId);
        Boolean isSuccess = (Boolean) response.get("status");

        if (!isSuccess) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<Map<String, Object>> updateProfile(@Valid @RequestBody ProfileDto profileDto) {
        Map<String, Object> response = profileService.updateProfile(profileDto);

        Boolean isSuccess = (Boolean) response.get("status");

        if(Boolean.FALSE.equals(isSuccess)){
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{profileId}")
    public ResponseEntity<Map<String, Object>> deleteProfile(@PathVariable int profileId){
        Map<String, Object> response = profileService.deleteProfile(profileId);
        Boolean isSuccess = (Boolean) response.get("status");

        if(!isSuccess){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
