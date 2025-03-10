package khang.doan2_tnn.services;

import khang.doan2_tnn.entities.users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import khang.doan2_tnn.repositories.userRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

@Service
@Slf4j
public class userService implements UserDetailsService {

    @Autowired
    private userRepository userRepository;

    public void saveUser(String fullName, String username, String password, String email) {
        users user = users.builder()
               .fullName(fullName)
               .username(username)
               .password(password)
               .email(email)
                .role("ROLE_USER")
                .userPicUrl("/UserDefaultAvatar.png")
               .build();
        userRepository.save(user);
    }

    public List<users> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        users user = userRepository.findByUsername(username);
        if (user != null) {
            return User.withUsername(user.getUsername())
                    .password(user.getPassword())
                    .authorities(user.getRole())
                    .build();
        }else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
