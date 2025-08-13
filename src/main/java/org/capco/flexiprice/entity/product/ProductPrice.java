package org.capco.flexiprice.entity.product;

import jakarta.persistence.*;
import org.capco.flexiprice.entity.cart.CartProductPrice;
import org.capco.flexiprice.enumeration.ClientType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        name = "product_price",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "client_type"})
)
public class ProductPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, updatable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "client_type", nullable = false, updatable = false)
    private ClientType clientType;

    @Column(nullable = false)
    private BigDecimal price;

    @OneToMany(mappedBy = "productPrice")
    private List<CartProductPrice> cartProductPrices = new ArrayList<>();

    /**
     * Protected no-arg constructor for JPA only.
     * Not intended for direct use in application code.
     */
    protected ProductPrice() {
    }

    public ProductPrice(Long id, ClientType clientType, BigDecimal price) {
        this.id = id;
        this.clientType = clientType;
        this.price = price;
    }

    public ProductPrice(ClientType clientType, BigDecimal price) {
        this(null, clientType, price);
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "ProductPrice{" +
                "id=" + id +
                ", product=" + product +
                ", clientType=" + clientType +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ProductPrice that)) return false;
        return Objects.equals(product, that.product) && clientType == that.clientType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, clientType);
    }
}

