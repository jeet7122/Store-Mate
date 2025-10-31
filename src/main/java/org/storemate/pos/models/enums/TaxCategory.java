package org.storemate.pos.models.enums;

public enum TaxCategory {
    GST(0.09),
    HST(0.13),
    NO_TAX(0.0);

    private double tax_value;
    private TaxCategory(double tax_value) {
        this.tax_value = tax_value;
    }
    public double getTax_value() {
        return tax_value;
    }
    public void setTax_value(double tax_value) {
        this.tax_value = tax_value;
    }
}

