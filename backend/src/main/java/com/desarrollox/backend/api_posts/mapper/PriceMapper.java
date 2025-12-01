package com.desarrollox.backend.api_posts.mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.desarrollox.backend.api_posts.controller.dto.PriceDto;
import com.desarrollox.backend.api_posts.model.PostPrice;

@Component
public class PriceMapper {

    public List<PriceDto> toPriceDto(List<PostPrice> byPostId) {
        return byPostId.stream()
                .map(postPrice -> PriceDto.builder()
                    .codeCountry(postPrice.getCodeCountry())
                    .country(postPrice.getCountry())
                    .amount(postPrice.getAmount())
                    .currency(postPrice.getCurrency())
                    .build()
                ).collect(Collectors.toList());
    }
    
}