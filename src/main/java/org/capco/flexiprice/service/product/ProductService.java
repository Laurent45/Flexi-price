package org.capco.flexiprice.service.product;

import org.capco.flexiprice.dto.ProductDTO;
import org.capco.flexiprice.entity.product.Product;
import org.capco.flexiprice.entity.product.ProductPrice;
import org.capco.flexiprice.enumeration.ClientType;
import org.capco.flexiprice.exception.ProductNotFoundException;
import org.capco.flexiprice.exception.ProductPriceNotFoundException;
import org.capco.flexiprice.repository.product.ProductPriceRepository;
import org.capco.flexiprice.repository.product.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final ProductPriceRepository productPriceRepository;

    public ProductService(ProductRepository productRepository, ProductPriceRepository productPriceRepository) {
        this.productRepository = productRepository;
        this.productPriceRepository = productPriceRepository;
    }

    public List<ProductDTO> getProducts() {
        List<Product> products = productRepository.findAll();
        return ProductDTO.from(products);
    }

    public ProductPrice getProductPrice(String productName, ClientType clientType) {
        LOG.info("Fetching price for product '{}' and client type '{}'", productName, clientType);

        if (!productRepository.existsProductByName(productName)) {
            LOG.warn("Product not found for name '{}'", productName);
            throw new ProductNotFoundException("Product not found: " + productName);
        }

        ProductPrice productPrice = productPriceRepository.findProductPriceByProductNameAndClientType(productName, clientType)
                .orElseThrow(() -> {
                    LOG.error("Product \"{}\" has not price set for the client type {}", productName, clientType);
                    return new ProductPriceNotFoundException("None price is set for the client type " + clientType);
                });

        LOG.info("Retrieved price for product '{}' and client type '{}': {}$", productName, clientType, productPrice.getPrice());
        return productPrice;
    }
}
