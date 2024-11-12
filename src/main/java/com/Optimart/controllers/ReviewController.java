package com.Optimart.controllers;

import com.Optimart.annotations.SecuredSwaggerOperation;
import com.Optimart.constants.Endpoint;
import com.Optimart.dto.Product.CreateProductDTO;
import com.Optimart.dto.Review.DeleteMultiReviewDTO;
import com.Optimart.dto.Review.UpdateReviewDTO;
import com.Optimart.dto.Review.WriteReviewDTO;
import com.Optimart.models.Product;
import com.Optimart.models.Review;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.Review.ReviewResponse;
import com.Optimart.services.Review.ReviewService;
import com.Optimart.utils.LocalizationUtils;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@RestController
@Tag(name = "Review", description = "Everything about reviews")
@RequestMapping(Endpoint.Reivew.BASE)
public class ReviewController {
    private final LocalizationUtils localizationUtils;
    private final ReviewService reviewService;

    @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Write a new review")
    @PostMapping
    public ResponseEntity<APIResponse<ReviewResponse>> createNewReview(@RequestBody WriteReviewDTO writeReviewDTO){
        return ResponseEntity.ok(reviewService.writeNewReview(writeReviewDTO));
    }

    @PreAuthorize("hasAuthority('MANAGE_ORDER.REVIEW.UPDATE') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Product.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Update a review")
    @PutMapping(Endpoint.Reivew.ID)
    public ResponseEntity<?> updateReview(@PathVariable String reviewId, @RequestBody UpdateReviewDTO updateReviewDTO) {
        return ResponseEntity.ok(reviewService.updateReview(updateReviewDTO, reviewId));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Product.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Update a review by me")
    @PutMapping(Endpoint.Reivew.ID_ME)
    public ResponseEntity<?> updateReviewByMe(@PathVariable String reviewId, @RequestBody UpdateReviewDTO updateReviewDTO) {
        return ResponseEntity.ok(reviewService.updateReview(updateReviewDTO, reviewId));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Product.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get a existing review by id")
    @GetMapping(Endpoint.Reivew.ID)
    public ResponseEntity<?> getOneReview(@PathVariable String reviewId) {
        return ResponseEntity.ok(reviewService.getOneReview(reviewId));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Product.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get all reviews")
    @GetMapping
    public ResponseEntity<?> getAllReview(@RequestParam Map<Object, String> filters) {
        return ResponseEntity.ok(reviewService.getAllReview(filters));
    }

    @PreAuthorize("hasAuthority('MANAGE_ORDER.REVIEW.DELETE') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Product.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Delete a existing review by id")
    @DeleteMapping(Endpoint.Reivew.ID)
    public ResponseEntity<?> deleteOneReview(@PathVariable String reviewId) {
        return ResponseEntity.ok(reviewService.deleteOneReview(reviewId));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Product.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Delete a existing review by me")
    @DeleteMapping(Endpoint.Reivew.ID_ME)
    public ResponseEntity<?> deleteOneReviewByMe(@PathVariable String reviewId) {
        return ResponseEntity.ok(reviewService.deleteOneReview(reviewId));
    }

    @PreAuthorize("hasAuthority('MANAGE_ORDER.REVIEW.DELETE') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Product.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Delete multiple review")
    @DeleteMapping(Endpoint.Reivew.DELETE_MANY)
    public ResponseEntity<?> deleteMultiReview(@RequestBody DeleteMultiReviewDTO deleteMultiReviewDTO) {
        return ResponseEntity.ok(reviewService.deleteMultiReview(deleteMultiReviewDTO));
    }
}
