package khang.doan2_tnn.controllers;

import khang.doan2_tnn.repositories.userRepository;
import khang.doan2_tnn.services.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
@RestController
public class LoginController {
    @Autowired
    private khang.doan2_tnn.services.userService userService;

    @Autowired
    private userRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @GetMapping("/register")
    public ModelAndView register(ModelAndView modelAndView) {
        modelAndView.setViewName("register");
        return modelAndView;
    }
    @PostMapping("/register")
    public ModelAndView register(String fullName, String username, String password, String email) {
        ModelAndView modelAndView = new ModelAndView();
        if (username.toLowerCase().contains("admin")) {
            modelAndView.setViewName("register");
            modelAndView.addObject("errorMessage", "Tên tài khoản không được sử dụng.");
            return modelAndView;
        }
        else if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            modelAndView.setViewName("register");
            modelAndView.addObject("errorMessage", "Không được để trống các trường.");
            return modelAndView;
        }
        else if (userRepository.findByUsername(username)!= null) {
            modelAndView.setViewName("register");
            modelAndView.addObject("errorMessage", "Tên tài khoản đã tồn tại.");
            return modelAndView;
        }
        else if (userRepository.findByEmail(email)!= null) {
            modelAndView.setViewName("register");
            modelAndView.addObject("errorMessage", "Email đã tồn tại.");
            return modelAndView;
        }
        else if (password.length() < 8) {
            modelAndView.setViewName("register");
            modelAndView.addObject("errorMessage", "Mật khẩu phải có ít nhất 8 ký tự.");
            return modelAndView;
        }
        else {
        userService.saveUser(fullName, username, passwordEncoder.encode(password), email);
        return new ModelAndView("redirect:/login");
        }
    }
    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false) String error, ModelAndView modelAndView) {
        if (error != null) {
            modelAndView.addObject("errorMessage", "Tên tài khoản hoặc mật khẩu không đúng!");
        }
        modelAndView.setViewName("login");
        return modelAndView;
    }
}
