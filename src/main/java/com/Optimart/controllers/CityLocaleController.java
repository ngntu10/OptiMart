package com.Optimart.controllers;

import com.Optimart.constants.Endpoint;
import com.Optimart.dto.CityLocale.CityLocaleDTO;
import com.Optimart.dto.CityLocale.CityLocaleSearchDTO;
import com.Optimart.dto.CityLocale.CityMutilDeleteDTO;
import com.Optimart.dto.deliveryType.DeliveryTypeDTO;
import com.Optimart.dto.deliveryType.DeliveryTypeMutilDeleteDTO;
import com.Optimart.dto.deliveryType.DeliveryTypeSearchDTO;
import com.Optimart.services.CityLocale.CityLocaleService;
import com.Optimart.services.DeliveryType.DeliveryTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "City", description = "Everything about delivery city")
@RequestMapping(Endpoint.CityLocale.BASE)
public class CityLocaleController {
    private final CityLocaleService cityLocaleService;
    @GetMapping
    public ResponseEntity<?> getListCity(@ModelAttribute CityLocaleSearchDTO cityLocaleSearchDTO){
        return ResponseEntity.ok(cityLocaleService.findAll(cityLocaleSearchDTO));
    }

    @PostMapping
    public ResponseEntity<?> createCity(@RequestBody CityLocaleDTO cityLocaleDTO){
        return ResponseEntity.ok(cityLocaleService.createType(cityLocaleDTO));
    }

    @GetMapping(Endpoint.CityLocale.ID)
    public ResponseEntity<?> getDetailsCityLocale(@PathVariable String cityId){
        return ResponseEntity.ok(cityLocaleService.getCity(cityId));
    }

    @PutMapping(Endpoint.CityLocale.ID)
    public ResponseEntity<?> editCityLocale(@RequestBody CityLocaleDTO cityLocaleDTO, @PathVariable String cityId){
        return ResponseEntity.ok(cityLocaleService.editCity(cityLocaleDTO, cityId));
    }

    @DeleteMapping(Endpoint.CityLocale.ID)
    public ResponseEntity<?> deleteCity(@PathVariable String cityId){
        return ResponseEntity.ok(cityLocaleService.deleteCity(cityId));
    }

    @DeleteMapping(Endpoint.CityLocale.DELETE_MANY)
    public ResponseEntity<?> deleteMutiCity(@RequestBody CityMutilDeleteDTO cityMutilDeleteDTO){
        return ResponseEntity.ok(cityLocaleService.deleteMutilCity(cityMutilDeleteDTO));
    }
}