package org.capco.flexiprice.unit.service.product;

import org.capco.flexiprice.dto.ProductDTO;
import org.capco.flexiprice.dto.ProductPriceDTO;
import org.capco.flexiprice.entity.product.Product;
import org.capco.flexiprice.entity.product.ProductPrice;
import org.capco.flexiprice.enumeration.ClientType;
import org.capco.flexiprice.enumeration.ProductType;
import org.capco.flexiprice.exception.ProductNotFoundException;
import org.capco.flexiprice.exception.ProductPriceNotFoundException;
import org.capco.flexiprice.repository.product.ProductPriceRepository;
import org.capco.flexiprice.repository.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private org.capco.flexiprice.service.product.ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductPriceRepository productPriceRepository;

    @Test
    void should_get_all_products() {
        // GIVEN
        List<Product> products = List.of(
                new Product(1L, ProductType.HIGH_END_PHONE, "iPhone 14 Pro Max"),
                new Product(2L, ProductType.MID_RANGE_PHONE, "Samsung Galaxy S23")
        );

        List<ProductDTO> productDTOExpected = List.of(
                new ProductDTO(ProductType.HIGH_END_PHONE, "iPhone 14 Pro Max"),
                new ProductDTO(ProductType.MID_RANGE_PHONE, "Samsung Galaxy S23")
        );

        when(productRepository.findAll()).thenReturn(products);

        // WHEN
        List<ProductDTO> productsResponse = productService.getProducts();

        // THEN
        assertThat(productsResponse).isEqualTo(productDTOExpected);
    }

    @Test
    void should_get_product_price_by_product_name_and_client_type() {
        // GIVEN
        String productName = "iPhone 14 Pro Max";
        ClientType clientType = ClientType.PROFESSIONAL_REVENUE_LT_10M;

        ProductPrice productPrice = new ProductPrice(1L, clientType, BigDecimal.valueOf(999.99));

        ProductPriceDTO productPriceDTOExpected = new ProductPriceDTO(1L, productName, clientType, BigDecimal.valueOf(999.99));


        when(productRepository.existsProductByName(productName)).thenReturn(true);
        when(productPriceRepository.findProductPriceByProductNameAndClientType(productName, clientType))
                .thenReturn(Optional.of(productPrice));

        // WHEN
        ProductPriceDTO productPriceResponse = productService.getProductPrice(productName, clientType);

        // THEN
        assertThat(productPriceResponse).isEqualTo(productPriceDTOExpected);
    }

    @Test
    void throw_product_not_found_exception_when_product_name_does_not_exist() {
        // GIVEN
        String productName = "Nonexistent Product";
        ClientType clientType = ClientType.PROFESSIONAL_REVENUE_LT_10M;

        when(productRepository.existsProductByName(productName)).thenReturn(false);

        // WHEN
        assertThatThrownBy(() -> productService.getProductPrice(productName, clientType))
                // THEN
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("Product not found: " + productName);
    }

    @Test
    void throw_product_price_not_found_exception_when_no_price_for_client_type() {
        // GIVEN
        String productName = "iPhone 14 Pro Max";
        ClientType clientType = ClientType.PROFESSIONAL_REVENUE_LT_10M;

        when(productRepository.existsProductByName(productName)).thenReturn(true);
        when(productPriceRepository.findProductPriceByProductNameAndClientType(productName, clientType))
                .thenReturn(Optional.empty());

        // WHEN
        assertThatThrownBy(() -> productService.getProductPrice(productName, clientType))
                // THEN
                .isInstanceOf(ProductPriceNotFoundException.class)
                .hasMessageContaining("No price found for product: " + productName + " and client type: " + clientType);
    }
}
