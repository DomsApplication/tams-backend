package com.tams.webserver.datasource.repository;

import com.tams.webserver.datasource.entity.DeviceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceInfoRepository  extends JpaRepository<DeviceInfo, String> {

    Optional<DeviceInfo> findByDeviceSerialNo(String deviceSerialNo);

}
