package org.capco.flexiprice.service.cart;

import org.capco.flexiprice.dto.ProductAddRequestDTO;
import org.capco.flexiprice.entity.cart.Cart;
import org.capco.flexiprice.entity.cart.CartProductPrice;
import org.capco.flexiprice.entity.cart.CartProductPriceKey;
import org.capco.flexiprice.entity.product.ProductPrice;
import org.capco.flexiprice.exception.CartNotFoundException;
import org.capco.flexiprice.repository.cart.CartProductPriceRepository;
import org.capco.flexiprice.repository.cart.CartRepository;
import org.capco.flexiprice.service.product.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
public class CartService {

    private static final Logger LOG = LoggerFactory.getLogger(CartService.class);

    private final CartRepository cartRepository;
    private final CartProductPriceRepository cartProductPriceRepository;

    private final ProductService productService;

    public CartService(CartRepository cartRepository, CartProductPriceRepository cartProductPriceRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.cartProductPriceRepository = cartProductPriceRepository;
        this.productService = productService;
    }

    public CartProductPrice addProductToCart(Long cartId, ProductAddRequestDTO productRequest) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> {
                    LOG.warn("Cart with id {} not found", cartId);
                    return new CartNotFoundException("Cart with id " + cartId + " not found");
                });

        ProductPrice productPrice = productService.getProductPrice(productRequest.name(), cart.getClient().getClientType());

        CartProductPriceKey cartProductPriceKey = new CartProductPriceKey(cartId, productPrice.getId());
        CartProductPrice cartProductPrice = cartProductPriceRepository.findCartProductPriceByCartProductPriceKey(cartProductPriceKey)
                .orElse(new CartProductPrice(cart, productPrice, 0));

        cartProductPrice.addQuantity(productRequest.quantity());

        return cartProductPriceRepository.save(cartProductPrice);
    }

    public Cart getCartById(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> {
                    LOG.warn("Cart with id {} not found", cartId);
                    return new CartNotFoundException("Cart with id " + cartId + " not found");
                });
    }

    public BigDecimal calculateTotalAmount(Cart cart) {
        return cart.getCartProductPrices().stream()
                .map(this::getProductPriceInCart)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getProductPriceInCart(CartProductPrice cartProductPrice) {
        BigDecimal price = cartProductPrice.getProductPrice().getPrice();
        return price.multiply(BigDecimal.valueOf(cartProductPrice.getQuantity()));
    }
}
