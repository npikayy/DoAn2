package khang.doan2_tnn.controllers;

import jakarta.transaction.Transactional;
import khang.doan2_tnn.entities.users;
import khang.doan2_tnn.services.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/admin/user_management")
public class userController {
    @Autowired
    private userService userService;

    @GetMapping()
    public ModelAndView user_management(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/user/user_management");
        modelAndView.addObject("users", userService.getAllUsers());
        return modelAndView;
    }
    @GetMapping("/admin_management")
    public ModelAndView admin_management(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/user/admin_management");
        modelAndView.addObject("users", userService.getAllAdmins());
        return modelAndView;
    }
    @Transactional()
    @GetMapping("/deleteUser")
    public ModelAndView deleteSong(@RequestParam String userId)
    {
       userService.deleteById(userId);
        return new ModelAndView("redirect:/admin/user_management");
    }

    @GetMapping("/updateUserRole")
    public ModelAndView updateUser(ModelAndView modelAndView, @RequestParam String userId)
    {
        modelAndView.setViewName("/admin/user/updateUserRole");
        users user = userService.findByUserId(userId);
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @PostMapping("/updateUserRole")
    public ModelAndView updateUserRole(@RequestParam String userId, String role)
    {
        userService.updateUser(role, userId);
        return new ModelAndView("redirect:/admin/user_management");
    }
    @GetMapping("/SearchUsers")
    public ModelAndView SearchUsers(ModelAndView modelAndView,
                                    @RequestParam(required = false) String fullName,
                                    @RequestParam(required = false) String email){
        List<users> users = userService.searchUsers(fullName, email);
        modelAndView.setViewName("/admin/user/searchPage");
        modelAndView.addObject("users", users);
        return modelAndView;
    }
}
