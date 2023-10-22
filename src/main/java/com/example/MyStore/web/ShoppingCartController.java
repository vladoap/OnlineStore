package com.example.MyStore.web;

import com.example.MyStore.model.binding.ProductPurchaseBindingModel;
import com.example.MyStore.model.service.UserOrderServiceModel;
import com.example.MyStore.model.service.ProductDetailsServiceModel;
import com.example.MyStore.model.service.ShoppingCartServiceModel;
import com.example.MyStore.model.service.UserDetailsServiceModel;
import com.example.MyStore.model.view.CartItemViewModel;
import com.example.MyStore.model.view.ShoppingCartViewModel;
import com.example.MyStore.model.binding.UserOrderBindingModel;
import com.example.MyStore.service.OrderService;
import com.example.MyStore.service.ProductService;
import com.example.MyStore.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.text.DecimalFormat;
import java.util.*;


@Controller
@RequestMapping("/purchase")
public class ShoppingCartController {

    private final ProductService productService;
    private final OrderService orderService;
    private final ModelMapper modelMapper;
    private final UserService userService;


    public ShoppingCartController(ProductService productService, OrderService orderService, ModelMapper modelMapper, UserService userService) {
        this.productService = productService;
        this.orderService = orderService;
        this.modelMapper = modelMapper;
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


        ProductDetailsServiceModel product = productService.getById(id);

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

    @GetMapping("/cart")
    public String shoppingCartBuy(Principal principal, Model model) {

        ShoppingCartServiceModel cartServiceModel = userService.getCartForUser(principal.getName());

        ShoppingCartViewModel cartViewModel = mapCartServiceToViewModel(cartServiceModel);

        BigDecimal subtotal = cartViewModel.getCartItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        DecimalFormat df = new DecimalFormat("0.00");
        String formattedSubtotal = df.format(subtotal);

        model.addAttribute("subtotal", formattedSubtotal);
        model.addAttribute("cartModel", cartViewModel);

        if (!model.containsAttribute("userModel")) {
            UserDetailsServiceModel userServiceModel = userService.getUserByUsername(principal.getName());

            UserOrderBindingModel userModel = modelMapper.map(userServiceModel, UserOrderBindingModel.class);
            userModel
                    .setRecipientFirstName(userServiceModel.getFirstName())
                    .setRecipientLastName(userServiceModel.getLastName())
                    .setRecipientEmail(userServiceModel.getEmail());

            model.addAttribute("userModel", userModel);
        }


        return "user-cart";
    }


    @PostMapping("/cart")
    public String shoppingCartBuyPost(Principal principal, String subtotal, @Valid UserOrderBindingModel userModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("userModel", userModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.userModel", bindingResult);

            return "redirect:cart";
        }


        UserOrderServiceModel userOrderServiceModel = modelMapper.map(userModel, UserOrderServiceModel.class);
        userService.createOrderForUser(principal.getName(), userOrderServiceModel);


        productService.reduceProductsQuantityByUser(userService.findByUsername(principal.getName()));

        userService.deleteAllCartItems(principal.getName());


        return "successful-order";
    }


    @DeleteMapping("/cart/delete/{id}")
    public String deleteItemInCart(@PathVariable Long id, Principal principal) {
        userService.deleteCartItem(id, principal.getName());


        return "redirect:/purchase/cart";
    }

    private ShoppingCartViewModel mapCartServiceToViewModel(ShoppingCartServiceModel cartServiceModel) {
        ShoppingCartViewModel cartViewModel = new ShoppingCartViewModel();

        List<CartItemViewModel> cartItemViewModel = cartServiceModel.getCartItems().stream()
                .map(cartItem -> modelMapper.map(cartItem, CartItemViewModel.class)).toList();

        cartViewModel.setCartItems(cartItemViewModel);

        return cartViewModel;
    }


}
