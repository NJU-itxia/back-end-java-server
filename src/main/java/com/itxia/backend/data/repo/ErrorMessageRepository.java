package com.itxia.backend.data.repo;

import com.itxia.backend.data.model.ErrorMessage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Yzh
 */
public interface ErrorMessageRepository extends JpaRepository<ErrorMessage, Integer> {

}
