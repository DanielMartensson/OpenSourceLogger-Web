package se.danielmartensson.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import se.danielmartensson.entities.Data;

@Repository
public interface DataRepository extends JpaRepository<Data, Long> {
	
	long countByJobName(String jobName);

	Data findFirstByJobNameOrderByLocalDateTimeDesc(String jobName);
	
	void deleteByJobName(String jobName);
	
	@Transactional
	@Modifying
	@Query("UPDATE Data data SET data.jobName = :newJobName WHERE data.jobName = :jobName")
    void updateJobNameWhereJobName(@Param("newJobName") String newJobName, @Param("jobName") String jobName);
		
	@Transactional
	@Query(value = "SELECT * FROM data WHERE job_name = :jobName ORDER BY local_date_time ASC LIMIT :selectedLimit OFFSET :selectedOffset" , nativeQuery = true)
    List<Data> findByJobNameOrderByLocalDateTimeAscLimit(@Param("jobName") String jobName, @Param("selectedOffset") long selectedOffset, @Param("selectedLimit") long selectedLimit);

	// If step = 1, please use "findByJobNameOrderByLocalDateTimeAscLimit" instead. Notice that \\:= is Hibernate SQL stuff
	@Transactional
	@Query(value = "SELECT * FROM (SELECT @row \\:= @row + 1 AS rownum, D.* FROM (SELECT @row \\:= 0) r, (SELECT * FROM data WHERE job_name = :jobName ORDER BY local_date_time ASC LIMIT :selectedLimit OFFSET :selectedOffset) D) RANKED WHERE rownum % :step = 1", nativeQuery = true)
    List<Data> findByJobNameOrderByLocalDateTimeAscLimitStep(@Param("jobName") String jobName, @Param("selectedOffset") long selectedOffset, @Param("selectedLimit") long selectedLimit, @Param("step") long step);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM data WHERE id IN (SELECT * FROM (SELECT id FROM data WHERE job_name = :jobName ORDER BY local_date_time ASC LIMIT :selectedLimit OFFSET :selectedOffset) as t)",  nativeQuery = true)
	void deleteByJobNameOrderByLocalDateTimeAscLimit(@Param("jobName") String jobName, @Param("selectedOffset") long selectedOffset, @Param("selectedLimit") long selectedLimit);
}