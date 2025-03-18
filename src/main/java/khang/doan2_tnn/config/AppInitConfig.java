package khang.doan2_tnn.config;

import khang.doan2_tnn.entities.users;
import khang.doan2_tnn.repositories.userRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AppInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(userRepository userRepository){

        return args -> {
            if (userRepository.findByUsername("admin") == null){
                users user = users.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .role("ROLE_ADMIN")
                        .fullName("Quản trị viên")
                        .email("admin@admin.com")
                        .userPicUrl("/AdminDefaultAvatar.png")
                        .build();
                userRepository.save(user);
                log.warn("admin user has been created with default password: admin");
            }
        };
    };
}
