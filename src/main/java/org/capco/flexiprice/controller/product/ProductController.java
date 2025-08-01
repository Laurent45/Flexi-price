package org.capco.flexiprice.controller.product;

import org.capco.flexiprice.dto.ProductDTO;
import org.capco.flexiprice.service.product.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    public final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDTO> getProducts() {
        LOG.info("Retrieving all products");
        return productService.getProducts();
    }
}
