package com.school.billingservice.repository;

import com.school.billingservice.domain.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {

    @Query(value = """
            select distinct p.id from parents p
            join children c on c.parent_id = p.id
            join schools s on c.school_id = s.id
            where s.id = :schoolId
            order by p.id
            """, nativeQuery = true)
    List<Long> findParentsIds(Long schoolId);

    @Query(value = """
            select distinct p.id, p.first_name, p.last_name from parents p
            join children c on c.parent_id = p.id
            join schools s on c.school_id = s.id
            where s.id = :schoolId and p.id = :parentId
            order by p.id
            """, nativeQuery = true)
    Parent findBySchoolIdAndParentId(Long schoolId, Long parentId);
}
