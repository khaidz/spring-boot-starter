package net.khaibq.springbootstater.utils;

import net.khaibq.springbootstater.dto.PageDto;
import org.springframework.data.domain.Page;

public class PageUtils {
    public static PageDto getPage(Page<?> page) {
        PageDto pageDto = new PageDto();
        pageDto.setTotalElement(page.getTotalElements());
        pageDto.setTotalPage(page.getTotalPages());
        pageDto.setCurrentPage(page.getPageable().getPageNumber());
        pageDto.setSize(page.getPageable().getPageSize());
        return pageDto;
    }
}
