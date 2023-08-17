package com.example.demo.security.costomUser;

import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("loadUserByUsername()");
        Optional<User> maybeUser = userRepository.findUser(email);
        if(maybeUser.isEmpty()) {
            throw new UsernameNotFoundException("There are no users matching the email: "+email);
        }
        User user = maybeUser.get();
        return CustomUserDetails.builder()
                .username(email)
                .password(user.getPassword())
                .authorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_"+user.getRole().name())))
                .isEnabled(true)
                .isCredentialsNonExpired(true)
                .isAccountNonLocked(true)
                .isAccountNonExpired(true)
                .build();
    }
}
