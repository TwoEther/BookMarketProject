package org.project.shop.web;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressForm {
    private String zipcode;
    private String address1;
    private String address2;
    private String reference;

    @Override
    public String toString() {
        return "AddressForm{" +
                "zipcode='" + zipcode + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", reference='" + reference + '\'' +
                '}';
    }
}
