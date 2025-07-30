package org.capco.flexiprice.entity.client;

import jakarta.persistence.*;
import org.capco.flexiprice.entity.cart.Cart;
import org.capco.flexiprice.enumeration.ClientType;

import java.util.Objects;

@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "client")
@Entity
public abstract class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "client_type", nullable = false)
    private ClientType clientType;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    protected Client() {
    }

    protected Client(Long id, ClientType clientType, Long cartId) {
        if (clientType == null) {
            throw new IllegalArgumentException("clientType cannot be null");
        }

        this.id = id;
        this.clientType = clientType;
        this.cart = new Cart(cartId, this);
    }

    protected Client(Long id, ClientType clientType) {
        this(id, clientType, null);
    }

    protected Client(ClientType clientType) {
        this(null, clientType);
    }

    public Long getId() {
        return id;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public Cart getCart() {
        return cart;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", clientType=" + clientType +
                ", cart=" + cart +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Client client)) return false;
        return Objects.equals(id, client.id) && clientType == client.clientType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientType);
    }
}

