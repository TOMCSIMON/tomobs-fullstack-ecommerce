package com.tomobs.ecommerce.controller.User;

import com.tomobs.ecommerce.dto.AddressUpdateDTO;
import com.tomobs.ecommerce.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class UserAddressRestController {

    private final UserAddressService userAddressService;

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteAddress(@PathVariable Long id) {

        userAddressService.deleteAddress(id);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PutMapping("/edit")
    public ResponseEntity<?> updateProfile(
            @RequestBody AddressUpdateDTO dto) {

        userAddressService.updateAddress(dto);

        return ResponseEntity.ok(Map.of("success", true));
    }

}
