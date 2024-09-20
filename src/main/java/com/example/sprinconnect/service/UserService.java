package com.example.sprinconnect.service;

import com.example.sprinconnect.Dto.UserDto;
import com.example.sprinconnect.models.User;
import com.example.sprinconnect.models.UserPagination;

import java.util.List;
import java.util.Map;

public interface UserService  {

    public UserPagination<User> getAllUser(int page, int size, String email, String firstName, String lastName, String startDate, String endDate);

    public Map<String, Object> getUserById(int id);

    public Map<String, Object> createUser(UserDto userDto);

    public Map<String, Object> updateUser(UserDto userDto);

    public Map<String, Object> deleteUser(long userId);

    public Map<String, Object> multiDeleteUser(List<Long> userIds);
}
