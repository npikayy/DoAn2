package khang.doan2_tnn.controllers;

import jakarta.servlet.http.HttpSession;
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
        modelAndView.setViewName("verifyEmail");
        return modelAndView;
    }
    @PostMapping("/verifyEmail")
    public ModelAndView verifyEmail(String email) {
        ModelAndView modelAndView = new ModelAndView();
        users user = userRepository.findByEmail(email);
        if (user == null) {
            modelAndView.addObject("message", "Email không tồn tại, vui lòng kiểm tra lại.");
            modelAndView.setViewName("verifyEmail");
            return modelAndView;
        }
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
        modelAndView.addObject("email", email);
        modelAndView.addObject("message", "Mã OTP vừa được gửi đến email của bạn, mã có hiệu lực trong vòng 10 phút, vui lòng kiểm tra hộp thư đến hoặc spam để nhận mã OTP.");
        modelAndView.setViewName("verifyEmail");
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
    @GetMapping("/verifyOTP")
    public ModelAndView verifyOTP(ModelAndView modelAndView, @RequestParam String email) {
        modelAndView.addObject("email", email);
        modelAndView.setViewName("verifyOTP");
        return modelAndView;
    }
    @PostMapping("/verifyOTP")
    public ModelAndView verifyOTP(@RequestParam String email, @RequestParam Integer otp, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        users user = userRepository.findByEmail(email);
        Optional<ForgotPassword> forgotPassword = fpRepository.findByOtpAndUser(otp, user);
        if (forgotPassword.isEmpty()) {
            modelAndView.addObject("email", email);
            modelAndView.addObject("message", "Mã OTP không đúng, vui lòng nhập lại.");
            modelAndView.setViewName("verifyOTP");
            return modelAndView;
        }
        if (forgotPassword.get().getExpirationDate().before(Date.from(Instant.now()))) {
            modelAndView.addObject("email", email);
            modelAndView.addObject("message", "Mã OTP đã hết hạn, vui lòng yêu cầu gửi lại mã OTP.");
            modelAndView.setViewName("verifyOTP");
            return modelAndView;
        }
        // Lưu trạng thái vào session
        session.setAttribute("otpVerified", true);

        return new ModelAndView("redirect:/forgotPassword/changePassword?email=" + email);
    }

    @GetMapping("/changePassword")
    public ModelAndView changePassword(ModelAndView modelAndView, @RequestParam String email, HttpSession session) {
        Boolean otpVerified = (Boolean) session.getAttribute("otpVerified");
        if (otpVerified == null || !otpVerified) {
            return new ModelAndView("redirect:/forgotPassword/verifyEmail");
        }
        modelAndView.addObject("email", email);
        modelAndView.setViewName("/changePassword");
        return modelAndView;
    }
    @PostMapping("/changePassword")
    public ModelAndView changePassword(String email, String password, HttpSession session) {
        if (password.length() < 8) {
            ModelAndView modelAndView = new ModelAndView("/changePassword");
            modelAndView.addObject("email", email);
            modelAndView.addObject("message", "Mật khẩu phải dài ít nhất 8 ký tự");
            return modelAndView;
        }
        // Xóa trạng thái OTP trong session
        session.removeAttribute("otpVerified");

        users user = userRepository.findByEmail(email);
        ForgotPassword forgotPassword = fpRepository.findByUser(user);
        if (forgotPassword != null) {
            forgotPassword.setUser(null);
            fpRepository.save(forgotPassword);
            user.setForgotPassword(null);
            userRepository.save(user);
            fpRepository.deleteByFpId(forgotPassword.getFpId());
        }

        password = passwordEncoder.encode(password);
        userRepository.updatePassword(email,password);
        return new ModelAndView("redirect:/login");
    }
    private Integer generateOTP() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}
