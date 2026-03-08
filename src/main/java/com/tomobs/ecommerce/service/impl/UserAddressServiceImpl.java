package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.config.CustomUserDetails;
import com.tomobs.ecommerce.dto.UserAddressAddDTO;
import com.tomobs.ecommerce.dto.UserAddressListDTO;
import com.tomobs.ecommerce.dto.AddressUpdateDTO;
import com.tomobs.ecommerce.mapper.AddressMapper;
import com.tomobs.ecommerce.model.Address;
import com.tomobs.ecommerce.model.User;
import com.tomobs.ecommerce.repository.UserAddressRepository;
import com.tomobs.ecommerce.repository.UserRepository;
import com.tomobs.ecommerce.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAddressServiceImpl implements UserAddressService {

    private final UserAddressRepository userAddressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;


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

        Long userId = getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean hasAnyAddress = userAddressRepository.existsByUser(user);

        if (!hasAnyAddress) {
            userAddressAddDTO.setIsDefault(true);
        }

        // USER EXPLICITLY SET THIS AS DEFAULT
        else if (userAddressAddDTO.getIsDefault()) {

            userAddressRepository
                    .findByUserAndIsDefaultTrue(user)
                    .ifPresent(existingDefault -> {
                        existingDefault.setDefault(false);
                        userAddressRepository.save(existingDefault);
                    });
        }
        Address address = addressMapper.toEntity(userAddressAddDTO, user);

        userAddressRepository.save(address);
    }


    @Override
    public List<UserAddressListDTO> getAddress() {

        Long userId = getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Address> addresses = userAddressRepository.findByUser(user);

        return addresses.stream()
                .map(addressMapper::toDTO)
                .toList();

    }

    @Override
    public void deleteAddress(Long id) {

        Long userId = getCurrentUserId();

        Address address = userAddressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if(!address.getUser().getId().equals(userId)){
            throw new RuntimeException("You are not allowed to delete this address");
        }
        userAddressRepository.deleteById(id);
    }

    @Override
    public void updateAddress(AddressUpdateDTO dto) {

        Long userId = getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Address existingAddress = userAddressRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!existingAddress.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to update this address");
        }
        if(dto.isDefault() && !existingAddress.isDefault()) {
            userAddressRepository.findByUserAndIsDefaultTrue(user)
                    .ifPresent(oldDefault -> {
                        oldDefault.setDefault(false);
                        userAddressRepository.save(oldDefault);
                    });
        }
        addressMapper.updateDtoToEntity(dto, existingAddress);
        userAddressRepository.save(existingAddress);
    }
}
