package org.capco.flexiprice.enumeration;

/**
 * Represents the type of client.
 */
public enum ClientType {
    /**
     * Particular client (individual).
     */
    PERSONAL,

    /**
     * Professional client with an annual revenue greater than or equal to 10 million dollars.
     */
    PROFESSIONAL_REVENUE_GTE_10M,

    /**
     * Professional client with an annual revenue less than 10 million dollars.
     */
    PROFESSIONAL_REVENUE_LT_10M
}
