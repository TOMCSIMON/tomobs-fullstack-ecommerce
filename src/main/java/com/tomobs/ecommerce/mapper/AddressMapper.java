package com.tomobs.ecommerce.mapper;

import com.tomobs.ecommerce.dto.UserAddressAddDTO;
import com.tomobs.ecommerce.dto.UserAddressListDTO;
import com.tomobs.ecommerce.dto.AddressUpdateDTO;
import com.tomobs.ecommerce.model.Address;
import com.tomobs.ecommerce.model.User;
import com.tomobs.ecommerce.repository.UserAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressMapper {

  private final UserAddressRepository addressRepository;
  // MAPPING DTO -> ENTITY
  public Address toEntity(UserAddressAddDTO dto, User user) {

    Address address = new Address();
    address.setUser(user);
    address.setFullName(dto.getFullName());
    address.setPhoneNumber(dto.getPhoneNumber());
    address.setAddressLine1(dto.getAddressLine1());
    address.setAddressLine2(dto.getAddressLine2());
    address.setCity(dto.getCity());
    address.setState(dto.getState());
    address.setPostalCode(dto.getPostalCode());
    address.setCountry(dto.getCountry());
    address.setDefault(dto.getIsDefault());

    return address;
  }

  // MAPPING ENTITY -> DTO
  public UserAddressListDTO toDTO(Address address) {

    UserAddressListDTO dto = new UserAddressListDTO();

    dto.setId(address.getId());
    dto.setFullName(address.getFullName());
    dto.setPhoneNumber(address.getPhoneNumber());
    dto.setAddressLine1(address.getAddressLine1());
    dto.setAddressLine2(address.getAddressLine2());
    dto.setCity(address.getCity());
    dto.setState(address.getState());
    dto.setPostalCode(address.getPostalCode());
    dto.setCountry(address.getCountry());
    dto.setDefault(address.isDefault());
    return dto;
  }

  // MAPPING UPDATE DTO -> ENTITY
  public void updateDtoToEntity(AddressUpdateDTO dto, Address existingAddress) {

    existingAddress.setFullName(dto.getFullName());
    existingAddress.setPhoneNumber(dto.getPhoneNumber());
    existingAddress.setAddressLine1(dto.getAddressLine1());
    existingAddress.setAddressLine2(dto.getAddressLine2());
    existingAddress.setCity(dto.getCity());
    existingAddress.setState(dto.getState());
    existingAddress.setPostalCode(dto.getPostalCode());
    existingAddress.setCountry(dto.getCountry());
    existingAddress.setDefault(dto.isDefault());

  }
}
