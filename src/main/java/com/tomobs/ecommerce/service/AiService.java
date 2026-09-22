package com.tomobs.ecommerce.service;

import com.tomobs.ecommerce.config.OrderReply;
import com.tomobs.ecommerce.dto.AiRequestDTO;

public interface AiService {

    OrderReply getStructuredAiResponse(AiRequestDTO userRequest, String conversationId);
}
