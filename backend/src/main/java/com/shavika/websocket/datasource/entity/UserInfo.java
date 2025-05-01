package com.shavika.websocket.datasource.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
public class UserInfo extends BaseEntity {

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "privilege", nullable = false)
    private String privilege;

    @Column(name = "company")
    private String company;

    @Column(name = "department_id")
    private String departmentId;

    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false;

    @Column(name = "user_period_used", nullable = false)
    private Boolean userPeriodUsed = false;

    @Column(name = "user_period_start")
    private LocalDateTime userPeriodStart;

    @Column(name = "user_period_end")
    private LocalDateTime userPeriodEnd;


}
