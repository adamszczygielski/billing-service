package com.school.billingservice.controller;

import com.school.billingservice.dto.request.ParentReportRequestDto;
import com.school.billingservice.dto.request.SchoolReportRequestDto;
import com.school.billingservice.dto.SchoolReportDto;
import com.school.billingservice.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("billings")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @GetMapping("schools/{schoolId}")
    public ResponseEntity<SchoolReportDto> getReport(@PathVariable long schoolId, @RequestParam short year,
                                                     @RequestParam short month) {
        SchoolReportDto reportDto = billingService.createReportDto(new SchoolReportRequestDto(schoolId, year, month));
        return ResponseEntity.ok(reportDto);
    }

    @GetMapping("schools/{schoolId}/parents/{parentId}")
    public ResponseEntity<SchoolReportDto> getReport(@PathVariable long schoolId, @PathVariable long parentId,
                                                     @RequestParam short year, @RequestParam short month) {
        SchoolReportDto reportDto = billingService.createReportDto(new ParentReportRequestDto(schoolId, parentId, year,
                month));
        return ResponseEntity.ok(reportDto);
    }
}
