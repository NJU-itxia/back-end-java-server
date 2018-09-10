package com.itxia.backend.data.repo;

import com.itxia.backend.data.model.ItxiaMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItxiaMemberRepository extends JpaRepository<ItxiaMember, Integer> {
}
