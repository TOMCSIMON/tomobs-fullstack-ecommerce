package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.config.CustomUserDetails;
import com.tomobs.ecommerce.dto.UserAddressAddDTO;
import com.tomobs.ecommerce.mapper.AddressMapper;
import com.tomobs.ecommerce.model.Address;
import com.tomobs.ecommerce.model.User;
import com.tomobs.ecommerce.repository.UserAddressRepository;
import com.tomobs.ecommerce.repository.UserRepository;
import com.tomobs.ecommerce.service.UserAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserAddressServiceImpl implements UserAddressService {

    private final UserAddressRepository userAddressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;


    public UserAddressServiceImpl(UserAddressRepository userAddressRepository, UserRepository userRepository, AddressMapper addressMapper) {
        this.userAddressRepository = userAddressRepository;
        this.userRepository = userRepository;
        this.addressMapper = addressMapper;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        return userDetails.getId();
    }


    @Override
    public void addAddress(UserAddressAddDTO userAddressAddDTO) {

        log.info("userId: {}" , getCurrentUserId());
        Long userId = getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Address address = addressMapper.toEntity(userAddressAddDTO, user);

        userAddressRepository.save(address);
    }
}
