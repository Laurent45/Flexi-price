package org.capco.flexiprice.unit.controller;

import io.restassured.common.mapper.TypeRef;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.capco.flexiprice.controller.product.ProductController;
import org.capco.flexiprice.dto.ProductDTO;
import org.capco.flexiprice.enumeration.ProductType;
import org.capco.flexiprice.service.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@WebMvcTest(value = ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    ProductService productService;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void should_return_all_products() {
        // WHEN
        List<ProductDTO> products = List.of(
            new ProductDTO(ProductType.HIGH_END_PHONE, "iPhone 14 Pro Max"),
            new ProductDTO(ProductType.MID_RANGE_PHONE, "Samsung Galaxy S21"),
            new ProductDTO(ProductType.LAPTOP, "Dell XPS 13")
        );

        when(productService.getProducts()).thenReturn(products);

        List<ProductDTO> list = given()
                .when()
                .get("/api/v1/products")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(new TypeRef<List<ProductDTO>>() {});

        assertThat(list).isEqualTo(products);
    }
}
