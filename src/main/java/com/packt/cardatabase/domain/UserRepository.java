package com.packt.cardatabase.domain;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

// exported를 false로 설정하면 리포지터리가 REST 리소스로 노출되지 않는다.
@RepositoryRestResource(exported = false)
public interface UserRepository extends CrudRepository<User, Long>{
	Optional<User> findByUsername(String username);
}
