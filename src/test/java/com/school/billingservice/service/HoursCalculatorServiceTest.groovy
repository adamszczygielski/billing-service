package com.school.billingservice.service

import spock.lang.Specification

import java.time.LocalTime


class HoursCalculatorServiceTest extends Specification {

    HoursCalculatorService hoursCalculatorService = new HoursCalculatorService()

    def "should calculate billable hours"() {
        when:
        def calculatedHours = hoursCalculatorService.calculateBillableHours(LocalTime.parse(entryDate),
                LocalTime.parse(exitDate))

        then:
        calculatedHours == expectedHours

        where:
        entryDate  | exitDate   || expectedHours
        "00:00:00" | "00:00:00" || 0
        "00:00:00" | "00:00:01" || 1
        "00:00:00" | "06:59:59" || 7
        "00:00:00" | "11:59:59" || 8
        "07:00:00" | "11:59:59" || 0
        "06:59:59" | "12:00:00" || 2
        "11:00:00" | "12:00:00" || 1
        "12:00:00" | "12:59:59" || 1
        "13:00:00" | "14:59:59" || 2
        "13:00:00" | "15:00:00" || 3
    }
}
