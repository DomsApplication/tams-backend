package com.tams.webserver.datasource.repository;

import com.tams.webserver.datasource.entity.ManvishAcsLogRawData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManvishAcsLogRawDataRepository  extends JpaRepository<ManvishAcsLogRawData, Integer> {

}
