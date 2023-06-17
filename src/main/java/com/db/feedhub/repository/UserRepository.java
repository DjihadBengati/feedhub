package com.db.feedhub.repository;

import com.db.feedhub.model.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;

public interface UserRepository extends ListCrudRepository<User, UUID> {

  void deleteByEmail(String email);
  Optional<User> findByEmail(String email);
}
