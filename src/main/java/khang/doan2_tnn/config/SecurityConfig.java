package khang.doan2_tnn.config;

import khang.doan2_tnn.services.userService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private final userService userService;

    @Bean
    public UserDetailsService userDetailsService() {
        return userService;
    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DefaultSecurityFilterChain SecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(httpForm -> {
                    httpForm
                        .loginPage("/login").permitAll()
                            .successHandler(successHandler());
                }).logout(httpLogout -> httpLogout
                        .logoutUrl("/logout") // URL để xử lý đăng xuất
                        .logoutSuccessUrl("/TrangChu") // Chuyển hướng sau khi đăng xuất
                        .permitAll()
                )
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/TrangChu/**",
                            "/login",
                            "/register",
                            "/forgotPassword/**",
                            "/css/**",
                            "/js/**",
                            "/img/**",
                            "/music/**",
                            "/playlistPics/**",
                            "/UserProfilePics/**",
                            "/artistPics/**").permitAll();
                    registry.requestMatchers("/admin/user_management/**").hasRole("ADMIN");
                    registry.requestMatchers("/admin/**").hasAnyRole("ADMIN", "UPLOADER");
                    registry.anyRequest().authenticated();
                })
                .build();
    }
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new CustomAuthenticationSuccessHandler();
    }
}
