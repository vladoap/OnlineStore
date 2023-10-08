package com.example.MyStore.service.impl;

import com.example.MyStore.model.entity.Address;
import com.example.MyStore.model.entity.AddressId;
import com.example.MyStore.model.entity.User;
import com.example.MyStore.model.entity.UserRole;
import com.example.MyStore.model.service.UserRegisterServiceModel;
import com.example.MyStore.repository.UserRepository;
import com.example.MyStore.service.AddressService;
import com.example.MyStore.service.UserRoleService;
import com.example.MyStore.service.UserService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserRoleService userRoleService;
    private final AddressService addressService;
    private final UserDetailsService userDetailsService;
    private final SecurityContextRepository securityContextRepository;
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, UserRoleService userRoleService,
                           AddressService addressService, UserDetailsService userDetailsService, SecurityContextRepository securityContextRepository,
                           HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.userRoleService = userRoleService;
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
        User user = modelMapper.map(userModel, User.class);
        user
                .setPassword(passwordEncoder.encode(user.getPassword()))
                .setCreated(LocalDateTime.now());
        UserRole roleUser = userRoleService.getUserRole();
        user.setRoles(Set.of(roleUser));


        AddressId userAddressId = new AddressId(userModel.getStreetName(), userModel.getCity(), userModel.getStreetNumber());
        Optional<Address> addressOpt = addressService.findById(userAddressId);

        if (addressOpt.isPresent()) {
            user.setAddress(addressOpt.get());
        } else {
            Address userAddress = new Address()
                    .setCountry(userModel.getCountry())
                    .setId(userAddressId);

            addressService.save(userAddress);

            user.setAddress(userAddress);
        }

        userRepository.save(user);

        authenticateUser(user.getUsername());

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
}
