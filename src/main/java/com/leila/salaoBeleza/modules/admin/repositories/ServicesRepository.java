package com.leila.salaoBeleza.modules.admin.repositories;

import com.leila.salaoBeleza.modules.admin.entities.ServicesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ServicesRepository extends JpaRepository<ServicesEntity, UUID> {

}
