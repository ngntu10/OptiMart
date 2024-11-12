package com.Optimart.controllers;

import com.Optimart.annotations.SecuredSwaggerOperation;
import com.Optimart.constants.Endpoint;
import com.Optimart.dto.CityLocale.CityLocaleDTO;
import com.Optimart.dto.CityLocale.CityLocaleSearchDTO;
import com.Optimart.dto.CityLocale.CityMutilDeleteDTO;
import com.Optimart.models.City;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.PagingResponse;
import com.Optimart.services.CityLocale.CityLocaleService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "City", description = "Everything about city")
@RequestMapping(Endpoint.CityLocale.BASE)
public class CityLocaleController {
    private final CityLocaleService cityLocaleService;

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = City.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get list city")
    @GetMapping
    public ResponseEntity<PagingResponse<List<City>>> getListCity(@ModelAttribute CityLocaleSearchDTO cityLocaleSearchDTO){
        return ResponseEntity.ok(cityLocaleService.findAll(cityLocaleSearchDTO));
    }

    @PreAuthorize("hasAuthority('CITY.CREATE') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = City.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Create a new city")
    @PostMapping
    public ResponseEntity<APIResponse<City>> createCity(@RequestBody CityLocaleDTO cityLocaleDTO){
        return ResponseEntity.ok(cityLocaleService.createType(cityLocaleDTO));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = City.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get city details by id")
    @GetMapping(Endpoint.CityLocale.ID)
    public ResponseEntity<City> getDetailsCityLocale(@PathVariable String cityId){
        return ResponseEntity.ok(cityLocaleService.getCity(cityId));
    }

    @PreAuthorize("hasAuthority('CITY.UPDATE') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = City.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Update city name by id")
    @PutMapping(Endpoint.CityLocale.ID)
    public ResponseEntity<APIResponse<City>> editCityLocale(@RequestBody CityLocaleDTO cityLocaleDTO, @PathVariable String cityId){
        return ResponseEntity.ok(cityLocaleService.editCity(cityLocaleDTO, cityId));
    }

    @PreAuthorize("hasAuthority('CITY.DELETE') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = String.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Delete a city by id")
    @DeleteMapping(Endpoint.CityLocale.ID)
    public ResponseEntity<APIResponse<Boolean>> deleteCity(@PathVariable String cityId){
        return ResponseEntity.ok(cityLocaleService.deleteCity(cityId));
    }

    @PreAuthorize("hasAuthority('CITY.DELETE') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = String.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Delete multiple city by id")
    @DeleteMapping(Endpoint.CityLocale.DELETE_MANY)
    public ResponseEntity<APIResponse<Boolean>> deleteMultiCity(@RequestBody CityMutilDeleteDTO cityMutilDeleteDTO){
        return ResponseEntity.ok(cityLocaleService.deleteMultiCity(cityMutilDeleteDTO));
    }
}