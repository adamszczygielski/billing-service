package com.school.billingservice.dto.request;

public record SchoolReportRequestDto(long schoolId, short year, short month) {

    public ParentReportRequestDto toParentReportRequestDto(long parentId) {
        return new ParentReportRequestDto(schoolId, parentId, year, month);
    }
}
