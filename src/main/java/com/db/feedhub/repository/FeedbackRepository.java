package com.db.feedhub.repository;

import com.db.feedhub.model.entity.Feedback;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedbackRepository extends ListCrudRepository<Feedback, UUID>,
    PagingAndSortingRepository<Feedback, UUID> {

  Page<Feedback> findByCensoredTrue(Pageable pageable);

  @Query(value = "SELECT * FROM feedhub_feedback WHERE date_time BETWEEN :startDateTime AND :endDateTime", nativeQuery = true)
  List<Feedback> findAllByDateTimeBetween(@Param("startDateTime") LocalDateTime start,
      @Param("endDateTime") LocalDateTime end);

  @Query(value = "SELECT * FROM feedhub_feedback WHERE date_time BETWEEN :startDateTime AND :endDateTime", nativeQuery = true)
  Page<Feedback> findPageByDateTimeBetween(@Param("startDateTime") LocalDateTime start,
      @Param("endDateTime") LocalDateTime end,
      Pageable pageable);
}
