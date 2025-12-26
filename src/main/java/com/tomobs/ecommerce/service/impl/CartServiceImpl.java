package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.config.CustomUserDetails;
import com.tomobs.ecommerce.dto.CartDTO;
import com.tomobs.ecommerce.model.*;
import com.tomobs.ecommerce.repository.CartItemsRepository;
import com.tomobs.ecommerce.repository.CartRepository;
import com.tomobs.ecommerce.repository.ProductVariantRepository;
import com.tomobs.ecommerce.repository.UserRepository;
import com.tomobs.ecommerce.service.CartService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartServiceImpl implements CartService {

  private final UserRepository userRepository;
  private final ProductVariantRepository productVariantRepository;
  private final CartRepository cartRepository;
  private final CartItemsRepository cartItemsRepository;

  public CartServiceImpl(
      CartRepository cartRepository,
      ProductVariantRepository productVariantRepository,
      UserRepository userRepository,
      CartItemsRepository cartItemsRepository) {
    this.cartRepository = cartRepository;
    this.userRepository = userRepository;
    this.productVariantRepository = productVariantRepository;
    this.cartItemsRepository = cartItemsRepository;
  }

  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new RuntimeException("No authenticated user found");
    }

    Object principal = authentication.getPrincipal();
    if (principal instanceof CustomUserDetails) {
      return ((CustomUserDetails) principal).getId();
    }

    throw new RuntimeException("Principal is not an instance of CustomUserDetails");
  }

  @Override
  public void addToCart(Long variantId) {

    Long userId = getCurrentUserId();

    ProductVariant variant =
        productVariantRepository
            .findById(variantId)
            .orElseThrow(() -> new RuntimeException("Product Variant not found!"));

    User user =
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));

    Cart cart =
        cartRepository
            .findByUserId(userId)
            .orElseGet(
                () -> {
                  Cart newCart = new Cart();
                  newCart.setUser(user);
                  return cartRepository.save(newCart);
                });

    Optional<CartItems> existingItem =
        cartItemsRepository.findByCartIdAndProductVariantId(cart.getId(), variantId);

    CartItems cartItems;
    if (existingItem.isPresent()) {

      cartItems = existingItem.get();
      cartItems.setQuantity(cartItems.getQuantity() + 1);
    } else {
      cartItems = new CartItems();
      cartItems.setCart(cart);
      cartItems.setProductVariant(variant);
      cartItems.setQuantity(1);
      cartItems.setPricePerUnit(variant.getPrice());
      cartItems.setSubtotal(variant.getPrice());
    }
    cartItemsRepository.save(cartItems);
  }

  @Override
  public List<CartDTO> findCart(Long userId) {

    User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));

    Cart cart = cartRepository.findByUserId(userId)
            .orElseGet(() -> {
                  Cart newCart = new Cart();
                  newCart.setUser(user);
                  return cartRepository.save(newCart);
                });

    // FETCHED DB CART ITEMS
    List<CartItems> itemsList = cartItemsRepository.findAllByCart(cart);

    // A NEW LIST TO STORE CART ITEMS
    List<CartDTO> finalCartData = new ArrayList<>();

    for (CartItems items : itemsList) {

      CartDTO cartDTO = new CartDTO();
      cartDTO.setCartItemId(items.getId());
      cartDTO.setQuantity(items.getQuantity());
      cartDTO.setPrice(items.getPricePerUnit());
      cartDTO.setSubtotal(items.calculateSubtotal());
      cartDTO.setVariantName(items.getProductVariant().getVariantName());

      VariantImage image = items.getProductVariant().getImages().get(0);
      if (image != null && image.getFilePath() != null) {
        String fileName = Paths.get(image.getFilePath()).getFileName().toString();
        cartDTO.setImageUrl("/uploads/products/" + fileName);
      }

      finalCartData.add(cartDTO);
    }
    return finalCartData;
  }

  @Override
  public void updateQuantity(Long cartItemId, int quantity) {

    CartItems item =
        cartItemsRepository
            .findById(cartItemId)
            .orElseThrow(() -> new RuntimeException("Item not found in cart!"));

    if (quantity <= 0) {
      cartItemsRepository.delete(item);
    } else {
      item.setQuantity(quantity);
      cartItemsRepository.save(item);
    }
  }


  @Override
  public Double calculateTotal(Long userId) {

    Cart cart =
        cartRepository
            .findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("cart not found!"));

    List<CartItems> items = cartItemsRepository.findAllByCart(cart);

    BigDecimal total =
        items.stream()
            .map(item -> item.getPricePerUnit().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    return total.doubleValue();
  }
}
