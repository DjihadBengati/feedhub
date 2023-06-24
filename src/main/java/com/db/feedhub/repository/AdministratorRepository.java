package com.db.feedhub.repository;

import com.db.feedhub.model.entity.Administrator;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AdministratorRepository extends ListCrudRepository<Administrator, UUID>,
    PagingAndSortingRepository<Administrator, UUID> {

  Optional<Administrator> findByEmail(String email);
}
