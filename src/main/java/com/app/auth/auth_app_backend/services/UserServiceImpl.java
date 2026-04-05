package com.app.auth.auth_app_backend.services;

import com.app.auth.auth_app_backend.dto.UserDto;
import com.app.auth.auth_app_backend.entities.Provider;
import com.app.auth.auth_app_backend.entities.User;
import com.app.auth.auth_app_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Iterator;
@RequiredArgsConstructor
@Service

public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        if(userDto.getEmail()==null || userDto.getEmail().isBlank())
            throw new IllegalArgumentException("Enter the email id");

        if(userRepository.existsByEmail(userDto.getEmail()))
            throw new IllegalArgumentException("Email Already exists");

        User user = modelMapper.map(userDto,User.class);

        user.setProvider(userDto.getProvider()!=null?userDto.getProvider() : Provider.LOCAL);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto FindByEmail(String email) {
        return null;
    }

    @Override
    public UserDto UpdateUser(UserDto userDto, String userId) {
        return null;
    }

    @Override
    public UserDto deleteUser(String userId) {
        return null;
    }

    @Override
    public Iterable<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map( user->modelMapper.map(user,UserDto.class))
                .toList();
    }
}
