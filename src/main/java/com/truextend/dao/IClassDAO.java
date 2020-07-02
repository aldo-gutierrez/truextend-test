package com.truextend.dao;

import com.truextend.model.Class0;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IClassDAO extends JpaRepository<Class0, Long> {
    Class0 findByCode(String code);
}
