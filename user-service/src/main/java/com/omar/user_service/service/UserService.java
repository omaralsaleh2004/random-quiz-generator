package com.omar.user_service.service;

import com.omar.user_service.dto.UserDto;
import com.omar.user_service.exception.NotFoundException;
import com.omar.user_service.model.User;
import com.omar.user_service.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public UserDto getUserById(int userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new NotFoundException("User not Found"));

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());

        return userDto;
    }

    public Map<Integer,UserDto> getUsersByIds(List<Integer> userIds) {
        List<User> users = userRepo.findAllById(userIds);
        Map<Integer , UserDto> userDtoMap = new HashMap<>();

        for(User user : users) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setEmail(user.getEmail());
            userDto.setFirstName(user.getFirstName());
            userDto.setLastName(user.getLastName());

            userDtoMap.put(userDto.getId(),userDto);
        }
        return userDtoMap;
    }
}
