package com.tomobs.ecommerce.service;

import com.tomobs.ecommerce.model.ProductVariant;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface VariantImageService {

    void saveImage(MultipartFile file, ProductVariant variant) throws IOException;
}

