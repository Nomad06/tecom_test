package com.bubalex.test.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageUtils {

    public static final char DESC_SYMBOL = '-';
    public static final char ASC_SYMBOL = '+';

    public static Pageable getPageable(Long pageNumber, Long pageSize, List<String> sortValues) {
        List<Sort.Order> orders = sortValues.stream()
                .map(sortValue -> {
                    char ordBySymbol = sortValue.charAt(0);
                    if (ordBySymbol == DESC_SYMBOL) {
                        return Sort.Order.asc(sortValue.replace(String.valueOf(DESC_SYMBOL), "").trim());
                    }
                    if (ordBySymbol == ASC_SYMBOL) {
                        return Sort.Order.desc(sortValue.replace(String.valueOf(ASC_SYMBOL), "").trim());
                    }
                    return Sort.Order.desc(sortValue.trim());
                })
                .collect(Collectors.toList());
        Sort sort = Sort.by(orders);

        return PageRequest.of(pageNumber.intValue() - 1, pageSize.intValue(), sort);
    }

}
