package com.tomobs.ecommerce.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.tomobs.ecommerce.dto.AdminDashboardDTO;
import com.tomobs.ecommerce.dto.AdminSalesDTO;
import com.tomobs.ecommerce.dto.DailyEarningMapping;
import com.tomobs.ecommerce.enums.PaymentStatus;
import com.tomobs.ecommerce.repository.OrdersRepository;
import com.tomobs.ecommerce.service.AdminDashboardService;
import com.tomobs.ecommerce.service.OrderService;
import com.tomobs.ecommerce.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final UserService userService;
    private final OrderService orderService;
    private final OrdersRepository ordersRepository;

    @Override
    public AdminDashboardDTO getDashboardSummary(LocalDate startDate, LocalDate endDate) {

        AdminDashboardDTO dto = new AdminDashboardDTO();
        dto.setTotalUsers(userService.findTotalUsers());
        dto.setTotalOrders(orderService.findOrders());
        dto.setTotalSale(ordersRepository.sumTotalRevenueByStatus(PaymentStatus.SUCCESS));

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        List<DailyEarningMapping> earnings = ordersRepository.getDailyEarnings(start, end, PaymentStatus.SUCCESS.name());

        Map<String, BigDecimal> earningMap = new HashMap<>();
        for (DailyEarningMapping e : earnings) {
            earningMap.put(e.getDate(), e.getAmount());
        }

        List<String> labels = new ArrayList<>();
        List<BigDecimal> data = new ArrayList<>();

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            String dbDateKey = currentDate.toString();
            labels.add(currentDate.format(DateTimeFormatter.ofPattern("dd MMM")));
            data.add(earningMap.getOrDefault(dbDateKey, BigDecimal.ZERO));
            currentDate = currentDate.plusDays(1);
        }
        dto.setLabels(labels);
        dto.setChartData(data);
        return dto;
    }

    @Override
    public void generatePdfReport(HttpServletResponse response, List<AdminSalesDTO> salesList,
                                  AdminDashboardDTO summary, LocalDate startDate, LocalDate endDate) throws IOException {

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Color.BLACK);
        Paragraph title = new Paragraph("ToMobs Sales Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.DARK_GRAY);
        Paragraph dateRange = new Paragraph("Report Period: " + startDate + " to " + endDate, subTitleFont);
        dateRange.setAlignment(Element.ALIGN_CENTER);
        dateRange.setSpacingAfter(20);
        document.add(dateRange);

        document.add(new Paragraph("Total Orders: " + summary.getTotalOrders()));
        document.add(new Paragraph("Total Revenue: Rs. " + summary.getTotalSale()));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        String[] headers = {"Order ID", "Customer Name", "Amount (Rs)", "Status", "Date"};
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headFont));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            cell.setPadding(5);
            table.addCell(cell);
        }

        for (AdminSalesDTO sale : salesList) {
            table.addCell(String.valueOf(sale.getId()));
            table.addCell(sale.getUserName());
            table.addCell(String.valueOf(sale.getTotalAmount()));
            table.addCell(sale.getStatus().toString());
            table.addCell(sale.getCreatedAt().toLocalDate().toString());
        }

        document.add(table);
        document.close();
    }
}
