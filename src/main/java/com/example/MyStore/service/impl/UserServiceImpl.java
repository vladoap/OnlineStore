package com.example.MyStore.service.impl;

import com.example.MyStore.model.entity.*;
import com.example.MyStore.model.enums.UserRoleEnum;
import com.example.MyStore.model.service.*;
import com.example.MyStore.repository.UserRepository;
import com.example.MyStore.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserRoleService userRoleService;
    private final CartService cartService;
    private final AddressService addressService;
    private final OrderService orderService;
    private final ProductService productService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, UserRoleService userRoleService,
                           CartService cartService, AddressService addressService, OrderService orderService, ProductService productService,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.userRoleService = userRoleService;
        this.cartService = cartService;
        this.addressService = addressService;
        this.orderService = orderService;
        this.productService = productService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean isUsernameFree(String username) {
        return userRepository.findByUsername(username).isEmpty();
    }


    @Override
    public boolean isEmailFree(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

    @Override
    public void save(User currentUser) {
        userRepository.save(currentUser);
    }


    @Override
    public void registerUser(UserRegisterServiceModel userModel) {

        User user = modelMapper.map(userModel, User.class);
        Address address = addressService.getAddressOrCreateNewIfNotExists(userModel.getCountry(), userModel.getCity(), userModel.getStreetName(), userModel.getStreetNumber());
        UserRole roleUser = userRoleService.getUserRole();

        user
                .setPassword(passwordEncoder.encode(user.getPassword()))
                .setCreated(LocalDateTime.now());

         Cart cart = cartService.createNewCart();
         user.setCart(cart);

        user
                .setRoles(Set.of(roleUser))
                .setAddress(address);
        user.getCart().setCreated(LocalDateTime.now());


        userRepository.save(user);

    }


    @Override
    public void updateUserDetails(UserDetailsServiceModel userModel, String username) {
        Address address = addressService.getAddressOrCreateNewIfNotExists(userModel.getCountry(), userModel.getCity(), userModel.getStreetName(), userModel.getStreetNumber());
        User user = findByUsername(username);
        if (user.getProfilePicture() != userModel.getProfilePicture()) {
            user.setProfilePicture(userModel.getProfilePicture());
        }
        user
                .setEmail(userModel.getEmail())
                .setUsername(userModel.getUsername())
                .setFirstName(userModel.getFirstName())
                .setLastName(userModel.getLastName())
                .setTitle(userModel.getTitle())
                .setAddress(address);

        userRepository.save(user);
    }


    @Override
    public Integer getCountOfCartItemsForUser(String username) {
        return findByUsername(username).getCart().getCartItems().size();

    }



    @Transactional
    @Override
    public void deleteCartItem(Long productId, String username) {
        User buyer = findByUsername(username);

        cartService.deleteCartItemByProductId(productId, buyer.getCart());

        userRepository.save(buyer);
    }



    @Transactional
    @Override
    public void deleteAllCartItems(String username) {
        User user = findByUsername(username);

        cartService.deleteAllCartItems(user.getCart());

        userRepository.save(user);
    }




    @Override
    public ShoppingCartServiceModel getCartForUser(String username) {
        User user = findByUsername(username);
        ShoppingCartServiceModel shoppingCart = new ShoppingCartServiceModel();

        List<CartItemServiceModel> cartItems = user.getCart().getCartItems().stream()
                .map(cartItem -> modelMapper.map(cartItem, CartItemServiceModel.class)).toList();

        shoppingCart.setCartItems(cartItems);

        return shoppingCart;
    }


    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found!"));
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with ID: " + id + " not found!"));
    }

    @Override
    public UserDetailsServiceModel getUserByUsername(String username) {
        User user = findByUsername(username);
        UserDetailsServiceModel userServiceModel = modelMapper.map(user, UserDetailsServiceModel.class);

        AddressId userAddressId = user.getAddress().getId();
        userServiceModel.setCity(userAddressId.getCity())
                .setStreetNumber(userAddressId.getStreetNumber())
                .setStreetName(userAddressId.getStreetName());
        userServiceModel.setCountry(user.getAddress().getCountry());


        return userServiceModel;
    }

    @Override
    public boolean isPasswordCorrectForUser(String username, String password) {
        User user = findByUsername(username);
        return passwordEncoder.matches(password, user.getPassword());
    }


    @Transactional
    @Override
    public void addCartItemInUserCart(Integer quantity, String buyerUsername, ProductDetailsServiceModel product) {
        User buyer = findByUsername(buyerUsername);

        if (buyer.getCart().getCartItems() == null) {
            buyer.getCart().setCartItems(new HashSet<>());
        }

        cartService.addOrUpdateCartItem(buyer.getCart(), product, quantity);

        userRepository.save(buyer);

    }


    @Override
    public Integer getCartItemQuantityForUser(Long productId, String username) {

        return findByUsername(username).getCart().getCartItems()
                .stream()
                .filter(cartItem -> cartItem.getProductId().equals(productId))
                .findFirst()
                .map(CartItem::getQuantity)
                .orElse(0);

    }


    @Transactional
    @Override
    public void createOrderForUser(String username, UserOrderServiceModel userOrderServiceModel) {
        User user = findByUsername(username);

        if (user.getOrders() == null) {
            user.setOrders(new ArrayList<>());
        }

        Address deliveryAddress = addressService.getAddressOrCreateNewIfNotExists(userOrderServiceModel.getCountry(),
                userOrderServiceModel.getCity(), userOrderServiceModel.getStreetName(), userOrderServiceModel.getStreetNumber());

        List<OrderItem> orderItems = user.getCart().getCartItems().stream()
                .map(cartItem -> modelMapper.map(cartItem, OrderItem.class))
                .toList();

        BigDecimal totalPrice = orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order()
                .setDeliveryAddress(deliveryAddress)
                .setOrderItems(orderItems)
                .setTotalPrice(totalPrice)
                .setRecipientFirstName(userOrderServiceModel.getRecipientFirstName())
                .setRecipientLastName(userOrderServiceModel.getRecipientLastName())
                .setRecipientEmail(userOrderServiceModel.getRecipientEmail());
        order.setCreated(LocalDateTime.now());

        orderService.save(order);

        List<Order> orders = user.getOrders();
        orders.add(order);

        userRepository.save(user);

    }

    @Override
    public void updatePassword(String username, String newPassword) {
        User user = findByUsername(username);
        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }

    @Override
    public List<UserDetailsForAdminServiceModel> getAllUsersDetailsExceptOwn(String username) {
       return userRepository.findAll().stream()
                .map(this::mapUserToUserDetailsForAdminServiceModel).toList();
    }

    @Override
    public UserDetailsForAdminServiceModel getUserDetailsById(Long id) {
        return mapUserToUserDetailsForAdminServiceModel(findById(id));
    }

    @Override
    public void addProductForUser(ProductAddServiceModel productServiceModel, String username) {
        User user = findByUsername(username);
        productService.createProduct(productServiceModel, user);

        userRepository.save(user);
    }


    @Override
    public boolean promoteUserToAdmin(Long userId) {
        User user = findById(userId);
        if (user.getRoles().stream().anyMatch(r -> r.getName().equals(UserRoleEnum.ADMIN))) {
            return false;
        }
        UserRole adminRole = userRoleService.getAdminRole();
        user.getRoles().add(adminRole);

        userRepository.save(user);

        return true;
    }



    @Transactional
    @Override
    public void updateShoppingCartWithProductQuantities(String username) {
        User user = findByUsername(username);
        Cart cart = user.getCart();
        List<CartItem> cartItems = new ArrayList<>(cart.getCartItems());

        if (cartItems.size() > 0) {

            for (CartItem cartItem : cartItems) {
                Product product = productService.getProductById(cartItem.getProductId());

                if (product.getQuantity() == 0) {
                    cart.getCartItems().remove(cartItem);
                    cartService.deleteCartItem(cartItem);
                } else if (product.getQuantity() < cartItem.getQuantity()) {
                    cartItem.setQuantity(product.getQuantity());
                }
            }

            userRepository.save(user);
        }
    }



    @Override
    public boolean isNotCurrentUser(Long id, String usernameCurrentUser) {
        return !findById(id).equals(findByUsername(usernameCurrentUser));
    }

    @Override
    public boolean deleteUserById(Long userId) {
        User user = findById(userId);
        userRepository.deleteById(user.getId());

        return userRepository.findById(userId).isEmpty();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    private UserDetailsForAdminServiceModel mapUserToUserDetailsForAdminServiceModel(User user) {
        UserDetailsForAdminServiceModel userModel = new UserDetailsForAdminServiceModel();
        userModel
                .setId(user.getId())
                .setFullName(user.getFirstName() + " " + user.getLastName())
                .setProfilePicture(user.getProfilePicture().getUrl())
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .setOrdersCount(user.getOrders() == null ? 0 : user.getOrders().size())
                .setProductsCount(user.getProducts() == null ? 0 : user.getProducts().size());

        Set<UserRole> roles = user.getRoles();
        boolean isAdmin = roles.stream().anyMatch(r -> r.getName().equals(UserRoleEnum.ADMIN));
        userModel.setRole(isAdmin ? UserRoleEnum.ADMIN : UserRoleEnum.USER);

        return userModel;
    }


}
