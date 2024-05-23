package net.khaibq.springbootstater.repository;

import net.khaibq.springbootstater.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvinceRepository extends JpaRepository<Province, Long> {
}
