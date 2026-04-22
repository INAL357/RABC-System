package com.app.auth.auth_app_backend.controllers;

import com.app.auth.auth_app_backend.dto.UserDto;
import com.app.auth.auth_app_backend.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/l1/users")
public class UserController {

    private final UserService userService;

    //Create users
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody  UserDto userDto) {
        return  ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDto));
    }

    //Get all user details
    @GetMapping
    public  ResponseEntity<Iterable<UserDto>> getAllUsers() {

        return ResponseEntity.ok(userService.getAllUsers());
    }

    //Get user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> FindByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(userService.FindByEmail(email));
    }

    //Get By id
    @GetMapping("/Id/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("UserId") String userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    //Delete an User
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") String userId) {
       userService.deleteUser(userId);
    }

    //Update An User
    @PutMapping("update/{userId}")
    public ResponseEntity<UserDto> UpdateUser(@RequestBody UserDto userDto, @PathVariable("userId") String userId) {
    return ResponseEntity.ok(userService.UpdateUser(userDto,userId));
    }


}
