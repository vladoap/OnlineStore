package com.example.MyStore.web;

import com.example.MyStore.model.binding.ProductPurchaseBindingModel;
import com.example.MyStore.model.service.ProductDetailsServiceModel;
import com.example.MyStore.service.ProductService;
import com.example.MyStore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/purchase")
public class PurchaseProductController {

    private final ProductService productService;
    private final UserService userService;

    public PurchaseProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }


    @PreAuthorize("@productServiceImpl.isNotOwner(#principal.name, #id)")
    @PostMapping("/{id}")
    public String addProductToCart(@PathVariable Long id, @Valid ProductPurchaseBindingModel productPurchaseBindingModel,
                                   BindingResult bindingResult, RedirectAttributes redirectAttributes, Principal principal) {

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("productPurchaseBindingModel", productPurchaseBindingModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.productPurchaseBindingModel", bindingResult);

            return "redirect:/products/details/" + id;
        }

        //TODO: should i get product in UserServiceImpl
        ProductDetailsServiceModel product = productService.getProductById(id);

        int availableQuantity = productService.getAvailableQuantityById(id) - userService.getCartItemQuantityForUser(product.getId(), principal.getName());

        if (availableQuantity < 0) {
            availableQuantity = 0;
        }

        if (availableQuantity < productPurchaseBindingModel.getQuantity()) {
            redirectAttributes
                    .addFlashAttribute("invalidQuantity", true)
                    .addFlashAttribute("availableQuantity", availableQuantity);

            return "redirect:/products/details/" + id;
        }


        userService.addCartItemInUserCart(productPurchaseBindingModel.getQuantity(), principal.getName(), product);

        return "redirect:/products/all";
    }


}
