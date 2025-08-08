package org.capco.flexiprice.service.cart;

import org.capco.flexiprice.dto.CartWithTotalAmountDTO;
import org.capco.flexiprice.dto.ProductAddRequestDTO;
import org.capco.flexiprice.dto.ProductAddToCartDTO;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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

 /**
  * Adds a product to the cart with the specified cart ID.
  * <p>
  * If the cart does not exist, throws {@link CartNotFoundException}.
  * If the product is already in the cart, increases its quantity.
  * Otherwise, creates a new cart product entry.
  * </p>
  *
  * @param cartId         the ID of the cart
  * @param productRequest the product details and quantity to add
  * @return the updated {@link ProductAddToCartDTO} containing cart ID, product name, and updated quantity
  * @throws CartNotFoundException if the cart is not found
  */
    @Transactional
    public ProductAddToCartDTO addProductToCart(Long cartId, ProductAddRequestDTO productRequest) {
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

        CartProductPrice cartProductPriceSaved = cartProductPriceRepository.save(cartProductPrice);
        return new ProductAddToCartDTO(cartId, productRequest.name(), cartProductPriceSaved.getQuantity());
    }

    /**
     * Retrieves the cart with the specified ID and calculates its total amount.
     * Throws {@link CartNotFoundException} if the cart does not exist.
     *
     * @param cartId the ID of the cart to retrieve
     * @return a {@link CartWithTotalAmountDTO} containing cart details and total amount
     * @throws CartNotFoundException if the cart is not found
     */
    @Transactional(readOnly = true)
    public CartWithTotalAmountDTO getCartWithTotalAmount(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> {
                    LOG.warn("Cart with id {} not found", cartId);
                    return new CartNotFoundException("Cart with id " + cartId + " not found");
                });

        BigDecimal totalAmount = calculateTotalAmount(cart);

        return CartWithTotalAmountDTO.cartProductPriceToProductDTO(cartId, cart.getCartProductPrices(), totalAmount);
    }

    /**
     * Calculates the total amount for all products in the given cart.
     * Sums the price multiplied by quantity for each product in the cart.
     *
     * @param cart the {@link Cart} entity containing products
     * @return the total amount as {@link BigDecimal}
     */
    private BigDecimal calculateTotalAmount(Cart cart) {
        return cart.getCartProductPrices().stream()
                .map(this::getProductPriceInCart)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getProductPriceInCart(CartProductPrice cartProductPrice) {
        BigDecimal price = cartProductPrice.getProductPrice().getPrice();
        return price.multiply(BigDecimal.valueOf(cartProductPrice.getQuantity()));
    }
}
