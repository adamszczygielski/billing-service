package com.school.billingservice.dto.request;

import java.math.BigDecimal;

public record AttendanceReportRequestDto(long childId, short year, short month, BigDecimal hourPrice) {
}
