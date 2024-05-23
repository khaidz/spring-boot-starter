package net.khaibq.springbootstater.repository;

import net.khaibq.springbootstater.entity.Village;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VillageRepository extends JpaRepository<Village, Long> {
}
