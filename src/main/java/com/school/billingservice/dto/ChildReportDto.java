package com.school.billingservice.dto;

import java.math.BigDecimal;
import java.util.List;

public record ChildReportDto(String childFirstName, String childLastName, int overallHours, int billableHours, BigDecimal price,
                             List<AttendanceReportDto> attendanceReportDtos) {
}
