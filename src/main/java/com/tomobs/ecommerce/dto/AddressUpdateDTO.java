package com.tomobs.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressUpdateDTO {

  private Long id;
  private String fullName;
  private String phoneNumber;
  private String addressLine1;
  private String addressLine2;
  private String city;
  private String state;
  private String postalCode;
  private String country;
  @JsonProperty("isDefault")
  private boolean isDefault;
}
