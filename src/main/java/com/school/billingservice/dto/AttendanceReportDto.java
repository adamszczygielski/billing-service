package com.school.billingservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record AttendanceReportDto(LocalDateTime entryDate, LocalDateTime exitDate, int overallHours, int billableHours,
                                  BigDecimal price) {
}
