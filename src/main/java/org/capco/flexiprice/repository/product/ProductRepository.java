package org.capco.flexiprice.repository.product;

import org.capco.flexiprice.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsProductByName(String productName);

}
