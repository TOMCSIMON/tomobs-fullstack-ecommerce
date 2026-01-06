package com.tomobs.ecommerce.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAddressListDTO {

    private Long id;

    private String fullName;

    private String phoneNumber;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String state;

    private String postalCode;

    private String country;

    private boolean isDefault;
}
