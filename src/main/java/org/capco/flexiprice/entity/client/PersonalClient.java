package org.capco.flexiprice.entity.client;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.capco.flexiprice.enumeration.ClientType;

import java.util.Objects;

@Entity
@Table(name = "personal_client")
public class PersonalClient extends Client {

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "fist_name", nullable = false)
    private String firstName;

    @Column(name = "user_name", nullable = false, unique = true, updatable = false)
    private String username;

    /**
     * Protected no-arg constructor for JPA only.
     * Not intended for direct use in application code.
     */
    protected PersonalClient() {
    }

    public PersonalClient(Long id, String lastName, String firstName, String username, Long cartId) {
        super(id, ClientType.PERSONAL, cartId);

        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("lastName cannot be null or empty");
        }

        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("firstName cannot be null or empty");
        }

        if  (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username cannot be null or empty");
        }

        this.lastName = lastName;
        this.firstName = firstName;
        this.username = username;
    }

    public PersonalClient(String lastName, String firstName, String username) {
        this(null, lastName, firstName, username, null);
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "PersonalClient{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", username='" + username + '\'' +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PersonalClient that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username);
    }
}
