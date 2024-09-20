package com.example.sprinconnect.controller;

import com.example.sprinconnect.Dto.UserDto;
import com.example.sprinconnect.models.User;
import com.example.sprinconnect.models.UserIdsRequest;
import com.example.sprinconnect.models.UserPagination;
import com.example.sprinconnect.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController()
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public UserPagination<User> getUsers(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "email", required = false, defaultValue = "") String email,
            @RequestParam(name = "firstName", required = false, defaultValue = "") String firstName,
            @RequestParam(name = "lastName", required = false, defaultValue = "") String lastName,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate
    ) {
        return userService.getAllUser(page, size, email, firstName, lastName, startDate, endDate);
    }


    @PostMapping()
    public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody UserDto userDto) {
        Map<String, Object> response = userService.createUser(userDto);
        Boolean isSuccess = (Boolean) response.get("status");

        if (Boolean.TRUE.equals(isSuccess)) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping()
    public ResponseEntity<Map<String, Object>> updateUser(@Valid @RequestBody UserDto userDto) {

        Map<String, Object> response = userService.updateUser(userDto);
        Boolean isSuccess = (Boolean) response.get("status");

        if (Boolean.TRUE.equals(isSuccess)) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @Async
    @GetMapping("/{userId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getUserById(@PathVariable int userId) {

        Map<String, Object> response = userService.getUserById(userId);

        Boolean isSuccess = (Boolean) response.get("status");

        if(Boolean.FALSE.equals(isSuccess)) {
            return CompletableFuture.completedFuture(new ResponseEntity<>(response, HttpStatus.NOT_FOUND));
        }
        return CompletableFuture.completedFuture(new ResponseEntity<>(response, HttpStatus.OK));
    }

    @Async
    @DeleteMapping("/{userId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> deleteUser(@PathVariable long userId) {

       return CompletableFuture.supplyAsync(() -> {
           Map<String, Object> response = userService.deleteUser(userId);

           Boolean isSuccess = (Boolean) response.get("status");

           if(Boolean.FALSE.equals(isSuccess)) {
               return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
           }

           return new ResponseEntity<>(response, HttpStatus.OK);
       });

    }

    @Async
    @DeleteMapping("/multipledelete")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> deleteMultipleUsers(@RequestBody UserIdsRequest userIdsRequest) {

        return CompletableFuture.supplyAsync(() -> {
            Map<String, Object> response = userService.multiDeleteUser(userIdsRequest.getUserIds());

            Boolean isSuccess = (Boolean) response.get("status");

            if(Boolean.FALSE.equals(isSuccess)) {
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

            }

            return new ResponseEntity<>(response, HttpStatus.OK);

        });

    }

}
