package khang.doan2_tnn.controllers;

import khang.doan2_tnn.dto.MailBody;
import khang.doan2_tnn.entities.ForgotPassword;
import khang.doan2_tnn.entities.users;
import khang.doan2_tnn.repositories.FPRepository;
import khang.doan2_tnn.repositories.userRepository;
import khang.doan2_tnn.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@RestController
@Slf4j
@RequestMapping("/forgotPassword")
public class FPController {
    @Autowired
    private userRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private FPRepository fpRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/verifyEmail")
    public ModelAndView forgotPassword(ModelAndView modelAndView) {
        modelAndView.setViewName("/forgotPassword");
        return modelAndView;
    }
    @PostMapping("/verifyEmail")
    public ModelAndView verifyEmail(String email) {
        users user = userRepository.findByEmail(email);
        ForgotPassword forgotPassword = user.getForgotPassword();
        if (forgotPassword == null) {
            sendOTP(email);
        } else {
            if (forgotPassword.getExpirationDate().before(Date.from(Instant.now()))) {
            forgotPassword.setUser(null);
            fpRepository.save(forgotPassword);
            user.setForgotPassword(null);
            userRepository.save(user);
            fpRepository.deleteByFpId(forgotPassword.getFpId());
            sendOTP(email);
            } else {
            log.info("OTP already sent to " + email);
                }
            }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("email", email);
        modelAndView.addObject("message", "OTP sent to your email, please check your inbox");
        modelAndView.setViewName("/forgotPassword");
        return modelAndView;
        }

    private void sendOTP(String email) {
        users user = userRepository.findByEmail(email);
        int otp = generateOTP();
        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("This is the OTP for reset password: " + otp)
                .subject("OTP for Reset Password")
                .build();
        ForgotPassword forgotPassword = ForgotPassword.builder()
                .otp(otp)
                .expirationDate(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .user(user)
                .build();
        emailService.sendSimpleMessage(mailBody);
        fpRepository.save(forgotPassword);
    }

    @PostMapping("/verifyOTP/{email}/{otp}")
    public ResponseEntity<String> verifyOTP(@PathVariable String email, @PathVariable Integer otp) {
        users user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        Optional<ForgotPassword> forgotPassword = fpRepository.findByOtpAndUser(otp, user);
        if (forgotPassword.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (forgotPassword.get().getExpirationDate().before(Date.from(Instant.now()))) {
            forgotPassword.get().setUser(null);
            fpRepository.save(forgotPassword.get());
            user.setForgotPassword(null);
            userRepository.save(user);
            fpRepository.deleteByFpId(forgotPassword.get().getFpId());
            return new ResponseEntity<>("OTP expired, please request a new one", HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok("OTP verified, please reset your password");
    }

    @GetMapping("/changePassword")
    public ModelAndView changePassword(ModelAndView modelAndView, @RequestParam String email) {
        modelAndView.addObject("email", email);
        modelAndView.setViewName("/changePassword");
        return modelAndView;
    }
    @PostMapping("/changePassword")
    public ModelAndView changePassword(String email, String password) {
        password = passwordEncoder.encode(password);
        userRepository.updatePassword(email,password);
        return new ModelAndView("redirect:/login");
    }
    private Integer generateOTP() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}
