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
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
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
        return userRepository.findByRole("ROLE_USER");
    }
    public List<users> getAllAdmins() {
        return userRepository.findByRole("ROLE_ADMIN");
    }

    public void deleteById(String userId) {
        users user = userRepository.findByUserId(userId);
        if (user != null && !user.getUsername().equals("admin")) {
            if (!user.getUserPicUrl().equals("/UserDefaultAvatar.png")) {
                String picUrl = user.getUserPicUrl();
                String newPicUrl = picUrl.replace("/UserProfilePics/", "src/main/resources/static/UserProfilePics/");
                File oldPicFile = new File(newPicUrl);
                    oldPicFile.delete();
            }
            userRepository.deleteByUserId(userId);
        }
    }
    public void updateUser(String role, String userId) {
        users user = userRepository.findByUserId(userId);
        if (user != null && !user.getUsername().equals("admin")) {
            user.setRole(role);
            if (role.equals("ROLE_ADMIN"))
                user.setUserPicUrl("/AdminDefaultAvatar.png");
            else
                user.setUserPicUrl("/UserDefaultAvatar.png");
            userRepository.save(user);
        }
    }
    public users findByUserId(String userId) {
        return userRepository.findByUserId(userId);
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
    public List<users> searchUsers(String fullName, String email) {
        return userRepository.searchUsers(fullName, email);
    }
}
