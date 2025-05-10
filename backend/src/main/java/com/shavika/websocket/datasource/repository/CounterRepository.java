package com.shavika.websocket.datasource.repository;

import com.shavika.websocket.datasource.entity.Counter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounterRepository extends JpaRepository<Counter, String> {}

