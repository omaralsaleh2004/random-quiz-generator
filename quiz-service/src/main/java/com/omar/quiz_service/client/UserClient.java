package com.omar.quiz_service.client;

import com.omar.quiz_service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient("user-service")
public interface UserClient {
    @GetMapping("/user/{userId}")
    public UserDto getUserById(@PathVariable int userId);

    @PostMapping("/user/all")
    public Map<Integer , UserDto> getUsersByIds(@RequestBody List<Integer> userIds);
}
