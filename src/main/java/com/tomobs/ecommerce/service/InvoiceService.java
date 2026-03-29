package com.tomobs.ecommerce.service;

import com.tomobs.ecommerce.model.Orders;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface InvoiceService {

    void generate(Orders order, HttpServletResponse response) throws IOException;
}
