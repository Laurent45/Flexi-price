package org.capco.flexiprice.entity.product;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.capco.flexiprice.enumeration.ProductType;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", nullable = false)
    private ProductType type;

    @Column(name = "product_name",  nullable = false, updatable = false)
    private String name;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<ProductPrice> productPrices = new HashSet<>();

    /**
     * Protected no-arg constructor for JPA only.
     * Not intended for direct use in application code.
     */
    protected Product() {
    }

    public Product(Long id, ProductType type, String name) {
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be null or blank");
        }
        this.id = id;
        this.type = type;
        this.name = name;
    }

    public Product(ProductType type, String name) {
        this(null, type, name);
    }

    public void addPriceProduct(ProductPrice productPrice) {
        productPrices.add(productPrice);
        productPrice.setProduct(this);
    }

    public void removePriceProduct(ProductPrice productPrice) {
        productPrices.remove(productPrice);
        productPrice.setProduct(null);
    }

    public Long getId() {
        return id;
    }

    public ProductType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Set<ProductPrice> getProductPrices() {
        return productPrices;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Product product)) return false;
        return Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
