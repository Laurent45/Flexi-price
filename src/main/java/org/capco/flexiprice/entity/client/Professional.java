package org.capco.flexiprice.entity.client;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.capco.flexiprice.enumeration.ClientType;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "professional_client")
public class Professional extends Client {

    @Column(name = "legal_name", nullable = false)
    private String legalName;

    @Column(name = "vat_number")
    private String vatNumber;

    @Column(name = "siren_number",  nullable = false, unique = true, updatable = false)
    private String sirenNumber;

    @Column(name = "annual_revenue", nullable = false)
    private BigDecimal annualRevenue;

    /**
     * Protected no-arg constructor for JPA only.
     * Not intended for direct use in application code.
     */
    protected Professional() {
    }

    /**
     * Private constructor to enforce object creation through static factory methods.
     * The constructor is private because we need to perform validation and determine
     * the appropriate {@link ClientType} before calling {@code super}. As a result,
     * instances must be created using the provided static {@code create} methods.
     */
    private Professional(Long id, ClientType clientType, String legalName, String vatNumber, String sirenNumber, BigDecimal annualRevenue, Long cartId) {
        super(id, clientType, cartId);
        this.legalName = legalName;
        this.vatNumber = vatNumber;
        this.sirenNumber = sirenNumber;
        this.annualRevenue = annualRevenue;
    }

    public String getLegalName() {
        return legalName;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public String getSirenNumber() {
        return sirenNumber;
    }

    public BigDecimal getAnnualRevenue() {
        return annualRevenue;
    }

    @Override
    public String toString() {
        return "Professional{" +
                "legalName='" + legalName + '\'' +
                ", vatNumber='" + vatNumber + '\'' +
                ", sirenNumber='" + sirenNumber + '\'' +
                ", annualRevenue=" + annualRevenue +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Professional that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(sirenNumber, that.sirenNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sirenNumber);
    }

    public static Professional create(Long id, String legalName, String vatNumber, String sirenNumber, BigDecimal annualRevenue, Long cartId) {
        if (legalName == null || legalName.isBlank()) {
            throw new IllegalArgumentException("legalName cannot be null or empty");
        }

        if (sirenNumber == null || sirenNumber.isBlank()) {
            throw new IllegalArgumentException("sirenNumber cannot be null or empty");
        }

        if (annualRevenue == null) {
            throw new IllegalArgumentException("annualRevenue cannot be null");
        }

        ClientType clientType = determineClientTypeByRevenue(annualRevenue);

        return new Professional(id, clientType, legalName, vatNumber, sirenNumber, annualRevenue, cartId);
    }


    public static Professional create(String legalName, String vatNumber, String sirenNumber,  BigDecimal annualRevenue) {
        return create(null, legalName, vatNumber, sirenNumber, annualRevenue, null);
    }

    /**
     * Determines the {@link ClientType} based on the provided annual revenue.
     * Returns {@code PROFESSIONAL_REVENUE_GTE_10M} if the revenue is greater than or equal to 1,000,000,
     * otherwise returns {@code PROFESSIONAL_REVENUE_LT_10M}.
     *
     * @param annualRevenue the annual revenue to evaluate
     * @return the corresponding {@link ClientType}
     */
    private static ClientType determineClientTypeByRevenue(BigDecimal annualRevenue) {
        return switch (annualRevenue) {
            case BigDecimal revenue when revenue.compareTo(BigDecimal.valueOf(1_000_000L)) >= 0 -> ClientType.PROFESSIONAL_REVENUE_GTE_10M;
            case null, default -> ClientType.PROFESSIONAL_REVENUE_LT_10M;
        };
    }
}
