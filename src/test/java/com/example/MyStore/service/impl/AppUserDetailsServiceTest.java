package com.example.MyStore.service.impl;

import com.example.MyStore.model.entity.User;
import com.example.MyStore.model.entity.UserRole;
import com.example.MyStore.model.enums.TitleEnum;
import com.example.MyStore.model.enums.UserRoleEnum;
import com.example.MyStore.repository.UserRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AppUserDetailsServiceTest {

    private User testUser;
    private UserRole adminRole;
    private UserRole userRole;

    private AppUserDetailsService serviceToTest;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void init() {
        serviceToTest = new AppUserDetailsService(mockUserRepository);

        adminRole = new UserRole()
                .setName(UserRoleEnum.ADMIN);

        userRole = new UserRole()
                .setName(UserRoleEnum.USER);

        testUser = new User()
                .setUsername("vlado")
                .setPassword("111111")
                .setFirstName("Vladimir")
                .setLastName("Apostolov")
                .setEmail("vlado@vlado.bg")
                .setTitle(TitleEnum.Mr)
                .setRoles(Set.of(adminRole, userRole));
    }

    @Test
    void testUserNotFound() {
        assertThrows(UsernameNotFoundException.class,
                () -> serviceToTest.loadUserByUsername("invalid_username"));
    }

    @Test
    void testUserFound() {

        Mockito.when(mockUserRepository.findByUsername(testUser.getUsername()))
                .thenReturn(Optional.of(testUser));

        UserDetails actual = serviceToTest.loadUserByUsername(testUser.getUsername());

        String expectedRoles = "ROLE_ADMIN, ROLE_USER";
        String actualRoles = actual.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(", "));

        assertEquals(testUser.getUsername(), actual.getUsername());
        assertEquals(expectedRoles, actualRoles);


    }


}