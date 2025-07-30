package org.capco.flexiprice.entity.cart;

import jakarta.persistence.*;
import org.capco.flexiprice.entity.client.Client;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id",  nullable = false)
    private Client client;

    @OneToMany(
            mappedBy = "cart",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private Set<CartProductPrice> cartProductPrices = new HashSet<>();

    /**
     * Protected no-arg constructor for JPA only.
     * Not intended for direct use in application code.
     */
    protected Cart() {
    }

    public Cart(Long id, Client client) {
        this.id = id;
        this.client = client;
    }

    public Cart(Client client) {
        this(null, client);
    }

    public Long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public Set<CartProductPrice> getCartProductPrices() {
        return cartProductPrices;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Cart cart)) return false;
        return Objects.equals(id, cart.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
