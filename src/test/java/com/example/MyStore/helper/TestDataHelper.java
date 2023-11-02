package com.example.MyStore.helper;


import com.example.MyStore.model.entity.*;
import com.example.MyStore.model.enums.CategoryNameEnum;
import com.example.MyStore.model.enums.TitleEnum;
import com.example.MyStore.model.enums.UserRoleEnum;
import com.example.MyStore.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class TestDataHelper {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Set<UserRole> initRoles() {
        UserRole adminRole = new UserRole()
                .setName(UserRoleEnum.ADMIN);

        UserRole userRole = new UserRole()
                .setName(UserRoleEnum.USER);

        userRoleRepository.saveAll(Set.of(adminRole, userRole));

        return Set.of(adminRole, userRole);
    }

    public User initUser1() {

        Set<UserRole> userRoles = initRoles();

        Picture picture = new Picture()
                .setTitle("profilePicture1")
                .setUrl("https://www.google.com/url?sa=i&url=https%3A%2F%2Fpixabay.com%2Fvectors%2Fblank-profile-picture-mystery-man-973460%2F&psig=AOvVaw1FfXrMLj9GvESJftkiaqBB&ust=1698972366193000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCLCrj4-LpIIDFQAAAAAdAAAAABAE")
                .setPublicId("picture1-public-id");
        picture
                .setId(1L)
                .setCreated(LocalDateTime.now());

        pictureRepository.save(picture);

        User user = new User()
                .setUsername("admin")
                .setPassword(passwordEncoder.encode("123456"))
                .setEmail("admin@admin.bg")
                .setTitle(TitleEnum.Mr)
                .setFirstName("Test")
                .setLastName("TestTest")
                .setRoles(userRoles)
                .setProfilePicture(picture);

        AddressId addressId = new AddressId("Vasil Aprilov", "Sofia", 25);
        Address address = new Address()
                .setId(addressId)
                .setCountry("Bulgaria");

        addressRepository.save(address);

        Cart cart = new Cart();
        cart
                .setCartItems(new HashSet<>())
                .setCreated(LocalDateTime.now());

        cartRepository.save(cart);

        user
                .setAddress(address)
                .setCart(cart)
                .setId(1L)
                .setCreated(LocalDateTime.now());

        userRepository.save(user);

        return user;
    }

    public User initUser2() {

        Set<UserRole> userRoles = initRoles();

        Picture picture = new Picture()
                .setTitle("profilePicture2")
                .setUrl("https://i.pinimg.com/736x/17/57/1c/17571cdf635b8156272109eaa9cb5900.jpg")
                .setPublicId("picture2-public-id");
        picture
                .setId(2L)
                .setCreated(LocalDateTime.now());

        pictureRepository.save(picture);

        User user = new User()
                .setUsername("gosho")
                .setPassword(passwordEncoder.encode("123456"))
                .setEmail("gosho@gosho.bg")
                .setTitle(TitleEnum.Mr)
                .setFirstName("Georgi")
                .setLastName("Georgiev")
                .setRoles(userRoles)
                .setProfilePicture(picture);

        AddressId addressId = new AddressId("Bulgaria", "Plovdiv", 150);
        Address address = new Address()
                .setId(addressId)
                .setCountry("Bulgaria");

        addressRepository.save(address);

        Cart cart = new Cart();
        cart
                .setCartItems(new HashSet<>())
                .setCreated(LocalDateTime.now());

        cartRepository.save(cart);

        user
                .setAddress(address)
                .setCart(cart)
                .setId(2L)
                .setCreated(LocalDateTime.now());

        userRepository.save(user);

        return user;
    }

    public List<Product> initProducts() {
        Category bookCat = new Category()
                .setName(CategoryNameEnum.Books)
                .setDescription("Book category");
        bookCat
                .setId(1L)
                .setCreated(LocalDateTime.now());

        Category electronicsCat = new Category()
                .setName(CategoryNameEnum.Electronics)
                .setDescription("Electronics category");
        electronicsCat
                .setId(2L)
                .setCreated(LocalDateTime.now());

        categoryRepository.save(bookCat);
        categoryRepository.save(electronicsCat);

        Picture picture1 = new Picture()
                .setTitle("product1")
                .setUrl("https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.pexels.com%2Fsearch%2Fproduct%2520photography%2F&psig=AOvVaw3LgFCrurwhaATFj8CZx8h9&ust=1699002974249000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCNCQqZL9pIIDFQAAAAAdAAAAABAE")
                .setPublicId("picture-product-1");
        picture1
                .setId(2L)
                .setCreated(LocalDateTime.now());

        Picture picture2 = new Picture()
                .setTitle("product2")
                .setUrl("https://expertphotography.b-cdn.net/wp-content/uploads/2018/09/product-photography-types-water-bottle.jpg")
                .setPublicId("picture-product-2");
        picture2
                .setId(3L)
                .setCreated(LocalDateTime.now());

        pictureRepository.save(picture1);
        pictureRepository.save(picture2);


        Product product1 = new Product()
                .setName("product1")
                .setDescription("Description of product 1")
                .setCategory(bookCat)
                .setPictures(List.of(picture1))
                .setSeller(initUser1())
                .setQuantity(10)
                .setPrice(BigDecimal.valueOf(12.53));
        product1
                .setId(1L)
                .setCreated(LocalDateTime.now());

        Product product2 = new Product()
                .setName("product2")
                .setDescription("Description of product 2")
                .setCategory(electronicsCat)
                .setPictures(List.of(picture2))
                .setSeller(initUser2())
                .setQuantity(150)
                .setPrice(BigDecimal.valueOf(25.25));
        product2
                .setId(2L)
                .setCreated(LocalDateTime.now());

        productRepository.save(product1);
        productRepository.save(product2);


        return List.of(product1, product2);
    }
}
