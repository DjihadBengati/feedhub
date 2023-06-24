package com.db.feedhub.repository;

import com.db.feedhub.model.entity.Token;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

public interface TokenRepository extends ListCrudRepository<Token, UUID> {

  Optional<Token> findByToken(String token);

  @Query(value = """
      select t from Token t inner join Administrator a\s
      on t.admin.id = a.id\s
      where a.id = :adminId and (t.expired = false or t.revoked = false)\s
      """)
  List<Token> findAllValidTokenByAdmin(UUID adminId);
}
