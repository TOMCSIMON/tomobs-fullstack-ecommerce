package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.config.OrderReply;
import com.tomobs.ecommerce.config.OrderTools;
import com.tomobs.ecommerce.dto.AiRequestDTO;
import com.tomobs.ecommerce.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiServiceImpl implements AiService {

  private static final String SYSTEM_PROMPT =
      """
      You are a customer support assistant for an e-commerce store.
      Only answer questions related to shopping, orders, products, and store policies.
      Use ONLY the provided context below to answer. If the context doesn't
      contain the answer, say you don't have that information.

      Context:{context}
      """;

  private final ChatClient chatClient;
  private final VectorStore vectorStore;
  private final ChatMemory chatMemory;
  private final OrderTools orderTools;

  @Override
  public OrderReply getStructuredAiResponse(AiRequestDTO userRequest, String conversationId) {

    try {
      SearchRequest searchRequest = SearchRequest.builder()
              .query(userRequest.getRequest())
              .topK(3)
              .build();

      List<Document> relevantChunks = vectorStore.similaritySearch(searchRequest);

      String context = relevantChunks.stream()
              .map(Document::getText)
              .collect(Collectors.joining("\n"));

      return chatClient.prompt()
              .system(SYSTEM_PROMPT.replace("{context}", context))
              .user(userRequest.getRequest())
              .tools(orderTools)
              .advisors(a -> a
                      .advisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                      .param(ChatMemory.CONVERSATION_ID, conversationId))
              .call()
              .entity(OrderReply.class);

    } catch (Exception e) {
      log.error("AI request failed", e);
      return new OrderReply("The assistant is temporarily unavailable.", null, null);
    }
  }
}
