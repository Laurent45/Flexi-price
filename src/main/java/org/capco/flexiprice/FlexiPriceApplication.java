package org.capco.flexiprice;

import org.capco.flexiprice.entity.product.Product;
import org.capco.flexiprice.entity.product.ProductPrice;
import org.capco.flexiprice.enumeration.ClientType;
import org.capco.flexiprice.enumeration.ProductType;
import org.capco.flexiprice.repository.product.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;

@SpringBootApplication
public class FlexiPriceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlexiPriceApplication.class, args);
    }

    @Bean
    @Profile("dev")
    public CommandLineRunner init(ProductRepository productRepository) {
        return args -> {
            Product laptop = new Product(ProductType.LAPTOP, "MacBook Pro");
            laptop.addPriceProduct(new ProductPrice(ClientType.PERSONAL, BigDecimal.valueOf(1200)));
            laptop.addPriceProduct(new ProductPrice(ClientType.PROFESSIONAL_REVENUE_GTE_10M, BigDecimal.valueOf(900)));
            laptop.addPriceProduct(new ProductPrice(ClientType.PROFESSIONAL_REVENUE_LT_10M, BigDecimal.valueOf(1000)));

            Product highEndPhone = new Product(ProductType.HIGH_END_PHONE, "iPhone 15 Pro Max");
            highEndPhone.addPriceProduct(new ProductPrice(ClientType.PERSONAL, BigDecimal.valueOf(1500)));
            highEndPhone.addPriceProduct(new ProductPrice(ClientType.PROFESSIONAL_REVENUE_GTE_10M, BigDecimal.valueOf(1000)));
            highEndPhone.addPriceProduct(new ProductPrice(ClientType.PROFESSIONAL_REVENUE_LT_10M, BigDecimal.valueOf(1150)));

            Product midRangePhone = new Product(ProductType.MID_RANGE_PHONE, "Google Pixel 7a");
            midRangePhone.addPriceProduct(new ProductPrice(ClientType.PERSONAL, BigDecimal.valueOf(800)));
            midRangePhone.addPriceProduct(new ProductPrice(ClientType.PROFESSIONAL_REVENUE_GTE_10M, BigDecimal.valueOf(550)));
            midRangePhone.addPriceProduct(new ProductPrice(ClientType.PROFESSIONAL_REVENUE_LT_10M, BigDecimal.valueOf(600)));

            productRepository.save(laptop);
            productRepository.save(highEndPhone);
            productRepository.save(midRangePhone);
        };
    }

}
