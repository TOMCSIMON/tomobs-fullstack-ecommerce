package com.tomobs.ecommerce.config;

import com.google.api.client.util.Value;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class VectorStoreConfig {

    private final EmbeddingModel embeddingModel;

    @Bean
    public VectorStore vectorStore() {

        VectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();

        Document doc = new Document("""
        Items can be returned within 30 days of purchase with original receipt.
        Electronics have a 15-day return window due to warranty terms.
        Refunds are processed within 5-7 business days after we receive the item.
        Sale items are final and cannot be returned.
        """);


        TokenTextSplitter splitter = TokenTextSplitter.builder()
                .withChunkSize(500)
                .build();

        List<Document> chunks = splitter.apply(List.of(doc));

        vectorStore.add(chunks);

        return vectorStore;
    }
}
