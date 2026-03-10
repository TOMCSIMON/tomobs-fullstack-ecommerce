package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.model.ProductVariant;
import com.tomobs.ecommerce.model.VariantImage;
import com.tomobs.ecommerce.repository.VariantImageRepository;
import com.tomobs.ecommerce.service.VariantImageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VariantImageServiceImpl implements VariantImageService {

    private final VariantImageRepository variantImageRepository;

    @Value("${product.images.upload.path}")
    private String uploadDir;

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
        log.info("Saving image to: {}",  uploadDir);
        log.info("Original filename: {} ", file.getOriginalFilename());
        log.info("File size: {} " , file.getSize() + " bytes");

        // ENSURE FOLDER EXISTS
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // GENERATE UNIQUE FILE NAME
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID() + fileExtension;

        // PATH WHERE FILE WILL BE SAVED
        Path filePath = uploadPath.resolve(fileName);

        // SAVE FILE TO DISK
        file.transferTo(filePath.toFile());
        log.info("File saved to: {}",  filePath.toAbsolutePath());

        // Check if this is the first image
        long existingImageCount = variantImageRepository.countByProductVariant(variant);
        boolean isPrimary = (existingImageCount == 0);

        VariantImage variantImage = new VariantImage();
        variantImage.setFileName(fileName);
        variantImage.setFilePath(filePath.toString());
        variantImage.setPrimary(isPrimary);
        variantImage.setProductVariant(variant);

        VariantImage saved = variantImageRepository.save(variantImage);
    }

    @Override
    @Transactional
    public void deleteImage(Long imageId) {
        variantImageRepository.findById(imageId).ifPresent(image -> {
            try {
                // 1. Delete the physical file from the disk
                Path filePath = Paths.get(image.getFilePath());
                Files.deleteIfExists(filePath);
                log.info("Deleted physical image file: {}", filePath.toAbsolutePath());

                // 2. Delete the record from the database
                variantImageRepository.delete(image);
                log.info("Deleted image record from DB with ID: {}", imageId);

            } catch (IOException e) {
                log.error("Failed to delete image file: {}", image.getFilePath(), e);
                throw new RuntimeException("Could not delete image file", e);
            }
        });
    }
}