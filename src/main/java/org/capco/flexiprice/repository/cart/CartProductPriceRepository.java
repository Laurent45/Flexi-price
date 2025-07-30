package org.capco.flexiprice.repository.cart;

import org.capco.flexiprice.entity.cart.CartProductPrice;
import org.capco.flexiprice.entity.cart.CartProductPriceKey;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CartProductPriceRepository extends CrudRepository<CartProductPrice, Long> {

    Optional<CartProductPrice> findCartProductPriceByCartProductPriceKey(CartProductPriceKey cartProductPriceKey);
}
