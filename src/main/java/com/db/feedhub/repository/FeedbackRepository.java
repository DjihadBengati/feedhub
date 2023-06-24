package com.db.feedhub.repository;

import com.db.feedhub.model.entity.Feedback;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FeedbackRepository extends ListCrudRepository<Feedback, UUID>,
    PagingAndSortingRepository<Feedback, UUID> {

  Page<Feedback> findByCensoredTrue(Pageable pageable);
}
