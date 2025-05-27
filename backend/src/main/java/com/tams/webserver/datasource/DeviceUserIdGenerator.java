package com.tams.webserver.datasource;

import com.tams.webserver.datasource.repository.CounterRepository;
import com.tams.webserver.datasource.entity.Counter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceUserIdGenerator {
    @Autowired
    private CounterRepository counterRepository;

    @Transactional
    public synchronized Long getNextId() {
        Counter counter = counterRepository.findById("device_user_id")
                .orElseGet(() -> new Counter("device_user_id", 1L));

        Long currentValue = counter.getValue();
        counter.setValue(currentValue + 1);
        counterRepository.save(counter);
        return currentValue;
    }
}
