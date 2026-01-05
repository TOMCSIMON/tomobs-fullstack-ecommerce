package com.tomobs.ecommerce.mapper;

import com.tomobs.ecommerce.dto.UserAddressAddDTO;
import com.tomobs.ecommerce.model.Address;
import com.tomobs.ecommerce.model.User;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

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
}
