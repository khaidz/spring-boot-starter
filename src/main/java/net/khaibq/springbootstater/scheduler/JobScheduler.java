package net.khaibq.springbootstater.scheduler;

import lombok.RequiredArgsConstructor;
import net.khaibq.springbootstater.entity.District;
import net.khaibq.springbootstater.entity.Province;
import net.khaibq.springbootstater.entity.Ward;
import net.khaibq.springbootstater.repository.DistrictRepository;
import net.khaibq.springbootstater.repository.ProvinceRepository;
import net.khaibq.springbootstater.repository.WardRepository;
import net.khaibq.springbootstater.service.JobService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JobScheduler {
    private final JobService jobService;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;


//    @Scheduled(cron = "0 0 0 * * ?")
    public void fetchDistricts() {
        List<String> provinceCodes = provinceRepository.findAll()
                .stream().map(Province::getCode)
                .toList();
        for(String provinceCode: provinceCodes){
            jobService.fetchDistrict(provinceCode);
        }
    }

//    @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(fixedDelay = 36000000)
    public void fetchWards() {
        List<String> districtCodes = districtRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream().filter(x -> x.getId() > 612)
                .map(District::getCode)
                .toList();
        for(String districtCode: districtCodes){
            jobService.fetchWard(districtCode);
        }
    }

    @Scheduled(fixedDelay = 36000000)
    public void fetchVillages() {
        int pageNumber = 1;
        int pageSize = 7000;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        List<String> wardCodes = wardRepository.findAll(pageable)
                .stream()
                .map(Ward::getCode)
                .toList();
        for(String wardCode: wardCodes){
            jobService.fetchVillage(wardCode);
        }
    }
}
