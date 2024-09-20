package com.example.sprinconnect.controller;

import com.example.sprinconnect.Dto.FamilyDto;
import com.example.sprinconnect.models.Family;
import com.example.sprinconnect.models.FamilyPagination;
import com.example.sprinconnect.service.FamilyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/families")
public class FamilyController {

    @Autowired
    private FamilyService familyService;

    @GetMapping("/{userId}")
    public FamilyPagination<Family> getAllFamilyByUserId(@PathVariable long userId,
                                                         @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                         @RequestParam(name = "size", required = false, defaultValue = "10") int size)
    {
        return familyService.getAllFamilyByUserId(page, size, userId);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createFamily(@RequestBody FamilyDto familyDto) {
        Map<String, Object> response = familyService.createFamily(familyDto);

        Boolean isSuccess = (Boolean) response.get("status");

        if(Boolean.FALSE.equals(isSuccess)) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Map<String, Object>> updateFamily(@RequestBody FamilyDto familyDto) {
        Map<String, Object> response = familyService.updateFamily(familyDto);

        Boolean isSuccess = (Boolean) response.get("status");

        if(Boolean.FALSE.equals(isSuccess)) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteFamily(@PathVariable long id) {
        Map<String, Object> response = familyService.deleteFamily(id);

        Boolean isSuccess = (Boolean) response.get("status");

        if (Boolean.FALSE.equals(isSuccess)) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
