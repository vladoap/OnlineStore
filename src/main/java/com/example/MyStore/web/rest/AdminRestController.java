package com.example.MyStore.web.rest;

import com.example.MyStore.model.dto.AllProductsDTO;
import com.example.MyStore.model.dto.UserDetailsDTO;
import com.example.MyStore.model.service.ProductSummaryServiceModel;
import com.example.MyStore.model.service.UserDetailsForAdminServiceModel;
import com.example.MyStore.model.view.ProductsSummaryViewModel;
import com.example.MyStore.service.PictureService;
import com.example.MyStore.service.ProductService;
import com.example.MyStore.service.UserService;
import com.example.MyStore.utils.PaginationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    private final ProductService productService;
    private final PictureService pictureService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public AdminRestController(ProductService productService, ModelMapper modelMapper, PictureService pictureService, UserService userService) {
        this.productService = productService;
        this.modelMapper = modelMapper;
        this.pictureService = pictureService;
        this.userService = userService;
    }

    @GetMapping("/products")
    public ResponseEntity<AllProductsDTO> getAllOffers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "6") int pageSize) {

        List<ProductSummaryServiceModel> productsPageable = productService.getAllProductsPageable(page, pageSize);

        int totalProducts = productService.getAllProducts().size();

        AllProductsDTO allProducts = new AllProductsDTO();
        allProducts
                .setProducts(mapProductServiceToViewModel(productsPageable))
                .setPages(PaginationUtil.getPageCount(pageSize, totalProducts));

        return ResponseEntity.ok(allProducts);
    }

    @DeleteMapping("/products/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {


        boolean isDeleted = productService.deleteProduct(id);

        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();

    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDetailsDTO>> getAllUsers(Principal principal) {

        List<UserDetailsForAdminServiceModel> usersServiceModel = userService.getAllUsersDetailsExceptOwn(principal.getName());

        List<UserDetailsDTO> usersDTO = mapUsersDetailsServiceToDTO(usersServiceModel);

        return ResponseEntity.ok(usersDTO);
    }

    @PreAuthorize("@userServiceImpl.isNotCurrentUser(#id, #principal.name)")
    @PatchMapping("/users/promote/{id}")
    public ResponseEntity<UserDetailsDTO> promoteToAdmin(@PathVariable Long id, Principal principal) {

        boolean isPromoted = userService.promoteUserToAdmin(id);

        UserDetailsDTO userDto = null;
        if (isPromoted) {
           userDto = modelMapper.map(userService.getUserDetailsById(id), UserDetailsDTO.class);
        }

        if (userDto == null) {
            return ResponseEntity.notFound().build();
        }


        return ResponseEntity.ok(userDto);
    }

    @PreAuthorize("@userServiceImpl.isNotCurrentUser(#id, #principal.name)")
    @DeleteMapping("users/delete/{id}")
    public ResponseEntity<UserDetailsDTO> deleteUser(@PathVariable Long id, Principal principal) {
       boolean isDeleted = userService.deleteUserById(id);

       if (isDeleted) {
           return ResponseEntity.noContent().build();
       }

       return ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Boolean> isCurrentLoggedUser(@PathVariable Long id, Principal principal) {

        if (userService.isNotCurrentUser(id, principal.getName())) {
            return ResponseEntity.ok(false);
        }

        return ResponseEntity.ok(true);
    }



    private List<ProductsSummaryViewModel> mapProductServiceToViewModel(List<ProductSummaryServiceModel> productsServiceModel) {
        return productsServiceModel
                .stream()
                .map(p -> {
                    ProductsSummaryViewModel mappedProduct = modelMapper.map(p, ProductsSummaryViewModel.class);
                    if (mappedProduct.getImageUrl() == null) {
                        mappedProduct.setImageUrl(pictureService.getDefaultProductPicture().getUrl());
                    }

                    return mappedProduct;
                })
                .toList();
    }

    private List<UserDetailsDTO> mapUsersDetailsServiceToDTO(List<UserDetailsForAdminServiceModel> usersServiceModel) {
         return usersServiceModel.stream().map(user -> modelMapper.map(user, UserDetailsDTO.class)).toList();
    }


}
