package com.study.springbatch.repository;

import com.study.springbatch.entity.WinEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WinRepository extends JpaRepository<WinEntity,Long> {

    // 승리 횟수가 조건 값과 같거나 큰 사람 조회
    Page<WinEntity> findByWinGreaterThanEqual(Long win, Pageable pageable);
}