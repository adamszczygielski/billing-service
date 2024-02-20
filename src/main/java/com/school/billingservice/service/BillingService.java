package com.school.billingservice.service;

import com.school.billingservice.domain.Attendance;
import com.school.billingservice.domain.Child;
import com.school.billingservice.domain.Parent;
import com.school.billingservice.domain.School;
import com.school.billingservice.dto.AttendanceReportDto;
import com.school.billingservice.dto.ChildReportDto;
import com.school.billingservice.dto.ParentReportDto;
import com.school.billingservice.dto.SchoolReportDto;
import com.school.billingservice.dto.request.AttendanceReportRequestDto;
import com.school.billingservice.dto.request.ChildReportRequestDto;
import com.school.billingservice.dto.request.ParentReportRequestDto;
import com.school.billingservice.dto.request.SchoolReportRequestDto;
import com.school.billingservice.repository.AttendanceRepository;
import com.school.billingservice.repository.ChildRepository;
import com.school.billingservice.repository.ParentRepository;
import com.school.billingservice.repository.SchoolRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BillingService {

    private static final Map<Long, BigDecimal> HOUR_PRICE_BY_SCHOOL_ID = new HashMap<>();

    private final HoursCalculatorService hoursCalculatorService;
    private final SchoolRepository schoolRepository;
    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;
    private final AttendanceRepository attendanceRepository;

    @Transactional
    public SchoolReportDto createReportDto(SchoolReportRequestDto requestDto) {
        School school = schoolRepository.findById(requestDto.schoolId())
                .orElseThrow(() -> new NoSuchElementException("School not found"));
        HOUR_PRICE_BY_SCHOOL_ID.put(school.getId(), school.getHourPrice());

        List<Long> parentIds = parentRepository.findParentsIds(school.getId());
        List<ParentReportDto> parentReportDtos = parentIds.stream().map(requestDto::toParentReportRequestDto)
                .map(this::createParentReportDtos)
                .toList();

        return new SchoolReportDto(school.getName(), parentReportDtos);
    }

    @Transactional
    public SchoolReportDto createReportDto(ParentReportRequestDto requestDto) {
        School school = schoolRepository.findById(requestDto.schoolId()).orElseThrow();
        HOUR_PRICE_BY_SCHOOL_ID.put(school.getId(), school.getHourPrice());

        ParentReportDto parentReportDtos = createParentReportDtos(requestDto);

        return new SchoolReportDto(school.getName(), List.of(parentReportDtos));
    }

    private ParentReportDto createParentReportDtos(ParentReportRequestDto requestDto) {
        Parent parent = parentRepository.findBySchoolIdAndParentId(requestDto.schoolId(), requestDto.parentId());
        BigDecimal hourPrice = HOUR_PRICE_BY_SCHOOL_ID.get(requestDto.schoolId());
        List<ChildReportDto> childReportDtos = createChildReportDtos(requestDto.toChildReportRequestDto(hourPrice));

        int overallHours = childReportDtos.stream()
                .map(ChildReportDto::overallHours)
                .reduce(Integer::sum)
                .orElse(0);

        int billableHours = childReportDtos.stream()
                .map(ChildReportDto::billableHours)
                .reduce(Integer::sum)
                .orElse(0);

        BigDecimal price = childReportDtos.stream()
                .map(ChildReportDto::price)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        return new ParentReportDto(parent.getFirstName(), parent.getLastName(), overallHours, billableHours,
                price, childReportDtos);
    }

    private List<ChildReportDto> createChildReportDtos(ChildReportRequestDto requestDto) {
        List<Child> children = childRepository.findByParentIdAndSchoolId(requestDto.parentId(), requestDto.schoolId());
        BigDecimal hourPrice = HOUR_PRICE_BY_SCHOOL_ID.get(requestDto.schoolId());

        List<ChildReportDto> childReportDtos = new ArrayList<>(children.size());

        children.forEach(c -> {
            List<AttendanceReportDto> attendanceReportDto = createAttendanceReportDto(requestDto.toAttendanceReportRequest(c.getId(), hourPrice));

            int overallHours = attendanceReportDto.stream()
                    .map(AttendanceReportDto::overallHours)
                    .reduce(Integer::sum)
                    .orElse(0);

            int billableHours = attendanceReportDto.stream()
                    .map(AttendanceReportDto::billableHours)
                    .reduce(Integer::sum)
                    .orElse(0);

            BigDecimal price = attendanceReportDto.stream()
                    .map(AttendanceReportDto::price)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);

            childReportDtos.add(new ChildReportDto(c.getFirstName(), c.getLastName(), overallHours, billableHours,
                    price, attendanceReportDto));
        });
        return childReportDtos;
    }

    private List<AttendanceReportDto> createAttendanceReportDto(AttendanceReportRequestDto requestDto) {
        LocalDateTime reportBegin = toReportBegin(requestDto.year(), requestDto.month());
        LocalDateTime reportEnd = toReportEnd(requestDto.year(), requestDto.month());

        List<Attendance> attendances = attendanceRepository.findAttendances(requestDto.childId(), reportBegin,
                reportEnd);

        List<AttendanceReportDto> attendanceReportDtos = new ArrayList<>(attendances.size());

        attendances.forEach(a -> {
            LocalTime entryTime = a.getEntryDate().toLocalTime();
            LocalTime exitTime = a.getExitDate().toLocalTime();

            int billableHours = hoursCalculatorService.calculateBillableHours(entryTime, exitTime);
            int overallHours = exitTime.getHour() - entryTime.getHour() + 1;

            attendanceReportDtos.add(new AttendanceReportDto(a.getEntryDate(), a.getExitDate(), overallHours,
                    billableHours, requestDto.hourPrice().multiply(BigDecimal.valueOf(billableHours))));
        });
        return attendanceReportDtos;
    }

    private LocalDateTime toReportBegin(short year, short month) {
        return LocalDateTime.of(year, month, 1, 0, 0);
    }

    private LocalDateTime toReportEnd(short year, short month) {
        return LocalDateTime.of(year, month, 1, 0, 0).plusMonths(1);
    }
}
