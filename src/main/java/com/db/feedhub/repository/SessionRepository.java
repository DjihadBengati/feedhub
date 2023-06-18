package com.db.feedhub.repository;

import com.db.feedhub.model.entity.Session;
import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SessionRepository extends ListCrudRepository<Session, UUID>,
    PagingAndSortingRepository<Session, UUID> {

}
