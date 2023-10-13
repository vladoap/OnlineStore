package com.example.MyStore.service.impl;

import com.example.MyStore.model.entity.*;
import com.example.MyStore.model.service.ProductDetailsServiceModel;
import com.example.MyStore.model.service.UserDetailsServiceModel;
import com.example.MyStore.model.service.UserRegisterServiceModel;
import com.example.MyStore.repository.UserRepository;
import com.example.MyStore.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserRoleService userRoleService;
    private final CartItemService cartItemService;
    private final AddressService addressService;
    private final UserDetailsService userDetailsService;
    private final SecurityContextRepository securityContextRepository;
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, UserRoleService userRoleService,
                           CartItemService cartItemService, AddressService addressService, UserDetailsService userDetailsService, SecurityContextRepository securityContextRepository,
                           HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.userRoleService = userRoleService;
        this.cartItemService = cartItemService;
        this.addressService = addressService;
        this.userDetailsService = userDetailsService;
        this.securityContextRepository = securityContextRepository;
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean isUsernameFree(String username) {
        return userRepository.findByUsername(username).isEmpty();
    }

    @Override
    public void registerUser(UserRegisterServiceModel userModel) {
        Address address = getAddressOrCreateNewIfNotExists(userModel.getCountry(), userModel.getCity(), userModel.getStreetName(), userModel.getStreetNumber());
        User user = modelMapper.map(userModel, User.class);
        user
                .setPassword(passwordEncoder.encode(user.getPassword()))
                .setCreated(LocalDateTime.now());
        UserRole roleUser = userRoleService.getUserRole();
        user
                .setRoles(Set.of(roleUser))
                .setCart(new Cart().setCartItems(new HashSet<>()))
                .setAddress(address);
        user.getCart().setCreated(LocalDateTime.now());


        userRepository.save(user);

        authenticateUser(user.getUsername());
    }



    @Override
    public void updateUserDetails(UserDetailsServiceModel userModel, String username) {
        Address address = getAddressOrCreateNewIfNotExists(userModel.getCountry(), userModel.getCity(), userModel.getStreetName(), userModel.getStreetNumber());
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

    //TODO: should it be private method that returns UserEntity
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found!"));
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


    @Override
    @Transactional
    public void addCartItemInUserCart(Integer quantity, String buyerUsername, ProductDetailsServiceModel product) {
        User buyer = findByUsername(buyerUsername);

        if (buyer.getCart().getCartItems() == null) {
            buyer.getCart().setCartItems(new HashSet<>());
        }


        Set<CartItem> cartItems = buyer.getCart().getCartItems();

        Optional<CartItem> cartItemOpt = cartItems
                .stream()
                .filter(cartItem -> cartItem.getProductId().equals(product.getId()))
                .findFirst();

        if (cartItemOpt.isEmpty()) {
            CartItem cartItemToAdd = cartItemService.createNewCartItem(quantity, product);
            cartItems.add(cartItemToAdd);

        } else {
            CartItem existingCartItem = cartItemOpt.get();
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
        }

        userRepository.save(buyer);
    }


    @Override
    public Integer getCartItemQuantityForUser(Long productId, String username) {
        initializeUserCart(username);

        return findByUsername(username).getCart().getCartItems()
                .stream()
                .filter(cartItem -> cartItem.getProductId().equals(productId))
                .findFirst()
                .map(CartItem::getQuantity)
                .orElse(0);
    }


    private Address getAddressOrCreateNewIfNotExists(String country, String cityName, String streetName, Integer streetNumber) {
        AddressId userAddressId = new AddressId(streetName, cityName, streetNumber);
        Optional<Address> addressOpt = addressService.findById(userAddressId);

        if (addressOpt.isPresent()) {
            return addressOpt.get();
        } else {
            Address userAddress = new Address()
                    .setCountry(country)
                    .setId(userAddressId);

            addressService.save(userAddress);

            return userAddress;
        }
    }

    private void authenticateUser(String username) {
        UserDetails principal = userDetailsService.loadUserByUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal,
                principal.getPassword(),
                principal.getAuthorities()
        );

        SecurityContextHolderStrategy strategy = SecurityContextHolder.getContextHolderStrategy();

        SecurityContext context = strategy.getContext();

        context.setAuthentication(authentication);

        strategy.setContext(context);

        securityContextRepository.saveContext(context, httpServletRequest, httpServletResponse);
    }

    private void initializeUserCart(String username) {
        User user = findByUsername(username);

        if (user.getCart() == null) {
            user.setCart(new Cart().setCartItems(new HashSet<>()));
            user.getCart().setCreated(LocalDateTime.now());
            userRepository.save(user);
        }
    }


}
