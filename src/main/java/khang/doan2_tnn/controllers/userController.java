package khang.doan2_tnn.controllers;

import khang.doan2_tnn.repositories.userRepository;
import khang.doan2_tnn.services.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class userController {
    @Autowired
    private userService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public ModelAndView register(ModelAndView modelAndView) {
        modelAndView.setViewName("register");
        return modelAndView;
    }
    @PostMapping("/register")
    public ModelAndView register(String fullName, String username, String password, String email) {

        userService.saveUser(fullName, username, passwordEncoder.encode(password), email);
        return new ModelAndView("redirect:/login");
    }
    @GetMapping("/login")
    public ModelAndView login(ModelAndView modelAndView) {
        modelAndView.setViewName("login");
        return modelAndView;
    }
    @GetMapping("admin/user_management")
    public ModelAndView user_management(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/user_management");
        modelAndView.addObject("users", userService.getAllUsers());
        return modelAndView;
    }
}
