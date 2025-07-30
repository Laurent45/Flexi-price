package org.capco.flexiprice.exception;

public class ProductPriceNotFoundException extends RuntimeException {
    public ProductPriceNotFoundException(String message) {
        super(message);
    }
}
