package springboot.security.controller;

import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springboot.security.model.User;
import springboot.security.service.DuplicateUsernameException;
import springboot.security.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String adminHome(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("user", new User());
        model.addAttribute("roles", userService.getAllRoles());
        model.addAttribute("activeTab", "users");
        return "admin/home";
    }

    @GetMapping("/user")
    public String userHome(Model model, Principal principal) {
        User me = userService.getByUsername(principal.getName());
        model.addAttribute("users", List.of(me));
        model.addAttribute("user", new User());
        model.addAttribute("roles", userService.getAllRoles());
        model.addAttribute("activeTab", "users");
        return "admin/home";
    }

//    @PostMapping("/save")
//    public String saveUser(@Valid @ModelAttribute("user") User user,
//                           BindingResult bindingResult,
//                           @RequestParam(value="roleIds", required=false) List<Long> roleIds,
//                           Model model) {
//
//
//        if (roleIds == null || roleIds.isEmpty()) {
//            bindingResult.reject("roles.empty", "Select at least one role");
//        }
//
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("users", userService.getAllUsers());
//            model.addAttribute("roles", userService.getAllRoles());
//            model.addAttribute("activeTab", "newUser");
//            return "admin/home";
//        }
//
//        try {
//            userService.saveUser(user, roleIds);
//        } catch (DuplicateUsernameException ex) {
//            bindingResult.rejectValue("username", "username.duplicate", ex.getMessage());
//            model.addAttribute("users", userService.getAllUsers());
//            model.addAttribute("roles", userService.getAllRoles());
//            model.addAttribute("activeTab", "newUser");
//            return "admin/home";
//        } catch (IllegalArgumentException ex) {
//
//            bindingResult.reject("save.invalid", ex.getMessage());
//            model.addAttribute("users", userService.getAllUsers());
//            model.addAttribute("roles", userService.getAllRoles());
//            model.addAttribute("activeTab", "newUser");
//            return "admin/home";
//        }
//
//        return "redirect:/admin";
//    }
//
//    @PostMapping("/delete")
//    public String deleteUser(@RequestParam("id") Long id) {
//        userService.deleteUser(id);
//        return "redirect:/admin?tab=users";
//    }

}