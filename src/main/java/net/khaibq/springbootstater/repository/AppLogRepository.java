package net.khaibq.springbootstater.repository;

import net.khaibq.springbootstater.entity.AppLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppLogRepository extends JpaRepository<AppLog, Long> {
}
