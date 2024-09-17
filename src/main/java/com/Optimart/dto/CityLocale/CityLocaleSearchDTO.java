package com.Optimart.dto.CityLocale;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CityLocaleSearchDTO {
    private int limit = -1;
    private int page = -1;
    private String search;
    private String order;
}
