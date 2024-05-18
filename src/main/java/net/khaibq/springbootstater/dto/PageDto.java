package net.khaibq.springbootstater.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageDto {
    private Long totalElement;
    private Integer totalPage;
    private Integer currentPage;
    private Integer size;
}
