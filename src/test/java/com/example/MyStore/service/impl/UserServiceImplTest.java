package com.example.MyStore.service.impl;

import com.example.MyStore.model.entity.*;
import com.example.MyStore.model.enums.TitleEnum;
import com.example.MyStore.model.enums.UserRoleEnum;
import com.example.MyStore.model.service.*;
import com.example.MyStore.repository.UserRepository;
import com.example.MyStore.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private ModelMapper modelMapper = new ModelMapper();
    @Mock
    private UserRoleService userRoleService;
    @Mock
    private CartService cartService;
    @Mock
    private AddressService addressService;
    @Mock
    private OrderService orderService;
    @Mock
    private ProductService productService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    private UserService userService;

    @BeforeEach
    void init() {
        userService = new UserServiceImpl(
                userRepository, modelMapper, userRoleService, cartService, addressService,
                orderService, productService, passwordEncoder
        );
    }

    @Test
    void testUserRegistration() {

        String username = "admin";
        String testPassword = "111";
        String encodedPassword = "encoded_password";
        String email = "admin@admin.bg";
        TitleEnum title = TitleEnum.Mr;
        String firstName = "Test";
        String lastName = "TestTest";
        String country = "Bulgaria";
        String city = "Plovdiv";
        String streetName = "Bulgaria";
        Integer streetNumber = 150;

        UserRegisterServiceModel userModel = new UserRegisterServiceModel()
                .setUsername(username)
                .setTitle(title)
                .setPassword(testPassword)
                .setEmail(email)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setCountry(country)
                .setCity(city)
                .setStreetName(streetName)
                .setStreetNumber(streetNumber);

        Address address = new Address();
        UserRole userRole = new UserRole();
        Cart cart = new Cart();

        when(addressService.getAddressOrCreateNewIfNotExists(country, city, streetName, streetNumber))
                .thenReturn(address);

        when(userRoleService.getUserRole())
                .thenReturn(userRole);

        when(cartService.createNewCart())
                .thenReturn(cart);

        when(passwordEncoder.encode(testPassword))
                .thenReturn(encodedPassword);


        userService.registerUser(userModel);

        verify(userRepository).save(userArgumentCaptor.capture());

        User actualSavedUser = userArgumentCaptor.getValue();

        assertEquals(userModel.getUsername(), actualSavedUser.getUsername());
        assertEquals(encodedPassword, actualSavedUser.getPassword());
        assertEquals(userModel.getTitle(), actualSavedUser.getTitle());
        assertEquals(userModel.getEmail(), actualSavedUser.getEmail());
        assertEquals(userModel.getFirstName(), actualSavedUser.getFirstName());
        assertEquals(userModel.getLastName(), actualSavedUser.getLastName());
        assertEquals(address, actualSavedUser.getAddress());
        assertEquals(cart, actualSavedUser.getCart());
        assertTrue(actualSavedUser.getRoles().contains(userRole));

    }

    @Test
    public void testUpdateUserDetails() {

        String username = "admin";
        String email = "admin@admin.bg";
        TitleEnum title = TitleEnum.Mr;
        String firstName = "Test";
        String lastName = "TestTest";
        String country = "Bulgaria";
        String city = "Plovdiv";
        String streetName = "Bulgaria";
        Integer streetNumber = 150;

        Picture picture = new Picture();

        UserDetailsServiceModel userModel = new UserDetailsServiceModel()
                .setUsername(username)
                .setTitle(title)
                .setEmail(email)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setCountry(country)
                .setCity(city)
                .setStreetName(streetName)
                .setStreetNumber(streetNumber)
                .setProfilePicture(picture);

        User currentUser = new User();
        Address address = new Address();

        when(addressService.getAddressOrCreateNewIfNotExists(country, city, streetName, streetNumber))
                .thenReturn(address);

        when(userRepository.findByUsername(currentUser.getUsername())).thenReturn(Optional.of(currentUser));

        userService.updateUserDetails(userModel, currentUser.getUsername());

        verify(userRepository).save(userArgumentCaptor.capture());

        User actualSavedUser = userArgumentCaptor.getValue();

        assertEquals(userModel.getUsername(), actualSavedUser.getUsername());
        assertEquals(userModel.getTitle(), actualSavedUser.getTitle());
        assertEquals(userModel.getEmail(), actualSavedUser.getEmail());
        assertEquals(userModel.getFirstName(), actualSavedUser.getFirstName());
        assertEquals(userModel.getLastName(), actualSavedUser.getLastName());
        assertEquals(address, actualSavedUser.getAddress());
        assertEquals(picture, actualSavedUser.getProfilePicture());
    }

    @Test
    public void testGetCountOfCartItemsForUser() {
        User currentUser = initUser();

        when(userRepository.findByUsername(currentUser.getUsername()))
                .thenReturn(Optional.of(currentUser));

        Integer countOfItems = userService.getCountOfCartItemsForUser(currentUser.getUsername());

        assertEquals(3, countOfItems);
    }

    @Test
    public void testGetCartForUser() {

        User currentUser = initUser();

        when(userRepository.findByUsername(currentUser.getUsername()))
                .thenReturn(Optional.of(currentUser));

        ShoppingCartServiceModel shoppingCart = userService.getCartForUser(currentUser.getUsername());

        String expectedCartItemsNames = currentUser.getCart().getCartItems()
                .stream().map(CartItem::getName).collect(Collectors.joining(", "));

        String actualCartItemNames = shoppingCart.getCartItems()
                .stream().map(CartItemServiceModel::getName).collect(Collectors.joining(", "));

        assertEquals(3, shoppingCart.getCartItems().size());
        assertEquals(expectedCartItemsNames, actualCartItemNames);

    }

    @Test
    public void testGetUserByUsername() {

        User currentUser = initUser();

        when(userRepository.findByUsername(currentUser.getUsername()))
                .thenReturn(Optional.of(currentUser));

        UserDetailsServiceModel userDetailsServiceModel = userService.getUserByUsername(currentUser.getUsername());

        assertEquals(currentUser.getUsername(), userDetailsServiceModel.getUsername());
        assertEquals(currentUser.getTitle(), userDetailsServiceModel.getTitle());
        assertEquals(currentUser.getEmail(), userDetailsServiceModel.getEmail());
        assertEquals(currentUser.getFirstName(), userDetailsServiceModel.getFirstName());
        assertEquals(currentUser.getLastName(), userDetailsServiceModel.getLastName());
        assertEquals(currentUser.getAddress().getId().getCity(), userDetailsServiceModel.getCity());
        assertEquals(currentUser.getAddress().getId().getStreetName(), userDetailsServiceModel.getStreetName());
        assertEquals(currentUser.getAddress().getId().getStreetNumber(), userDetailsServiceModel.getStreetNumber());
        assertEquals(currentUser.getAddress().getCountry(), userDetailsServiceModel.getCountry());
        assertEquals(currentUser.getProfilePicture(), userDetailsServiceModel.getProfilePicture());
    }

    @Test
    public void testGetCartItemQuantityForUser() {

        User currentUser = initUser();

        when(userRepository.findByUsername(currentUser.getUsername()))
                .thenReturn(Optional.of(currentUser));

        Integer quantity = userService.getCartItemQuantityForUser(2L, currentUser.getUsername());

        assertEquals(10, quantity);
    }

    @Test
    public void testCreateOrderForUser() {

        User currentUser = initUser();
        Address address = new Address();

        String expectedOrderItems = currentUser.getCart().getCartItems().stream()
                .map(cartItem -> modelMapper.map(cartItem, OrderItem.class))
                .map(OrderItem::getName)
                .collect(Collectors.joining(", "));

        BigDecimal totalPrice = currentUser.getCart().getCartItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        UserOrderServiceModel userOrderModel = new UserOrderServiceModel()
                .setRecipientFirstName("Georgi")
                .setRecipientLastName("Georgiev")
                .setRecipientEmail("gosho@gosho.bg")
                .setCountry("Germany")
                .setCity("Munich")
                .setStreetName("Street")
                .setStreetNumber(85);

        when(userRepository.findByUsername(currentUser.getUsername()))
                .thenReturn(Optional.of(currentUser));

        when(addressService.getAddressOrCreateNewIfNotExists(
                userOrderModel.getCountry(), userOrderModel.getCity(),
                userOrderModel.getStreetName(), userOrderModel.getStreetNumber()
        )).thenReturn(address);

        userService.createOrderForUser(currentUser.getUsername(), userOrderModel);

        verify(userRepository).save(userArgumentCaptor.capture());

        User actualSavedUser = userArgumentCaptor.getValue();
        Order actualSavedUserOrder = actualSavedUser.getOrders().get(0);
        String actualOrderItems = actualSavedUserOrder.getOrderItems()
                .stream().map(OrderItem::getName).collect(Collectors.joining(", "));

        assertEquals(userOrderModel.getRecipientFirstName(), actualSavedUserOrder.getRecipientFirstName());
        assertEquals(userOrderModel.getRecipientLastName(), actualSavedUserOrder.getRecipientLastName());
        assertEquals(userOrderModel.getRecipientEmail(), actualSavedUserOrder.getRecipientEmail());
        assertEquals(address, actualSavedUserOrder.getDeliveryAddress());
        assertEquals(expectedOrderItems, actualOrderItems);
        assertEquals(totalPrice, actualSavedUserOrder.getTotalPrice());
    }

    @Test
    public void testUpdatePassword() {
        String encodedPassword = "encoded_password";
        String newPassword = "admin123";

        User currentUser = new User()
                .setUsername("admin")
                .setPassword("123456");

        when(userRepository.findByUsername(currentUser.getUsername()))
                .thenReturn(Optional.of(currentUser));

        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);

        userService.updatePassword(currentUser.getUsername(), newPassword);

        verify(userRepository).save(userArgumentCaptor.capture());

        User actualSavedUser = userArgumentCaptor.getValue();

        assertEquals(currentUser.getUsername(), actualSavedUser.getUsername());
        assertEquals(encodedPassword, actualSavedUser.getPassword());
    }

    @Test
    public void testPromoteUserToAdminWhenAlreadyIsAdmin() {
        User user = initUser();

        UserRole adminRole = new UserRole()
                .setName(UserRoleEnum.ADMIN);

        UserRole userRole = new UserRole()
                .setName(UserRoleEnum.USER);

        user.setRoles(Set.of(adminRole, userRole));

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        boolean isPromoted = userService.promoteUserToAdmin(user.getId());

        assertFalse(isPromoted);
    }

    @Test
    public void testPromoteUserToAdminWhenNotAdmin() {
        User user = initUser();


        UserRole userRole = new UserRole()
                .setName(UserRoleEnum.USER);

        user.setRoles(new HashSet<>(Set.of(userRole)));

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        when(userRoleService.getAdminRole())
                .thenReturn(new UserRole().setName(UserRoleEnum.ADMIN));

        boolean isPromoted = userService.promoteUserToAdmin(user.getId());

        verify(userRepository).save(userArgumentCaptor.capture());
        User actualSavedUser = userArgumentCaptor.getValue();

        String actualRoles = actualSavedUser.getRoles().stream().map(role -> role.getName().name())
                        .collect(Collectors.joining(", "));

        assertTrue(isPromoted);
        assertTrue(actualRoles.contains("ADMIN"));
    }


    @Test
    public void testUpdateShoppingCartWithProductQuantitiesWhenQuantityIsZeroShouldRemoveCartItem() {
        User user = initUser();

        Product product1 = new Product()
                .setQuantity(0);
        product1.setId(1L);

        Product product2 = new Product()
                .setQuantity(50);
        product2.setId(2L);

        Product product3 = new Product()
                .setQuantity(23);
        product3.setId(3L);

        when(productService.getProductById(1L)).thenReturn(product1);
        when(productService.getProductById(2L)).thenReturn(product2);
        when(productService.getProductById(3L)).thenReturn(product3);

        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        assertEquals(3, user.getCart().getCartItems().size());

        userService.updateShoppingCartWithProductQuantities(user.getUsername());

        verify(cartService).deleteCartItem(any());
        verify(userRepository).save(userArgumentCaptor.capture());
        User actualSavedUser = userArgumentCaptor.getValue();

        assertEquals(2, actualSavedUser.getCart().getCartItems().size());
        assertFalse(actualSavedUser.getCart().getCartItems().stream()
                .anyMatch(cartItem -> cartItem.getProductId().equals(1L) && cartItem.getName().equals("item1")));
    }

    @Test
    public void testUpdateShoppingCartWithProductQuantitiesWhenProductQuantityIsLessShouldUpdate() {
        User user = initUser();

        Product product1 = new Product()
                .setQuantity(2);
        product1.setId(1L);

        Product product2 = new Product()
                .setQuantity(50);
        product2.setId(2L);

        Product product3 = new Product()
                .setQuantity(23);
        product3.setId(3L);

        when(productService.getProductById(1L)).thenReturn(product1);
        when(productService.getProductById(2L)).thenReturn(product2);
        when(productService.getProductById(3L)).thenReturn(product3);

        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        userService.updateShoppingCartWithProductQuantities(user.getUsername());

        verify(userRepository).save(userArgumentCaptor.capture());

        User actualSavedUser = userArgumentCaptor.getValue();
        CartItem updatedCartItem = actualSavedUser.getCart().getCartItems()
                        .stream().filter(cartItem -> cartItem.getProductId().equals(1L)).findFirst().get();

        assertEquals(3, actualSavedUser.getCart().getCartItems().size());
        assertEquals(product1.getQuantity(), updatedCartItem.getQuantity());
    }



    private static User initUser() {
        User user = new User()
                .setUsername("admin")
                .setEmail("admin@admin.bg")
                .setTitle(TitleEnum.Mr)
                .setFirstName("Test")
                .setLastName("TestTest");

        AddressId addressId = new AddressId("Bulgaria", "Plovdiv", 150);
        Address address = new Address()
                .setId(addressId)
                .setCountry("Bulgaria");

        Picture picture = new Picture()
                .setTitle("profilePicture");

        Cart cart = new Cart();
        CartItem cartItem1 = new CartItem()
                .setName("item1")
                .setProductId(1L)
                .setQuantity(5)
                .setPrice(BigDecimal.valueOf(50.25));
        CartItem cartItem2 = new CartItem()
                .setName("item2")
                .setProductId(2L)
                .setQuantity(10)
                .setPrice(BigDecimal.valueOf(99.99));
        CartItem cartItem3 = new CartItem()
                .setName("item3")
                .setProductId(3L)
                .setQuantity(20)
                .setPrice(BigDecimal.valueOf(25));

        cart.setCartItems(new HashSet<>(Set.of(cartItem1, cartItem2, cartItem3)));

        user
                .setAddress(address)
                .setProfilePicture(picture)
                .setCart(cart)
                .setId(1L);

        return user;
    }


    private UserRole createUserRole() {
        return new UserRole().setName(UserRoleEnum.USER);
    }

    private Address createAddress(String country, String city, String streetName, Integer streetNumber) {
        AddressId userAddressId = new AddressId(streetName, city, streetNumber);
        return new Address()
                .setCountry(country)
                .setId(userAddressId);
    }


}