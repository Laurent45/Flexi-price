package org.capco.flexiprice.dto;

import org.capco.flexiprice.entity.product.Product;
import org.capco.flexiprice.enumeration.ProductType;

import java.util.List;

public record ProductDTO(
        ProductType productType,
        String productName
) {

    public static List<ProductDTO> from(List<Product> products) {
        return products.stream()
                .map(p -> new ProductDTO(p.getType(), p.getName()))
                .toList();
    }
}
