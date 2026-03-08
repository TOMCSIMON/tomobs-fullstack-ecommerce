package com.tomobs.ecommerce.service;

import com.tomobs.ecommerce.dto.UserAddressAddDTO;
import com.tomobs.ecommerce.dto.UserAddressListDTO;
import com.tomobs.ecommerce.dto.AddressUpdateDTO;

import java.util.List;

public interface UserAddressService {

     void addAddress(UserAddressAddDTO userAddressAddDTO);

     List<UserAddressListDTO> getAddress();

     void deleteAddress(Long id);

     void updateAddress(AddressUpdateDTO dto);
}
