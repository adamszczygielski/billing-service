package com.school.billingservice.dto;


import java.math.BigDecimal;
import java.util.List;


public record ParentReportDto(String parentFirstName, String parentLastName, int overallHours, int billableHours,
                              BigDecimal price, List<ChildReportDto> childReportDtos) {
}
