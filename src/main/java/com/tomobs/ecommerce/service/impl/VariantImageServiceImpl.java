package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.model.ProductVariant;
import com.tomobs.ecommerce.model.VariantImage;
import com.tomobs.ecommerce.repository.VariantImageRepository;
import com.tomobs.ecommerce.service.VariantImageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class VariantImageServiceImpl implements VariantImageService {

    private final VariantImageRepository variantImageRepository;

    @Value("${product.images.upload.path}")
    private String uploadDir;

    public VariantImageServiceImpl(VariantImageRepository variantImageRepository) {
        this.variantImageRepository = variantImageRepository;
    }

    @Override
    @Transactional
    public void saveImage(MultipartFile file, ProductVariant variant) throws IOException {

        // Validate file
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image. Got: " + contentType);
        }

        // Log for debugging
        System.out.println("Saving image to: " + uploadDir);
        System.out.println("Original filename: " + file.getOriginalFilename());
        System.out.println("File size: " + file.getSize() + " bytes");

        // ENSURE FOLDER EXISTS
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            System.out.println("Created upload directory: " + uploadPath.toAbsolutePath());
        }

        // GENERATE UNIQUE FILE NAME
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + fileExtension;

        // PATH WHERE FILE WILL BE SAVED
        Path filePath = uploadPath.resolve(fileName);

        // SAVE FILE TO DISK
        file.transferTo(filePath.toFile());
        System.out.println("File saved to: " + filePath.toAbsolutePath());

        // Check if this is the first image (make it primary)
        long existingImageCount = variantImageRepository.countByProductVariant(variant);
        boolean isPrimary = (existingImageCount == 0);

        // SAVE TO DB RECORD
        VariantImage variantImage = new VariantImage();
        variantImage.setFileName(fileName);
        variantImage.setFilePath(filePath.toString());
        variantImage.setPrimary(isPrimary); // First image is primary
        variantImage.setProductVariant(variant);

        VariantImage saved = variantImageRepository.save(variantImage);
        System.out.println("Image record saved to database with ID: " + saved.getId());
    }
}