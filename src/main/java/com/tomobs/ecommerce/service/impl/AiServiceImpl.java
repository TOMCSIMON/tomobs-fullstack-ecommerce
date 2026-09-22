package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.config.OrderReply;
import com.tomobs.ecommerce.config.OrderTools;
import com.tomobs.ecommerce.config.RateLimiter;
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
      
          IMPORTANT: To cancel an order, follow these steps in order:
          1. If the user hasn't given a cancellation reason yet, ask for one.
             Do not invent, assume, or guess a reason on their behalf.
          2. Once you have both the order ID and a real reason from the user,
             ask them to confirm (e.g. "You'd like to cancel order 44 due to
             [reason] — should I submit this cancellation request?").
          3. Only call the cancelOrder tool after the user has explicitly
             confirmed (e.g. replied "yes", "confirm", "go ahead").
          4. Note: cancelling submits a request for review — it does not
             instantly cancel the order, so don't tell the user it's final
             or irreversible.
      
          Context:
          {context}
          """;

  private final ChatClient chatClient;
  private final VectorStore vectorStore;
  private final ChatMemory chatMemory;
  private final OrderTools orderTools;
  private final RateLimiter rateLimiter;

  @Override
  public OrderReply getStructuredAiResponse(AiRequestDTO userRequest, String conversationId) {

    if (!rateLimiter.isAllowed(conversationId)) {
      return new OrderReply("You're sending messages too quickly. Please wait a moment and try again.", null, null);
    }
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
      return new OrderReply("The assistant is temporarily unavailable. Please try again in a moment", null, null);
    }
  }
}
