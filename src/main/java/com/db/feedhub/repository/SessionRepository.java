package com.db.feedhub.repository;

import com.db.feedhub.model.entity.Session;
import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;

public interface SessionRepository extends ListCrudRepository<Session, UUID> {

}
