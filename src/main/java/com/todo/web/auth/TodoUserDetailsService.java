package com.todo.web.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TodoUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public TodoUserDetailsService(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username);
        if(null == user){
            throw new UsernameNotFoundException("cannot find username: " + username);
        }
        return new TodoUserPrincipal(user);
    }

    public Long getCurrentUserId(String username) {
        User user = this.userRepository.findByUsername(username);
        if(null == user){
            throw new UsernameNotFoundException("cannot find username: " + username);
        }
        return user.getId();
    }
}
