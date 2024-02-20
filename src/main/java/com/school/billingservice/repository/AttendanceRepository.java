package com.school.billingservice.repository;

import com.school.billingservice.domain.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query(value = """
            select distinct a.id, a.entry_date, a.exit_date, a.child_id from attendances a
            join children c on c.id = a.child_id
            where a.child_id = :childId
            and a.entry_date >= :begin and a.exit_date < :end
            order by a.id
            """, nativeQuery = true)
    List<Attendance> findAttendances(Long childId, LocalDateTime begin, LocalDateTime end);
}
