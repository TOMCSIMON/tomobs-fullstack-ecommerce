package com.tomobs.ecommerce.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.apache.tomcat.util.http.fileupload.impl.FileCountLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MultipartException.class)
    public String handleMultipartException(MultipartException ex, RedirectAttributes redirectAttributes) {

        // Log the full error for debugging
        ex.printStackTrace();

        Throwable rootCause = ex.getRootCause();

        if (rootCause instanceof FileCountLimitExceededException) {
            redirectAttributes.addFlashAttribute("error",
                    "Too many files uploaded at once. Maximum allowed is 100 files per request.");
        } else if (rootCause instanceof FileSizeLimitExceededException) {
            redirectAttributes.addFlashAttribute("error",
                    "One of the files exceeds the maximum size of 20MB.");
        } else if (ex instanceof MaxUploadSizeExceededException) {
            redirectAttributes.addFlashAttribute("error",
                    "Total upload size exceeds the maximum limit of 50MB.");
        } else {
            redirectAttributes.addFlashAttribute("error",
                    "Error uploading files: " + ex.getMessage());
        }

        return "redirect:/admin/products/add";
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error",
                "File size exceeds maximum limit. Maximum file size is 20MB and total request size is 50MB.");
        return "redirect:/admin/products/add";
    }
}
