package com.tomobs.ecommerce.config;


import com.tomobs.ecommerce.model.Product;
import com.tomobs.ecommerce.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductIndexer {

    private final VectorStore vectorStore;
    private final JdbcTemplate jdbcTemplate;
    private final ProductRepository productRepository;

    @PostConstruct
    public void indexProducts() {

        jdbcTemplate.execute("DELETE FROM vector_store");

        List<Product> products = productRepository.findAll();

        List<Document> documents = products.stream()
                .map(product -> new Document(
                        """
                        Product: %s
                        CreateAt: %s
                        """.formatted(
                                product.getProductName(),
                                product.getCreatedAt()
                        ),
                        Map.of("productId", product.getId().toString())
                ))
                .toList();

        TokenTextSplitter splitter = TokenTextSplitter.builder()
                .withChunkSize(500)
                .build();

        vectorStore.add(splitter.apply(documents));

        log.info("Indexed {} products into PGVector", products.size());
    }
}