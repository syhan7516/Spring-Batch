package com.study.springbatch.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;

@Entity
public class AfterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String userName;

    @Builder
    public AfterEntity(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }
}
