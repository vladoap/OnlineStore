package com.example.MyStore.web;

import com.example.MyStore.model.binding.UserDetailsBindingModel;
import com.example.MyStore.model.binding.UserPasswordChangeBindingModel;
import com.example.MyStore.model.entity.Picture;
import com.example.MyStore.model.entity.User;
import com.example.MyStore.model.service.UserDetailsServiceModel;
import com.example.MyStore.service.PictureService;
import com.example.MyStore.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("users/profile")
public class UserProfileController {

    private final UserService userService;
    private final PictureService pictureService;
    private final ModelMapper modelMapper;

    public UserProfileController(UserService userService, PictureService pictureService, ModelMapper modelMapper) {
        this.userService = userService;
        this.pictureService = pictureService;
        this.modelMapper = modelMapper;
    }

    @ModelAttribute
    public UserPasswordChangeBindingModel userPasswordChangeBindingModel() {
        return new UserPasswordChangeBindingModel();
    }

    @GetMapping("/update")
    public String userUpdate(Model model, Principal principal) {

        if (!model.containsAttribute("userDetails")) {

            User user = userService.findByUsername(principal.getName());

            UserDetailsServiceModel userDetailsServiceModel = userService.getUserByUsername(principal.getName());

            UserDetailsBindingModel userDetailsBindingModel = modelMapper.map(userDetailsServiceModel, UserDetailsBindingModel.class);
            userDetailsBindingModel.setProfilePicture(userDetailsServiceModel.getProfilePicture().getUrl());


            model.addAttribute("userDetails", userDetailsBindingModel);
        }

        return "user-profile";
    }

    @PatchMapping("/update")
    public String userUpdatePost(@Valid UserDetailsBindingModel userDetailsBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                 Principal principal) throws IOException {

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("userDetails", userDetailsBindingModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.userDetails", bindingResult);

            return "redirect:update";
        }

        boolean isPasswordCorrect = userService.isPasswordCorrectForUser(principal.getName(), userDetailsBindingModel.getPassword());

        if (!isPasswordCorrect) {
            redirectAttributes
                    .addFlashAttribute("userDetails", userDetailsBindingModel)
                    .addFlashAttribute("incorrectPassword", true);

            return "redirect:update";
        }

        UserDetailsServiceModel userServiceModel = modelMapper.map(userDetailsBindingModel, UserDetailsServiceModel.class);


        if (!userDetailsBindingModel.getNewProfilePicture().isEmpty()) {
            String title = userDetailsBindingModel.getNewProfilePicture().getOriginalFilename();
            Picture picture = pictureService.uploadPicture(userDetailsBindingModel.getNewProfilePicture(), title);
            userServiceModel.setProfilePicture(picture);
        } else {
            userServiceModel.setProfilePicture(pictureService.findByUrl(userDetailsBindingModel.getProfilePicture()));
        }

        userService.updateUserDetails(userServiceModel, principal.getName());

           redirectAttributes.addFlashAttribute("successMessageProfile", "Profile updated successfully");

        return "redirect:update";
    }

    @PatchMapping("/password")
    public String changeUserPassword(@Valid UserPasswordChangeBindingModel userPasswordModel, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                     Principal principal) {

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("userPasswordChangeBindingModel", userPasswordModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.userPasswordChangeBindingModel", bindingResult);

            return "redirect:update";
        }

        boolean isPasswordCorrect = userService.isPasswordCorrectForUser(principal.getName(), userPasswordModel.getOldPassword());

        if (!isPasswordCorrect) {
            redirectAttributes
                    .addFlashAttribute("userPasswordChangeBindingModel", userPasswordModel)
                    .addFlashAttribute("incorrectPasswordChange", true);
            return "redirect:update";
        }

        if (userPasswordModel.getOldPassword().equals(userPasswordModel.getNewPassword())) {
            redirectAttributes
                    .addFlashAttribute("userPasswordChangeBindingModel", userPasswordModel)
                    .addFlashAttribute("PasswordIsTheSame", true);
            return "redirect:update";
        }

        userService.updatePassword(principal.getName(), userPasswordModel.getNewPassword());

        redirectAttributes.addFlashAttribute("successMessagePassword", "Password changed successfully");
        return "redirect:update";
    }
}
