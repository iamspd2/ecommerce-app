package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    @Before
    public void setUp () {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void modify_cart_request(){
        User user = getTestUser();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(getTestUser());
        when(cartRepository.save(any())).thenReturn(new Cart());
        Optional<Item> itemOptional = Optional.of(new Item());
        itemOptional.get().setPrice(new BigDecimal(11.0));
        when(itemRepository.findById(any())).thenReturn(itemOptional);
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername(user.getUsername());
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);


        Assertions.assertNotNull(response);
        Assertions.assertEquals(200, response.getStatusCodeValue());

        Cart cart= response.getBody();
        Assertions.assertNotNull(cart);
        Assertions.assertEquals(11, cart.getTotal().intValue());
    }

    public static User getTestUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setPassword("password");
        user.setCart(new Cart());
        return user;
    }
}
