package com.tomobs.ecommerce.config;

import com.tomobs.ecommerce.model.Product;
import com.tomobs.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class VectorStoreConfig {

    private final EmbeddingModel embeddingModel;
    private final ProductRepository productRepository;

    @Bean
    public VectorStore vectorStore() {

        VectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();

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

        List<Document> chunks = splitter.apply(documents);

        vectorStore.add(chunks);

        return vectorStore;
    }
}
