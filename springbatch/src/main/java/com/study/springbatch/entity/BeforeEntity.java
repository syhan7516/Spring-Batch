package com.study.springbatch.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class BeforeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String userName;

    @Builder
    public BeforeEntity(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }
}
