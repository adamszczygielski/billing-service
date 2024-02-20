package com.school.billingservice.dto.request;

import java.math.BigDecimal;

public record ChildReportRequestDto(long parentId, long schoolId, short year, short month, BigDecimal hourPrice) {

    public AttendanceReportRequestDto toAttendanceReportRequest(long childId, BigDecimal hourPrice) {
        return new AttendanceReportRequestDto(childId, year, month, hourPrice);
    }
}
