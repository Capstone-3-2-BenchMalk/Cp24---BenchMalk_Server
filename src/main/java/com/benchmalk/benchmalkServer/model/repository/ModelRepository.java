package com.benchmalk.benchmalkServer.model.repository;

import com.benchmalk.benchmalkServer.model.domain.Model;
import com.benchmalk.benchmalkServer.model.domain.ModelType;
import com.benchmalk.benchmalkServer.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {
    Boolean existsByNameAndModelType(String name, ModelType modelType);

    List<Model> findByUser(User user);

    List<Model> findByModelType(ModelType modelType);

    Boolean existsByNameAndUser(String name, User user);
}
