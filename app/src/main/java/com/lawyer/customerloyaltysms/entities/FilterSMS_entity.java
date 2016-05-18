package com.lawyer.customerloyaltysms.entities;

/**
 * Created by stiven on 5/17/2016.
 */
public class FilterSMS_entity {
    public String getFilterDescription() {
        return FilterDescription;
    }

    public void setFilterDescription(String filterDescription) {
        FilterDescription = filterDescription;
    }

    public String getFilterCustomer() {
        return FilterCustomer;
    }

    public void setFilterCustomer(String filterCustomer) {
        FilterCustomer = filterCustomer;
    }

    public String FilterDescription;
    public String FilterCustomer;
}
