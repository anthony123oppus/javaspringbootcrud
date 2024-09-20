package com.example.sprinconnect.service.implement;

import com.example.sprinconnect.Dto.UserDto;
import com.example.sprinconnect.models.Family;
import com.example.sprinconnect.models.Profile;
import com.example.sprinconnect.models.User;
import com.example.sprinconnect.models.UserPagination;
import com.example.sprinconnect.repository.UserRepository;
import com.example.sprinconnect.service.UserService;
import com.example.sprinconnect.service.filter.UserFilter;
import com.example.sprinconnect.service.mappers.ResponseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.stream.Collectors;

//need para sa Autowired sa controller
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserPagination<User> getAllUser(int page, int size, String email, String firstName, String lastName, String startDate, String endDate) {

        Specification<User> specs = UserFilter.filterBySearch(email, firstName, lastName, startDate, endDate);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Page<User> users = userRepository.findAll(specs, pageable);

        List<User> userList = users.getNumberOfElements() == 0 ? Collections.emptyList() : users.getContent().stream().map(user -> {
            Profile userProf = user.getUserProfile();
            Profile profile = userProf != null ? userProf.getUser().getUserProfile() : null;

            return User.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .userProfile(profile)
                    .build();
        }).collect(Collectors.toList());

        return new UserPagination<>(userList, users.getNumber(), users.getSize(),
                                    users.getTotalElements(), users.getTotalPages(),
                                    users.isLast());
    }

    @Override
    public Map<String, Object> getUserById(int id) {
        Optional<User> optionalUser = userRepository.findById((long)id);

        Map<String, Object> serviceRes;

        serviceRes = optionalUser.map(user -> ResponseMapper.responseMapper(user,
                "User found",
                true,
                HttpStatus.OK.value()
        )).orElseGet(() -> ResponseMapper.responseMapper(null,
                "User not found",
                false,
                HttpStatus.NOT_FOUND.value()
        ));

        return  serviceRes;

    }

    @Override
    public Map<String, Object> createUser(UserDto userDto) {
        // Check if user with the given email already exists
        Optional<User> existingUser = userRepository.findByEmail(userDto.getEmail());

        Map<String, Object> serviceRes = new HashMap<>();

        if (existingUser.isPresent()) {
            // Handle the case where the email already exists
//            throw new RuntimeException("User with email " + userDto.getEmail() + " already exists.");
            serviceRes = ResponseMapper.responseMapper(existingUser.get(),
                            "User with email " + userDto.getEmail() + " already exists.",
                            false,
                            HttpStatus.BAD_REQUEST.value()
                            );

        }else{
            User user = new User();
            user.setEmail(userDto.getEmail());
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());

            User userData = userRepository.save(user);

            serviceRes = ResponseMapper.responseMapper(userData,
                            "User created successfully",
                            true,
                            HttpStatus.CREATED.value()
                            );

        }

        return serviceRes;
    }

    @Override
    public Map<String, Object> updateUser(UserDto userDto) {
        Optional<User> userExist = userRepository.findById((long)userDto.getId());

        Map<String, Object> serviceRes;

        if(userExist.isPresent()){
            User user = userExist.get(); // Get the existing user
            // Update fields
            user.setEmail(userDto.getEmail());
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());

            // Save the updated user
            User savedUser =userRepository.save(user);


            serviceRes = ResponseMapper.responseMapper(savedUser,
                            "User updated successfully",
                            true,
                            HttpStatus.OK.value()
                            );

        }else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            serviceRes = ResponseMapper.responseMapper(userDto,
                            "User with Id " + userDto.getId() + " does not exist.",
                            false,
                            HttpStatus.NOT_FOUND.value()
                            );

        }

        return serviceRes;
    }

    @Override
    public Map<String, Object> deleteUser(long userId) {
        Optional<User> userExist = userRepository.findById((long)userId);

        Map<String, Object> serviceRes;

        if(userExist.isPresent()){
            userRepository.delete(userExist.get());
            serviceRes = ResponseMapper.responseMapper(null,
                            "User deleted successfully",
                            true,
                            HttpStatus.OK.value()
                            );

        }else {
            serviceRes = ResponseMapper.responseMapper(null,
                            "User not found",
                            false,
                            HttpStatus.NOT_FOUND.value()
                            );

        }

        return serviceRes;
    }

    @Override
    public Map<String, Object> multiDeleteUser(List<Long> userIds) {
        // Find all existing IDs in the database
        List<User> existingUsers = userRepository.findAllById(userIds);
        Set<Long> existingUserIds = existingUsers.stream().map(User::getId).collect(Collectors.toSet());

        // Determine which IDs are not found
        List<Long> notFoundIds = userIds.stream()
                .filter(id -> !existingUserIds.contains(id))
                .toList();

        // Perform delete operation for existing IDs
        userRepository.deleteAllById(existingUserIds);

        Map<String, Object> serviceRes;

        if(!notFoundIds.isEmpty()) {
            serviceRes = ResponseMapper.responseMapper(null,
                            "The following IDs were not found: " +
                                     notFoundIds.stream()
                                                .map(String::valueOf)
                                                .collect(Collectors.joining(", ")),
                            false,
                            HttpStatus.NOT_FOUND.value()
                            );
            
        }else{
            serviceRes = ResponseMapper.responseMapper(null,
                            "Users deleted successfully",
                            true,
                            HttpStatus.OK.value()
                            );

        }

        return serviceRes;
    }

}
