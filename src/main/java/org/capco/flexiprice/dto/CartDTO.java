package org.capco.flexiprice.dto;

import org.capco.flexiprice.entity.cart.CartProductPrice;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public record CartDTO(
    Long cartId,
    List<ProductItemDTO> products,
    BigDecimal totalAmount
) {
    public record ProductItemDTO(
            String name,
            BigDecimal price,
            int quantity
    ) {}

    public static CartDTO cartProductPriceToProductDTO(Long cartId, Set<CartProductPrice> cartProductPrices, BigDecimal totalAmount) {
        List<ProductItemDTO> productItemDTOS = cartProductPrices.stream()
                .map(CartDTO::cartProductPriceToProductDTO)
                .toList();
        return new CartDTO(cartId, productItemDTOS, totalAmount);
    }

    private static ProductItemDTO cartProductPriceToProductDTO(CartProductPrice cartProductPrice) {
        return new ProductItemDTO(
                cartProductPrice.getProductPrice().getProduct().getName(),
                cartProductPrice.getProductPrice().getPrice(),
                cartProductPrice.getQuantity()
        );
    }
}
