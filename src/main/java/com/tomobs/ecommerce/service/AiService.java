package com.tomobs.ecommerce.service;

import com.tomobs.ecommerce.dto.AiRequestDTO;

public interface AiService {

    String getAiResponse(AiRequestDTO userRequest);
}
