package com.mo.ecommerce.controller;

import com.mo.ecommerce.dto.review.ReviewRequest;
import com.mo.ecommerce.dto.review.ReviewResponse;
import com.mo.ecommerce.entity.User;
import com.mo.ecommerce.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ReviewResponse addReview(@AuthenticationPrincipal User user,
                                   @Valid @RequestBody ReviewRequest reviewRequest) {
        return reviewService.addReview(user, reviewRequest);
    }

    @GetMapping("/my")
    public List<ReviewResponse> getMyReviews(@AuthenticationPrincipal User user) {
        return reviewService.getAllReviews(user);
    }

    @GetMapping("/product/{productId}")
    public List<ReviewResponse> getProductReviews(@PathVariable Long productId) {
        return reviewService.getProductReviews(productId);
    }

    @GetMapping("/{reviewId}")
    public ReviewResponse getReview(@PathVariable Long reviewId) {
        return reviewService.getReview(reviewId);
    }

    @PutMapping("/{reviewId}")
    public ReviewResponse updateReview(@AuthenticationPrincipal User user,
                                       @PathVariable Long reviewId,
                                       @Valid @RequestBody ReviewRequest reviewRequest) {
        return reviewService.updateReview(user, reviewId, reviewRequest);
    }

    @DeleteMapping("/{reviewId}")
    public String deleteReview(@AuthenticationPrincipal User user,
                               @PathVariable Long reviewId) {
        reviewService.deleteReview(user, reviewId);
        return "Review deleted successfully";
    }
}

