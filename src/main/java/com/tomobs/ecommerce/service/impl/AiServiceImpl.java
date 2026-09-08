package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.dto.AiRequestDTO;
import com.tomobs.ecommerce.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiServiceImpl implements AiService {

  private final ChatClient chatClient;

  @Override
  public String getAiResponse(AiRequestDTO userRequest) {

    log.info("Sending request to OpenAI: {}", userRequest.getRequest());

    return chatClient.prompt().user(userRequest.getRequest()).call().content();
  }
}
