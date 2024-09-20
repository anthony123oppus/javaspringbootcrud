package com.example.sprinconnect.service;

import com.example.sprinconnect.Dto.FamilyDto;
import com.example.sprinconnect.models.Family;
import com.example.sprinconnect.models.FamilyPagination;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

public interface FamilyService {

    public FamilyPagination<Family> getAllFamilyByUserId(int page, int size, long userId);

    public Map<String, Object> createFamily(FamilyDto familyDto);

    public Map<String, Object> updateFamily(FamilyDto familyDto);

    public Map<String, Object> deleteFamily(long id);
}
