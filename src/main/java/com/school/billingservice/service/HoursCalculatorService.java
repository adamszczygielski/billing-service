package com.school.billingservice.service;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;

@Service
public class HoursCalculatorService {

    private static final LocalTime BEGIN = LocalTime.of(7, 0, 0);
    private static final LocalTime END = LocalTime.of(12, 0, 0);

    public int calculateBillableHours(LocalTime entryTime, LocalTime exitTime) {
        if (entryTime.equals(exitTime)) {
            return 0;
        }

        if (exitTime.isBefore(BEGIN)
                || ((entryTime.equals(END) || entryTime.isAfter(END)))) {

            return (int) Duration.between(entryTime, exitTime).toHours() + 1;
        }

        if (entryTime.isBefore(BEGIN)
                && ((exitTime.equals(BEGIN) || exitTime.isAfter(BEGIN)) && exitTime.isBefore(END))) {

            return (int) Duration.between(entryTime, BEGIN).toHours() + 1;
        }

        if (((entryTime.equals(BEGIN) || entryTime.isAfter(BEGIN)) && entryTime.isBefore(END))
                && ((exitTime.equals(END) || exitTime.isAfter(END)))) {

            return (int) Duration.between(END, exitTime).toHours() + 1;
        }

        if (entryTime.isBefore(BEGIN)
                && (exitTime.equals(END) || exitTime.isAfter(END))) {

            return (int) (Duration.between(entryTime, BEGIN).toHours() + Duration.between(END, exitTime).toHours() + 2);
        }

        return 0;
    }
}
