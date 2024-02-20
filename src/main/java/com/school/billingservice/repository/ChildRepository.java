package com.school.billingservice.repository;

import com.school.billingservice.domain.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {

    @Query(value = """
            select distinct c.id, c.first_name, c.last_name, c.parent_id, c.school_id from children c
            join parents p on c.parent_id = p.id
            join schools s on c.school_id = s.id
            where s.id = :schoolId and p.id = :parentId
            """, nativeQuery = true)
    List<Child> findByParentIdAndSchoolId(Long parentId, Long schoolId);
}
