package com.shavika.websocket.datasource.repository;

import com.shavika.websocket.datasource.entity.ManvishAcsLogRawData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManvishAcsLogRawDataRepository  extends JpaRepository<ManvishAcsLogRawData, Integer> {

}
