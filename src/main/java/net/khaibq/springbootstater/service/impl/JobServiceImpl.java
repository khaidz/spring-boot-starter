package net.khaibq.springbootstater.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.khaibq.springbootstater.entity.AppLog;
import net.khaibq.springbootstater.entity.District;
import net.khaibq.springbootstater.entity.Province;
import net.khaibq.springbootstater.entity.Village;
import net.khaibq.springbootstater.entity.Ward;
import net.khaibq.springbootstater.repository.AppLogRepository;
import net.khaibq.springbootstater.repository.DistrictRepository;
import net.khaibq.springbootstater.repository.ProvinceRepository;
import net.khaibq.springbootstater.repository.VillageRepository;
import net.khaibq.springbootstater.repository.WardRepository;
import net.khaibq.springbootstater.service.JobService;
import net.khaibq.springbootstater.utils.CommonUtils;
import org.modelmapper.ModelMapper;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class JobServiceImpl implements JobService {
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;
    private final VillageRepository villageRepository;
    private final AppLogRepository appLogRepository;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;

    @Override
    public void initProvince() {
        Resource resource = new ClassPathResource("data/province.json");
        try (FileInputStream file = new FileInputStream(resource.getFile())) {
            List<Map<String, String>> provinces = objectMapper.readValue(file, List.class);
            List<Province> entities = provinces.stream().map(item -> {
                Province province = new Province();
                province.setCode(item.get("code"));
                province.setNameVn(item.get("value"));
                province.setNameEn(CommonUtils.removeAccent(item.get("value")));
                return province;
            }).sorted(Comparator.comparing(Province::getCode)).toList();
            provinceRepository.saveAll(entities);
            log.info("initProvince Success");

        } catch (Exception exception) {
            log.info("initProvince Error: {}", exception.getMessage());
        }
    }

    @Async
    @SneakyThrows
    @Override
    public void fetchDistrict(String provinceCode) {
        log.info("Start fetchDistrict: provinceCode = {}", provinceCode);
        String url = "https://baohiemxahoi.gov.vn/UserControls/BHXH/BaoHiemYTe/HienThiHoGiaDinh/AjaxPost.aspx/GetHuyenByLstmatinh";
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("lstmatinh", provinceCode);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Object> requestEntity = new HttpEntity<>(reqBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class);

        AppLog appLog = new AppLog();
        appLog.setUrl(url);
        appLog.setCode(provinceCode);
        appLog.setStatus(responseEntity.getStatusCode().toString());
        appLog.setCreatedBy("AUTO");
        appLog.setCreatedDate(LocalDateTime.now());
        appLogRepository.save(appLog);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String json = responseEntity.getBody();
            Map<String, Object> response = objectMapper.readValue(json, Map.class);

            Object d = response.get("d");
            JSONArray jsonArr = new JSONArray(d.toString());
            List<District> districts = new ArrayList<>();
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject jsonObj = jsonArr.getJSONObject(i);
                District district = new District();
                district.setProvinceCode(provinceCode);
                district.setCode(jsonObj.getString("MaHuyen"));
                district.setNameVn(jsonObj.getString("TenHuyen"));
                district.setNameEn(CommonUtils.removeAccent(jsonObj.getString("TenHuyen")));
                districts.add(district);
            }
            districtRepository.saveAll(districts);
        }
        Thread.sleep(5000);
    }

    @Async
    @SneakyThrows
    @Override
    public void fetchWard(String districtCode) {
        log.info("Start fetchWard: districtCode = {}", districtCode);
        String url = "https://baohiemxahoi.gov.vn/UserControls/BHXH/BaoHiemYTe/HienThiHoGiaDinh/AjaxPost.aspx/GetPhuongBylstmahuyen";
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("lstmahuyen", districtCode);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Object> requestEntity = new HttpEntity<>(reqBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class);

        AppLog appLog = new AppLog();
        appLog.setUrl(url);
        appLog.setCode(districtCode);
        appLog.setStatus(responseEntity.getStatusCode().toString());
        appLog.setCreatedBy("AUTO");
        appLog.setCreatedDate(LocalDateTime.now());
        appLogRepository.save(appLog);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String json = responseEntity.getBody();
            Map<String, Object> response = objectMapper.readValue(json, Map.class);

            Object d = response.get("d");
            JSONArray jsonArr = new JSONArray(d.toString());
            List<Ward> wards = new ArrayList<>();
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject jsonObj = jsonArr.getJSONObject(i);
                Ward ward = new Ward();
                ward.setDistrictCode(districtCode);
                ward.setCode(jsonObj.getString("MaXa"));
                ward.setNameVn(jsonObj.getString("TenXa"));
                ward.setNameEn(CommonUtils.removeAccent(jsonObj.getString("TenXa")));
                wards.add(ward);
            }
            wardRepository.saveAll(wards);
        }
        Thread.sleep(5000);
    }

    @Async
    @SneakyThrows
    @Override
    public void fetchVillage(String wardCode) {
        log.info("Start fetchVillage: wardCode = {}", wardCode);
        String url = "https://baohiemxahoi.gov.vn/UserControls/BHXH/BaoHiemYTe/HienThiHoGiaDinh/AjaxPost.aspx/GetThonBylstmaxa";
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("lstmaxa", wardCode);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Object> requestEntity = new HttpEntity<>(reqBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class);

        AppLog appLog = new AppLog();
        appLog.setUrl(url);
        appLog.setCode(wardCode);
        appLog.setStatus(responseEntity.getStatusCode().toString());
        appLog.setCreatedBy("AUTO");
        appLog.setCreatedDate(LocalDateTime.now());
        appLogRepository.save(appLog);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String json = responseEntity.getBody();
            Map<String, Object> response = objectMapper.readValue(json, Map.class);

            Object d = response.get("d");
            JSONArray jsonArr = new JSONArray(d.toString());
            List<Village> villages = new ArrayList<>();
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject jsonObj = jsonArr.getJSONObject(i);
                Village village = new Village();
                village.setWardCode(wardCode);
                village.setCode(jsonObj.getString("MaThon"));
                village.setNameVn(jsonObj.getString("TenThon"));
                village.setNameEn(CommonUtils.removeAccent(jsonObj.getString("TenThon")));
                villages.add(village);
            }
            villageRepository.saveAll(villages);
        }
        Thread.sleep(5000);
    }
}
