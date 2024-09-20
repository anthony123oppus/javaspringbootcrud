package com.example.sprinconnect.service.implement;

import com.example.sprinconnect.Dto.FamilyDto;
import com.example.sprinconnect.models.Family;
import com.example.sprinconnect.models.FamilyPagination;
import com.example.sprinconnect.models.User;
import com.example.sprinconnect.repository.FamilyRepository;
import com.example.sprinconnect.repository.UserRepository;
import com.example.sprinconnect.service.FamilyService;
import com.example.sprinconnect.service.mappers.FamilyMapper;
import com.example.sprinconnect.service.mappers.ResponseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FamilyServiceImpl implements FamilyService {

    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public FamilyPagination<Family> getAllFamilyByUserId(int page, int size, long userId) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Page<Family> families = familyRepository.findAllByUserId(userId, pageable);

        List<Family> familyList = families.getNumberOfElements() == 0 ? List.of() : families.getContent();

        return new FamilyPagination<>(familyList, families.getNumber(), families.getSize(), families.getTotalElements(), families.getTotalPages(), families.isLast());
    }

    @Override
    public Map<String, Object> createFamily(FamilyDto familyDto) {

        Optional<User> userExist = userRepository.findById((long)familyDto.getUserId());
        Map<String, Object> serviceRes;

        if(userExist.isPresent()){

            Optional<Family> familyNameExist = familyRepository.findByFullName(familyDto.getFirstName(), familyDto.getLastName(), familyDto.getUserId());

            if(familyNameExist.isPresent()){
                serviceRes = ResponseMapper.responseMapper(null, "Family member already exist",  false,HttpStatus.BAD_REQUEST.value());
                return serviceRes;

            }

            Family family = new Family();
            Family familyMapper = FamilyMapper.familyMapper(family, familyDto);
            familyMapper.setUser(userExist.get());

            Family savedFamily = familyRepository.save(family);

            serviceRes = ResponseMapper.responseMapper(savedFamily, "Family created successfully",  true,HttpStatus.CREATED.value());

        }else{
            serviceRes = ResponseMapper.responseMapper(null,"User not found " + familyDto.getUserId() + " not found",  false,HttpStatus.NOT_FOUND.value());

        }

        return serviceRes;
    }

    @Override
    public Map<String, Object> updateFamily(FamilyDto familyDto) {

        Optional<User> userExist = userRepository.findById((long)familyDto.getUserId());
        Map<String, Object> serviceRes;

        if(userExist.isPresent()){

            Optional<Family> familyExist = familyRepository.findById(familyDto.getId());

            if(familyExist.isPresent()){
                Family family = familyExist.get();
                Family familyMapper = FamilyMapper.familyMapper(family, familyDto);
                familyMapper.setUser(userExist.get());

                Family updatedFamily = familyRepository.save(family);

                serviceRes = ResponseMapper.responseMapper(updatedFamily, "Family updated successfully",  true, HttpStatus.OK.value());

            }else{
                serviceRes = ResponseMapper.responseMapper(null, "Family not found",  false, HttpStatus.NOT_FOUND.value());

            }

        }else{
            serviceRes = ResponseMapper.responseMapper(null, "User not found " + familyDto.getUserId() + " not found",  false, HttpStatus.NOT_FOUND.value());

        }

        return serviceRes;
    }

    @Override
    public Map<String, Object> deleteFamily(long id) {
        Map<String, Object> serviceRes;

        Optional<Family> familyExist = familyRepository.findById(id);

        if(familyExist.isPresent()){
            familyRepository.delete(familyExist.get());
            serviceRes = ResponseMapper.responseMapper(null, "Family deleted successfully",  true, HttpStatus.OK.value());

        }else{
            serviceRes = ResponseMapper.responseMapper(null, "Family with an id of " + id + " not found",  false, HttpStatus.NOT_FOUND.value());

        }

        return serviceRes;
    }
}
