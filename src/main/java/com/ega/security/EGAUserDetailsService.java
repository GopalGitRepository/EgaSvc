package com.ega.security;

import com.ega.model.EGAUserDetails;
import com.ega.model.User;
import com.ega.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EGAUserDetailsService implements UserDetailsService {

    @Autowired
    public UserRepository userRepository;

    @Override
    public EGAUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserName(username);
        user.orElseThrow(() -> new UsernameNotFoundException("User details not found in a repository:" + username));
        return user.map(EGAUserDetails::new).get();
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User findUser(String username){
        Optional<User> user = userRepository.findByUserName(username);
        user.orElseThrow(() -> new UsernameNotFoundException("User details not found in a repository:" + username));
        return user.get();
    }
}
