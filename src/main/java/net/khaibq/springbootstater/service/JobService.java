package net.khaibq.springbootstater.service;

import java.io.IOException;

public interface JobService {
    void initProvince() throws IOException;

    void fetchDistrict(String provinceCode);

    void fetchWard(String districtCode);

    void fetchVillage(String wardCode);
}
