package com.Optimart.controllers;

import com.Optimart.annotations.SecuredSwaggerOperation;
import com.Optimart.constants.Endpoint;
import com.Optimart.responses.APIResponse;
import com.Optimart.services.Report.ReportService;
import com.Optimart.utils.LocalizationUtils;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "Report", description = "Everything about report")
@RequestMapping(Endpoint.Report.BASE)
public class ReportController {
    private final LocalizationUtils localizationUtils;
    private final ReportService reportService;

    @ApiResponse(responseCode = "200",description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get statistics user type")
    @GetMapping(Endpoint.Report.USER_TYPE)
    public ResponseEntity<?> countUserType(){
        try {
            return ResponseEntity.ok(reportService.getUserTypeCount());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new APIResponse<>(null, ex.getMessage()));
        }
    }

    @ApiResponse(responseCode = "200",description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get statistics product status")
    @GetMapping(Endpoint.Report.PRODUCT_STATUS)
    public ResponseEntity<?> countProductStatus(){
        try {
            return ResponseEntity.ok(reportService.getProductStatusCount());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new APIResponse<>(null, ex.getMessage()));
        }
    }

    @ApiResponse(responseCode = "200",description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get statistics all records")
    @GetMapping(Endpoint.Report.ALL_RECORDS)
    public ResponseEntity<?> countAllRecords(){
        try {
            return ResponseEntity.ok(reportService.getModelStatistics());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new APIResponse<>(null, ex.getMessage()));
        }
    }

    @ApiResponse(responseCode = "200",description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get statistics product type")
    @GetMapping(Endpoint.Report.PRODUCT_TYPE)
    public ResponseEntity<?> countProductType(){
        try {
            return ResponseEntity.ok(reportService.getProductTypeStats());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new APIResponse<>(null, ex.getMessage()));
        }
    }
}
