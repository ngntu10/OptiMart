package com.Optimart.services.CityLocale;

import com.Optimart.dto.CityLocale.CityLocaleDTO;
import com.Optimart.dto.CityLocale.CityLocaleSearchDTO;
import com.Optimart.dto.CityLocale.CityMutilDeleteDTO;
import com.Optimart.models.City;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.PagingResponse;

import java.util.List;


public interface ICityLocaleService {
    PagingResponse<List<City>> findAll(CityLocaleSearchDTO cityTypeSearchDTO);
    APIResponse<City> createType(CityLocaleDTO createCityDTO);
    City getCity(String id);
    APIResponse<City> editCity(CityLocaleDTO cityTypeDTO, String cityId);
    APIResponse<Boolean> deleteCity(String id);
    APIResponse<Boolean> deleteMultiCity(CityMutilDeleteDTO cityMutilDeleteDTO);
}
