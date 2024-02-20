package com.school.billingservice.dto.request;

import java.math.BigDecimal;

public record ParentReportRequestDto(Long schoolId, Long parentId, Short year, Short month) {

    public ChildReportRequestDto toChildReportRequestDto(BigDecimal hourPrice) {
        return new ChildReportRequestDto(parentId, schoolId, year, month, hourPrice);
    }
}
