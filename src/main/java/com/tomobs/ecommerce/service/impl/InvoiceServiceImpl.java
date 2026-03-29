package com.tomobs.ecommerce.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.tomobs.ecommerce.model.Orders;
import com.tomobs.ecommerce.service.InvoiceService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.IOException;


@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    @Override
    public void generate(Orders order, HttpServletResponse response) throws IOException {

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);
        Paragraph title = new Paragraph("INVOICE - ToMobs", fontTitle);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph("Order ID: " + order.getId()));
        document.add(new Paragraph("Date: " + order.getCreatedAt()));
        document.add(new Paragraph("Customer: " + order.getUser().getUserName()));

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.addCell("Product Name");
        table.addCell("Quantity");
        table.addCell("Price");

        order.getOrderItems().forEach(item -> {
            table.addCell(item.getProductVariant().getProduct().getProductName());
            table.addCell(String.valueOf(item.getQuantity()));
            table.addCell(String.valueOf(item.getPriceAtPurchase()));
        });

        document.add(table);
        document.add(new Paragraph("Total Amount: ₹" + order.getTotalAmount()));
        document.close();
    }
}
