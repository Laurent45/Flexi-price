package org.capco.flexiprice.controller;

import jakarta.validation.Valid;
import org.capco.flexiprice.dto.CartDTO;
import org.capco.flexiprice.dto.ProductAddRequestDTO;
import org.capco.flexiprice.dto.ProductAddToCartDTO;
import org.capco.flexiprice.entity.cart.Cart;
import org.capco.flexiprice.entity.cart.CartProductPrice;
import org.capco.flexiprice.service.cart.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {

    private static final Logger LOG = LoggerFactory.getLogger(CartController.class);

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/{cartId}/product")
    public ProductAddToCartDTO addProductToCart(@PathVariable Long cartId, @RequestBody @Valid ProductAddRequestDTO requestBody) {
        LOG.info("Received request to add product to cart. {}, {}", cartId, requestBody);
        CartProductPrice cartProductPrice = cartService.addProductToCart(cartId, requestBody);
        return new ProductAddToCartDTO(cartId, requestBody.name(), cartProductPrice.getQuantity());
    }

    @GetMapping("/{cartId}")
    public CartDTO getCart(@PathVariable Long cartId) {
        LOG.info("Received request to get product from cart {} and total price.", cartId);
        Cart cart = cartService.getCartById(cartId);

        BigDecimal totalAmount = cartService.calculateTotalAmount(cart);
        return CartDTO.cartProductPriceToProductDTO(cartId, cart.getCartProductPrices(), totalAmount);
    }
}
