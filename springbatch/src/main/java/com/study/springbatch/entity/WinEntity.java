package com.study.springbatch.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WinEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long winId;
    private String userName;
    
    // 승리 횟수
    private Long win;

    // 보상 지급 여부
    private Boolean reward;

    // 보상 지급 여부 변경 메서드
    public void changeReward() {
        this.reward = true;
    }

    @Builder
    public WinEntity(Long winId, String userName, Long win, Boolean reward) {
        this.winId = winId;
        this.userName = userName;
        this.win = win;
        this.reward = reward;
    }
}
