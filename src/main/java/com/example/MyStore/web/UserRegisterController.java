package com.example.MyStore.web;

import com.example.MyStore.model.binding.UserRegisterBindingModel;
import com.example.MyStore.model.service.UserRegisterServiceModel;
import com.example.MyStore.service.PictureService;
import com.example.MyStore.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserRegisterController {

    private final UserService userService;
    private final PictureService pictureService;
    private final ModelMapper modelMapper;

    public UserRegisterController(UserService userService, PictureService pictureService, ModelMapper modelMapper) {
        this.userService = userService;
        this.pictureService = pictureService;
        this.modelMapper = modelMapper;
    }

    @ModelAttribute
    public UserRegisterBindingModel userRegisterBindingModel() {
        return new UserRegisterBindingModel();
    }

    @GetMapping("/register")
    public String register() {
        return "auth-register";
    }

    @PostMapping("/register")
    public String registerPost(@Valid UserRegisterBindingModel userRegisterBindingModel,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes,
                               HttpServletRequest request, HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.userRegisterBindingModel", bindingResult);
            return "redirect:register";
        }

        UserRegisterServiceModel userServiceModel = modelMapper.map(userRegisterBindingModel, UserRegisterServiceModel.class);
        userServiceModel.setProfilePicture(pictureService.getDefaultProfilePicture());
        userService.registerUser(userServiceModel);


     return "redirect:/home";
    }
}
