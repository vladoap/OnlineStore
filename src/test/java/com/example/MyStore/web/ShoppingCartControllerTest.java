package com.example.MyStore.web;

import com.example.MyStore.helper.TestDataHelper;
import com.example.MyStore.model.binding.UserOrderBindingModel;
import com.example.MyStore.model.entity.CartItem;
import com.example.MyStore.model.entity.Product;
import com.example.MyStore.model.entity.User;
import com.example.MyStore.repository.*;
import com.example.MyStore.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser("admin")
@SpringBootTest
@AutoConfigureMockMvc
class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private TestDataHelper testDataHelper;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = testDataHelper.getUser1();
        testDataHelper.initProducts();
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        pictureRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testAddProductToCartWithValidationErrors() throws Exception {
        Product product = testDataHelper.initNewProductForUser2();
        int invalidQuantity = -100;

        mockMvc
                .perform(post("/purchase/" + product.getId())
                        .param("quantity", String.valueOf(invalidQuantity))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("productPurchaseBindingModel"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.productPurchaseBindingModel"));
    }

    @Test
    public void testAddProductToCartWhenQuantityAboveTheAvailable() throws Exception {
        Product product = testDataHelper.initNewProductForUser2();
        int quantity = 150;

        Integer currentQuantityOfCartItem = testUser.getCart().getCartItems()
                .stream()
                .filter(cartItem -> cartItem.getProductId().equals(product.getId()))
                .findFirst()
                .map(CartItem::getQuantity)
                .orElse(0);

        int availableQuantity = product.getQuantity() - currentQuantityOfCartItem;

        mockMvc
                .perform(post("/purchase/" + product.getId())
                        .param("quantity", String.valueOf(quantity))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("invalidQuantity", true))
                .andExpect(flash().attribute("availableQuantity", availableQuantity));
    }

    @Test
    public void testAddProductToCartAddNewCartItem() throws Exception {
        Product product = testDataHelper.initNewProductForUser2();
        User user = userRepository.findByUsername("admin").get();
        int quantity = 20;

        mockMvc
                .perform(post("/purchase/" + product.getId())
                        .param("quantity", String.valueOf(quantity))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection());

        Integer cartItemQuantity = userService.getCartItemQuantityForUser(product.getId(), user.getUsername());

        assertEquals(quantity, cartItemQuantity);
    }

    @Test
    public void testAddProductToCartUpdateCartItemQuantity() throws Exception {
        Product product = testDataHelper.initNewProductForUser2();
        User user = userRepository.findByUsername("admin").get();
        int quantity = 20;

        mockMvc
                .perform(post("/purchase/" + product.getId())
                        .param("quantity", String.valueOf(quantity))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection());

        Integer cartItemQuantity = userService.getCartItemQuantityForUser(product.getId(), user.getUsername());

        assertEquals(quantity, cartItemQuantity);

        mockMvc
                .perform(post("/purchase/" + product.getId())
                        .param("quantity", String.valueOf(quantity))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection());

        cartItemQuantity = userService.getCartItemQuantityForUser(product.getId(), user.getUsername());

        assertEquals(quantity * 2, cartItemQuantity);
    }

    @Test
    public void testShoppingCartBuyOpenCartPage() throws Exception {
        User user = userRepository.findByUsername("admin").get();

        BigDecimal subtotal = user.getCart().getCartItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        DecimalFormat df = new DecimalFormat("0.00");
        String formattedSubtotal = df.format(subtotal);

        mockMvc
                .perform(get("/purchase/cart"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("subtotal", formattedSubtotal))
                .andExpect(model().attributeExists("cartModel"))
                .andExpect(model().attribute("userModel", hasProperty("country", equalTo(user.getAddress().getCountry()))))
                .andExpect(model().attribute("userModel", hasProperty("city", equalTo(user.getAddress().getId().getCity()))))
                .andExpect(model().attribute("userModel", hasProperty("streetName", equalTo(user.getAddress().getId().getStreetName()))))
                .andExpect(model().attribute("userModel", hasProperty("streetNumber", equalTo(user.getAddress().getId().getStreetNumber()))))
                .andExpect(model().attribute("userModel", hasProperty("recipientFirstName", equalTo(user.getFirstName()))))
                .andExpect(model().attribute("userModel", hasProperty("recipientLastName", equalTo(user.getLastName()))))
                .andExpect(model().attribute("userModel", hasProperty("recipientEmail", equalTo(user.getEmail()))))
                .andExpect(view().name("user-cart"));
    }

    @Test
    public void testShoppingCartBuyWithValidationErrors() throws Exception {
        UserOrderBindingModel orderModel = new UserOrderBindingModel()
                .setCountry("Bulgaria")
                .setCity("Varna")
                .setStreetName("")
                .setStreetNumber(102)
                .setRecipientFirstName("")
                .setRecipientLastName("Petrov")
                .setRecipientEmail("email@email.bg");

        mockMvc
                .perform(post("/purchase/cart")
                        .param("country", orderModel.getCountry())
                        .param("city", orderModel.getCity())
                        .param("streetName", orderModel.getStreetName())
                        .param("streetNumber", orderModel.getStreetNumber().toString())
                        .param("recipientFirstName", orderModel.getRecipientFirstName())
                        .param("recipientLastName", orderModel.getRecipientLastName())
                        .param("recipientEmail", orderModel.getRecipientEmail())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("userModel"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.userModel"));
    }


    @Test
    public void testShoppingCartBuy() throws Exception {
        Product product = testDataHelper.initNewProductForUser2();
        User user = userRepository.findByUsername("admin").get();
        int quantity = 20;

        mockMvc
                .perform(post("/purchase/" + product.getId())
                        .param("quantity", String.valueOf(quantity))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection());

        Integer cartItemQuantity = userService.getCartItemQuantityForUser(product.getId(), user.getUsername());
        assertEquals(quantity, cartItemQuantity);

        Integer countOfCartItemsForUserBefore = userService.getCountOfCartItemsForUser(user.getUsername());
        assertEquals(1, countOfCartItemsForUserBefore);

        UserOrderBindingModel orderModel = new UserOrderBindingModel()
                .setCountry("Bulgaria")
                .setCity("Varna")
                .setStreetName("Plovdiv")
                .setStreetNumber(102)
                .setRecipientFirstName("Peter")
                .setRecipientLastName("Petrov")
                .setRecipientEmail("email@email.bg");

        mockMvc
                .perform(post("/purchase/cart")
                        .param("country", orderModel.getCountry())
                        .param("city", orderModel.getCity())
                        .param("streetName", orderModel.getStreetName())
                        .param("streetNumber", orderModel.getStreetNumber().toString())
                        .param("recipientFirstName", orderModel.getRecipientFirstName())
                        .param("recipientLastName", orderModel.getRecipientLastName())
                        .param("recipientEmail", orderModel.getRecipientEmail())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("successful-order"));

        Integer countOfCartItemsForUserAfter = userService.getCountOfCartItemsForUser(user.getUsername());
        assertEquals(0, countOfCartItemsForUserAfter);

        Integer productQuantityAfterBuy = productRepository.findById(product.getId()).get().getQuantity();
        assertEquals(product.getQuantity() - quantity, productQuantityAfterBuy);
    }

    @Test
    public void testDeleteItemInCart() throws Exception {
        Product product = testDataHelper.initNewProductForUser2();

        User user = userRepository.findByUsername("admin").get();
        int quantity = 20;

        mockMvc
                .perform(post("/purchase/" + product.getId())
                        .param("quantity", String.valueOf(quantity))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection());

        Integer cartItemQuantity = userService.getCartItemQuantityForUser(product.getId(), user.getUsername());
        assertEquals(quantity, cartItemQuantity);

        Integer countOfCartItemsForUserBefore = userService.getCountOfCartItemsForUser(user.getUsername());
        assertEquals(1, countOfCartItemsForUserBefore);

        mockMvc
                .perform(delete("/purchase/cart/delete/" + product.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection());


        Optional<CartItem> cartItem = userRepository.findByUsername(user.getUsername()).get().getCart().getCartItems().stream().
                filter(ci -> ci.getProductId().equals(product.getId())).findFirst();

        assertTrue(cartItem.isEmpty());
    }


}