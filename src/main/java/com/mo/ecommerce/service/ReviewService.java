package com.mo.ecommerce.service;

import com.mo.ecommerce.dto.review.ReviewRequest;
import com.mo.ecommerce.dto.review.ReviewResponse;
import com.mo.ecommerce.entity.Product;
import com.mo.ecommerce.entity.Review;
import com.mo.ecommerce.entity.User;
import com.mo.ecommerce.repository.ProductRepo;
import com.mo.ecommerce.repository.ReviewRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepo reviewRepo;
    private final ProductRepo productRepo;

    public ReviewResponse addReview(User user, ReviewRequest reviewRequest) {
        Product product = productRepo.findById(reviewRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Review review = Review.builder()
                .user(user)
                .rating(reviewRequest.getRate())
                .comment(reviewRequest.getComment())
                .product(product)
                .build();

        Review savedReview = reviewRepo.save(review);



        return mapToReviewResponse(savedReview);
    }

    public void deleteReview(User user, Long reviewId) {
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only delete your own reviews");
        }

        reviewRepo.delete(review);

    }

    public ReviewResponse getReview(Long reviewId) {
        return mapToReviewResponse(
                reviewRepo.findById(reviewId)
                        .orElseThrow(() -> new RuntimeException("Review not found"))
        );
    }


    public List<ReviewResponse> getAllReviews(User user) {
        return reviewRepo.findByUser(user)
                .stream()
                .map(this::mapToReviewResponse)
                .collect(Collectors.toList());
    }

    public List<ReviewResponse> getProductReviews(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return reviewRepo.findByProduct(product)
                .stream()
                .map(this::mapToReviewResponse)
                .collect(Collectors.toList());
    }

    public ReviewResponse updateReview(User user, Long reviewId, ReviewRequest reviewRequest) {
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only update your own reviews");
        }

        Product product = productRepo.findById(reviewRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        review.setRating(reviewRequest.getRate());
        review.setComment(reviewRequest.getComment());
        review.setProduct(product);

        Review updatedReview = reviewRepo.save(review);

        return mapToReviewResponse(updatedReview);
    }

    private ReviewResponse mapToReviewResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .rate(review.getRating())
                .comment(review.getComment())
                .productName(review.getProduct().getName())
                .username(review.getUser().getUsername())
                .build();
    }


}
