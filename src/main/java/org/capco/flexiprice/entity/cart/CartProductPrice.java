package org.capco.flexiprice.entity.cart;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import org.capco.flexiprice.entity.product.ProductPrice;

@Entity
@Table(name = "cart_product_price")
public class CartProductPrice {

    @EmbeddedId
    private CartProductPriceKey cartProductPriceKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("cartId")
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productPriceId")
    @JoinColumn(name = "product_price_id")
    private ProductPrice productPrice;

    @Column(name = "product_quantity")
    private int quantity;

    protected CartProductPrice() {
    }

    public CartProductPrice(Cart cart, ProductPrice productPrice, int quantity) {
        this.cartProductPriceKey = new CartProductPriceKey(cart.getId(), productPrice.getId());
        this.cart = cart;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public Cart getCart() {
        return cart;
    }

    public ProductPrice getProductPrice() {
        return productPrice;
    }
}
