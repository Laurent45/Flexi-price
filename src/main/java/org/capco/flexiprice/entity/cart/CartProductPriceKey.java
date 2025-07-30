package org.capco.flexiprice.entity.cart;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CartProductPriceKey implements Serializable {

    @Column(name = "cart_id")
    Long cartId;

    @Column(name = "product_price_id")
    Long productPriceId;

    /**
     * Protected no-arg constructor for JPA only.
     * Not intended for direct use in application code.
     */
    protected CartProductPriceKey() {
    }

    public CartProductPriceKey(Long cartId, Long productPriceId) {
        this.cartId = cartId;
        this.productPriceId = productPriceId;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getProductPriceId() {
        return productPriceId;
    }

    public void setProductPriceId(Long productPriceId) {
        this.productPriceId = productPriceId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CartProductPriceKey that)) return false;
        return Objects.equals(cartId, that.cartId) && Objects.equals(productPriceId, that.productPriceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId, productPriceId);
    }
}
