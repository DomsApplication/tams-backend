package com.tams.webserver.datasource.repository;

import com.tams.webserver.datasource.entity.DeviceCommands;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceCommandsRepository  extends JpaRepository<DeviceCommands, String> {

    Optional<DeviceCommands> findByDeviceSerialNo(String deviceSerialNo);

}
