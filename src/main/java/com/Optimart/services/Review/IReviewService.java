package com.Optimart.services.Review;

import com.Optimart.dto.Review.DeleteMultiReviewDTO;
import com.Optimart.dto.Review.UpdateReviewDTO;
import com.Optimart.dto.Review.WriteReviewDTO;
import com.Optimart.models.Review;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.PagingResponse;
import com.Optimart.responses.Review.ReviewResponse;

import java.util.List;
import java.util.Map;

public interface IReviewService {
    APIResponse<ReviewResponse> writeNewReview(WriteReviewDTO writeReviewDTO);
    APIResponse<ReviewResponse> updateReview(UpdateReviewDTO updateReviewDTO, String reviewId);
    APIResponse<ReviewResponse> getOneReview(String reviewId);
    APIResponse<Boolean> deleteOneReview(String reviewId);
    APIResponse<Boolean> deleteOneReviewByMe(String reviewId);
    APIResponse<Boolean> deleteMultiReview(DeleteMultiReviewDTO deleteMultiReviewDTO);
    PagingResponse<List<ReviewResponse>> getAllReview(Map<Object, String> filters);

}
