package ru.urfu.courseProject.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.urfu.courseProject.dto.UserDto;
import ru.urfu.courseProject.entity.User;
import ru.urfu.courseProject.repository.UserRepository;
import ru.urfu.courseProject.service.GetRoleService;
import ru.urfu.courseProject.service.GetUsernameService;
import ru.urfu.courseProject.service.UserService;

import java.util.List;

@Controller
public class SecurityController {

    private UserService userService;
    private UserRepository userRepository;
    private GetUsernameService getUsernameService;
    private GetRoleService getRoleService;

    public SecurityController(UserService userService, UserRepository userRepository,
                              GetUsernameService getUsernameService,
                              GetRoleService getRoleService) {
        this.getUsernameService = getUsernameService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.getRoleService = getRoleService;
    }

    @GetMapping("/index")
    public String home() {
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about-form";
    }

    @GetMapping("/login")
    public String login() {
        if (userRepository.findByEmail("admin@gmail.com") == null) {
            setAdminAccount();
        }
        return "login";
    }

    private void setAdminAccount()
    {
        UserDto userDto = new UserDto();
        userDto.setRole("ROLE_ADMIN");
        userDto.setLastName("admin");
        userDto.setFirstName("admin");
        userDto.setEmail("admin@gmail.com");
        userDto.setPassword("1");
        userService.saveUser(userDto);
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model) {
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null,
                    "На этот адрес электронной почты уже зарегистрирована учетная запись.");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/register";
        }
        userDto.setRole("ROLE_READONLY");
        userService.saveUser(userDto);
        return "redirect:/register?success";
    }

    @GetMapping("/users")
    public String users(Model model) {
        User user = userService.findUserByEmail(getUsernameService.getusername());
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("mainUserRole", getRoleService.getRoleCurrentUser());
        model.addAttribute("users", users);
        return "users";
    }

    @PostMapping("/saveRole")
    public String saveRole(@ModelAttribute UserDto userDto)
    {
        String r = userDto.getRole();
        userService.saveUserRole(userDto);
        return "redirect:/users";
    }
    @GetMapping("/addNewRole")
    public ModelAndView addNewRole(@RequestParam String userEmail)
    {
        ModelAndView mav = new ModelAndView("add-new-role");
        UserDto user = userService.mapToUserDto(userService.findUserByEmail(userEmail));
        mav.addObject("user", user);
        return mav;
    }
}
