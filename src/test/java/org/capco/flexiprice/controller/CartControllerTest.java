package org.capco.flexiprice.controller;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.capco.flexiprice.controller.cart.CartController;
import org.capco.flexiprice.dto.ProductAddToCartDTO;
import org.capco.flexiprice.service.cart.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(CartController.class)
public class CartControllerTest {

    @MockitoBean
    private CartService cartService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void should_add_product_to_cart() {
        Long cartId = 1L;
        String requestBody = """
                {
                    "name": "iPhone 14 Pro Max",
                    "quantity": 1
                }
                """;

        ProductAddToCartDTO expectedResponse = new ProductAddToCartDTO(cartId, "iPhone 14 Pro Max", 1);

        when(cartService.addProductToCart(any(Long.class), any())).thenReturn(expectedResponse);

        ProductAddToCartDTO response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/v1/carts/1/product")
                .then()
                .statusCode(200)
                .extract()
                .as(ProductAddToCartDTO.class);

        assertThat(response).isEqualTo(expectedResponse);
    }
}
