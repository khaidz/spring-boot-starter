package net.khaibq.springbootstater.repository;

import net.khaibq.springbootstater.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WardRepository extends JpaRepository<Ward, Long> {
}
