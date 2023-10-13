package com.example.MyStore.web;

import com.example.MyStore.model.CloudinaryImage;
import com.example.MyStore.model.binding.PictureAddBindingModel;
import com.example.MyStore.model.service.PictureAddServiceModel;
import com.example.MyStore.service.CloudinaryService;
import com.example.MyStore.service.PictureService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/pictures")
public class PictureController {


    private final CloudinaryService cloudinaryService;
    private final PictureService pictureService;

    public PictureController(CloudinaryService cloudinaryService, PictureService pictureService) {
        this.cloudinaryService = cloudinaryService;
        this.pictureService = pictureService;
    }

    @PreAuthorize("@productServiceImpl.isOwner(#principal.name, #id)")
    @PostMapping("/add/product/{id}")
    public String addProductsPicture(@PathVariable Long id, Principal principal, @Valid PictureAddBindingModel pictureAddBindingModel,
                                     BindingResult bindingResult, RedirectAttributes redirectAttributes) throws IOException {

        MultipartFile file = pictureAddBindingModel.getPicture();

        if (file.isEmpty() || bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("pictureModel", pictureAddBindingModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.pictureModel", bindingResult);
            return "redirect:/products/update/" + id;
        }

        String title = pictureAddBindingModel.getTitle();

        CloudinaryImage uploadedImage = cloudinaryService.upload(pictureAddBindingModel.getPicture());

        PictureAddServiceModel pictureAddServiceModel = new PictureAddServiceModel()
                .setUrl(uploadedImage.getUrl())
                .setPublicId(uploadedImage.getPublicId())
                .setTitle(title);

        pictureService.savePicture(id, pictureAddServiceModel);


        return "redirect:/products/update/" + id;
    }
}
