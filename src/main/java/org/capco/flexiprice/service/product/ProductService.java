package org.capco.flexiprice.service.product;

import org.capco.flexiprice.dto.ProductDTO;
import org.capco.flexiprice.dto.ProductPriceDTO;
import org.capco.flexiprice.entity.product.Product;
import org.capco.flexiprice.enumeration.ClientType;
import org.capco.flexiprice.exception.ProductNotFoundException;
import org.capco.flexiprice.exception.ProductPriceNotFoundException;
import org.capco.flexiprice.repository.product.ProductPriceRepository;
import org.capco.flexiprice.repository.product.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * Retrieves all products from the repository and converts them to DTOs.
     *
     * @return a list of ProductDTO representing all products
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> getProducts() {
        List<Product> products = productRepository.findAll();
        return ProductDTO.from(products);
    }

/**
 * Retrieves the price for a specific product and client type.
 *
 * @param productName the name of the product to look up
 * @param clientType the type of client for which the price is requested
 * @return ProductPriceDTO containing the product name, client type, and price
 * @throws ProductNotFoundException if the product with the given name does not exist
 * @throws ProductPriceNotFoundException if no price is set for the specified client type
 */
    @Transactional(readOnly = true)
    public ProductPriceDTO getProductPrice(String productName, ClientType clientType) {
        LOG.info("Fetching price for product '{}' and client type '{}'", productName, clientType);

        if (!productRepository.existsProductByName(productName)) {
            LOG.warn("Product not found for name '{}'", productName);
            throw new ProductNotFoundException("Product not found: " + productName);
        }

        ProductPriceDTO productPriceDTO = productPriceRepository.findProductPriceByProductNameAndClientType(productName, clientType)
                .map(productPrice -> new ProductPriceDTO(productPrice.getId(), productName, clientType, productPrice.getPrice()))
                .orElseThrow(() -> {
                    LOG.error("Product \"{}\" has not price set for the client type {}", productName, clientType);
                    return new ProductPriceNotFoundException("No price found for product: " + productName + " and client type: " + clientType);
                });

        LOG.info("Retrieved price for product '{}' and client type '{}': {}$", productName, clientType, productPriceDTO.productPrice());
        return productPriceDTO;
    }
}
