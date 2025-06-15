package com.tams.webserver.datasource.repository;

import com.tams.webserver.datasource.entity.DeviceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceInfoRepository  extends JpaRepository<DeviceInfo, String> {

    Optional<DeviceInfo> findByDeviceSerialNo(String deviceSerialNo);

    @Modifying
    @Query("update DeviceInfo di set di.isRegistered = true where di.cloudId = :deviceSerialNo")
    int registerDevice(@Param("deviceSerialNo") String deviceSerialNo);

    @Query("select di from DeviceInfo di where di.isRegistered = true")
    List<DeviceInfo> findAllRegistered();

    @Query("select di from DeviceInfo di where di.isRegistered = false or di.isRegistered = null")
    List<DeviceInfo> findAllUnRegistered();
}
