package com.itxia.backend.data.repo;

import com.itxia.backend.data.model.RequestMessage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Yzh
 */
public interface RequestMessageRepository extends JpaRepository<RequestMessage, Integer> {
}
