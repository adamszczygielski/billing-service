package com.school.billingservice.dto;

import java.util.List;

public record SchoolReportDto(String schoolName, List<ParentReportDto> parentReportDtos) {
}
