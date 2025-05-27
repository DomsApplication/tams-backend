package com.tams.webserver.datasource.repository;

import com.tams.webserver.datasource.entity.Counter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounterRepository extends JpaRepository<Counter, String> {}

