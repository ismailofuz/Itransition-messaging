package com.example.task5_mailing.service;

import com.example.task5_mailing.entity.User;
import com.example.task5_mailing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void saveUser(String username){
        Optional<User> byUsername = userRepository.findByUsername(username);
        if(byUsername.isEmpty()){
            User user = new User(null,username);
            userRepository.save(user);
        }
    }

    public List<String> getUsernames(String key){
        List<String> usernames = new ArrayList<>();
        List<User> users = userRepository.searchUsers(key);
        for(User user: users)
            usernames.add(user.getUsername());
        return usernames;
    }

}
