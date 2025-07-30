package org.capco.flexiprice.repository.product;

import org.capco.flexiprice.entity.product.ProductPrice;
import org.capco.flexiprice.enumeration.ClientType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {

    Optional<ProductPrice> findProductPriceByProductIdAndClientType(Long productId, ClientType clientType);

    Optional<ProductPrice> findProductPriceByProductNameAndClientType(String productName, ClientType clientType);
}
