package org.project.shop.domain;


import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Getter;

@Embeddable
@Getter
public class Address {
    private String zipcode;
    private String address1;
    private String address2;
    private String reference;


    public Address(String zipcode, String address1, String address2, String reference) {
        this.zipcode = zipcode;
        this.address1 = address1;
        this.address2 = address2;
        this.reference = reference;
    }

    protected Address(){}

}
