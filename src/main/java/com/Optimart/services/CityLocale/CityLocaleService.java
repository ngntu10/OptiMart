package com.Optimart.services.CityLocale;

import com.Optimart.constants.MessageKeys;
import com.Optimart.dto.CityLocale.CityLocaleDTO;
import com.Optimart.dto.CityLocale.CityLocaleSearchDTO;
import com.Optimart.dto.CityLocale.CityMutilDeleteDTO;
import com.Optimart.models.City;
import com.Optimart.repositories.CityLocaleRepository;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.PagingResponse;
import com.Optimart.utils.LocalizationUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityLocaleService implements ICityLocaleService{
    private final CityLocaleRepository cityLocaleRepository;
    private final ModelMapper mapper;
    private final LocalizationUtils localizationUtils;

    @Override
    public PagingResponse<List<City>> findAll(CityLocaleSearchDTO cityTypeSearchDTO) {
        List<City> cityResponseList;
        Pageable pageable;
        if (cityTypeSearchDTO.getPage() == -1 && cityTypeSearchDTO.getLimit() == -1) {
            cityResponseList = cityLocaleRepository.findAll();
            return new PagingResponse<>(cityResponseList, localizationUtils.getLocalizedMessage(MessageKeys.CITY_GET_SUCCESS), 1, (long) cityResponseList.size());
        } else {
            cityTypeSearchDTO.setPage(Math.max(cityTypeSearchDTO.getPage(), 1));
            pageable = PageRequest.of(cityTypeSearchDTO.getPage() - 1, cityTypeSearchDTO.getLimit(), Sort.by("createdAt").descending());
        }
        if (StringUtils.hasText(cityTypeSearchDTO.getOrder())) {
            String order = cityTypeSearchDTO.getOrder();
            String[] orderParams = order.split("-");
            if (orderParams.length == 2) {
                Sort.Direction direction = orderParams[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
                pageable = PageRequest.of(cityTypeSearchDTO.getPage() - 1, cityTypeSearchDTO.getLimit(), Sort.by(new Sort.Order(direction, orderParams[0])));
            }
        }
        Page<City> cityPage = cityLocaleRepository.findAll(pageable);
        return new PagingResponse<>(cityPage.getContent(), localizationUtils.getLocalizedMessage(MessageKeys.CITY_GET_SUCCESS), cityPage.getTotalPages(), cityPage.getTotalElements());
    }

    @Transactional
    @Override
    public APIResponse<City> createType(CityLocaleDTO createCityDTO) {
        City city = new City();
        city.setName(createCityDTO.getName());
        cityLocaleRepository.save(city);
        return new APIResponse<>(city, localizationUtils.getLocalizedMessage(MessageKeys.CITY_CREATE_SUCCESS));
    }

    @Override
    public City getCity(String id) {
        return cityLocaleRepository.findById(Long.parseLong(id)).get();
    }

    @Override
    @Transactional
    public APIResponse<City> editCity(CityLocaleDTO cityTypeDTO, String cityId) {
        City city = cityLocaleRepository.findById(Long.parseLong(cityId)).get();
        mapper.map(cityTypeDTO, city);
        cityLocaleRepository.save(city);
        return new APIResponse<>(city, localizationUtils.getLocalizedMessage(MessageKeys.CITY_UPDATE_SUCCESS));
    }

    @Override
    @Transactional
    public APIResponse<Boolean> deleteCity(String id) {
        City city = cityLocaleRepository.findById(Long.parseLong(id)).get();
        cityLocaleRepository.delete(city);
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.CITY_DELETE_SUCCESS));
    }

    @Override
    @Transactional
    public APIResponse<Boolean> deleteMultiCity(CityMutilDeleteDTO cityMutilDeleteDTO) {
        List<String> cityIds = cityMutilDeleteDTO.getCityIds();
        cityIds.forEach(item -> {
            cityLocaleRepository.deleteById(Long.parseLong(item));
        });
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.CITY_DELETE_SUCCESS));
    }
}
