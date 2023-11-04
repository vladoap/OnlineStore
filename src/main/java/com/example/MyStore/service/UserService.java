package com.example.MyStore.service;

import com.example.MyStore.model.entity.Cart;
import com.example.MyStore.model.entity.Product;
import com.example.MyStore.model.entity.User;
import com.example.MyStore.model.service.*;

import java.util.List;

public interface UserService {
    boolean isUsernameFree(String username);

    void registerUser(UserRegisterServiceModel userModel);

    ShoppingCartServiceModel getCartForUser(String username);

    User findByUsername(String username);

    void addCartItemInUserCart(Integer quantity, String buyerUsername, ProductDetailsServiceModel product);


    Integer getCartItemQuantityForUser(Long productId, String username);

    User findById(Long id);

    UserDetailsServiceModel getUserByUsername(String username
    );

    boolean isPasswordCorrectForUser(String username, String password);

    void updateUserDetails(UserDetailsServiceModel userModel, String username);

    Integer getCountOfCartItemsForUser(String username);


    void deleteCartItem(Long productId, String username);

    void deleteAllCartItems(String username);

//    void updateCartItemQuantityIfLessThenCurrent(Product product, Cart cart);


    void updateShoppingCartWithProductQuantities(String username);


    void createOrderForUser(String username, UserOrderServiceModel userOrderServiceModel);

    void updatePassword(String username, String newPassword);

    List<UserDetailsForAdminServiceModel> getAllUsersDetailsExceptOwn(String username);

    void addProductForUser(ProductAddServiceModel productServiceModel, String username);

    boolean promoteUserToAdmin(Long id);

    UserDetailsForAdminServiceModel getUserDetailsById(Long id);

    boolean isNotCurrentUser(Long id, String usernameCurrentUser);

    boolean deleteUserById(Long id);

    List<User> getAllUsers();

    boolean isEmailFree(String email);

    void save(User currentUser);
}
