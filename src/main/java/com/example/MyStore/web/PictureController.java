package com.example.MyStore.web;

import com.example.MyStore.model.binding.PictureAddBindingModel;
import com.example.MyStore.model.entity.Picture;
import com.example.MyStore.service.PictureService;
import com.example.MyStore.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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



    private final PictureService pictureService;
    private final ProductService productService;

    public PictureController(PictureService pictureService, ProductService productService) {

        this.pictureService = pictureService;
        this.productService = productService;
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

        Picture picture = pictureService.uploadPicture(pictureAddBindingModel.getPicture(), title);

        productService.addProductPicture(id, picture);



        return "redirect:/products/update/" + id;
    }
}
