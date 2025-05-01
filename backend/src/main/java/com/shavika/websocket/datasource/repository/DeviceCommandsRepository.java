package com.shavika.websocket.datasource.repository;

import com.shavika.websocket.datasource.entity.DeviceCommands;
import com.shavika.websocket.datasource.entity.DeviceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceCommandsRepository  extends JpaRepository<DeviceCommands, String> {

    Optional<DeviceCommands> findByDeviceSerialNo(String deviceSerialNo);

}
