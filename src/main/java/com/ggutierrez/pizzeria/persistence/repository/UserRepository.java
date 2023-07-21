package com.ggutierrez.pizzeria.persistence.repository;

import com.ggutierrez.pizzeria.persistence.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, String> {
}
