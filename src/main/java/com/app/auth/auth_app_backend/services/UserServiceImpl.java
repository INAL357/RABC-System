package com.app.auth.auth_app_backend.services;

import com.app.auth.auth_app_backend.Exception.ResourceNotFoundException;
import com.app.auth.auth_app_backend.dto.UserDto;
import com.app.auth.auth_app_backend.entities.Provider;
import com.app.auth.auth_app_backend.entities.User;
import com.app.auth.auth_app_backend.helper.UserHelper;
import com.app.auth.auth_app_backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.UUID;

@RequiredArgsConstructor
@Service

public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
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
        User user = userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("Email not found"));
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public UserDto UpdateUser(UserDto userDto, String userId) {
        UUID uId = UserHelper.parseUUID(userId);
        User existingUser = userRepository.findById(uId).orElseThrow(()->new ResourceNotFoundException("Given Id Not Found"));

        if(userDto.getName()!=null) existingUser.setName(userDto.getName());
        if(userDto.getImage()!=null) existingUser.setImage(userDto.getImage());
        if(userDto.getProvider()!=null) existingUser.setProvider(userDto.getProvider());

        //TODO: Change Password LOGIC later
        if(userDto.getPassword()!=null) existingUser.setPassword(userDto.getPassword());

        existingUser.setEnable(userDto.isEnable());
        User UpdateUser =userRepository.save(existingUser);
        return modelMapper.map(UpdateUser,UserDto.class);
    }

    @Override
    public void deleteUser(String userId) {
        UUID uId = UserHelper.parseUUID(userId);
        User user = userRepository.findById(uId).orElseThrow(()->new ResourceNotFoundException("Given Id Not Found"));
        userRepository.deleteById(uId);

    }

    @Override
    @Transactional
    public Iterable<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map( user->modelMapper.map(user,UserDto.class))
                .toList();
    }

    @Override
    public UserDto getUserById(String userId) {
        UUID uId = UserHelper.parseUUID(userId);
        User user = userRepository.findById(uId).orElseThrow(()->new ResourceNotFoundException("Given Id Not Found"));

        return modelMapper.map(user,UserDto.class);
    }
}
