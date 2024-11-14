package com.fptaptech.s4.service.impl;

import com.fptaptech.s4.entity.Review;
import com.fptaptech.s4.repository.ReviewRepository;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review createReview(Review review) {
        // handle reviewText to prevent XSS
        if (isInputSafe(review.getReviewText())) {
            review.setReviewText(review.getReviewText());
        } else {
            review.setReviewText(""); // error message
        }
        return reviewRepository.save(review);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review getReviewById(Integer reviewId) {
        return reviewRepository.findById(reviewId).orElse(null);
    }

    public Review updateReview(Integer reviewId, Review updatedReview) {
        return reviewRepository.findById(reviewId).map(review -> {
            review.setRating(updatedReview.getRating());
            if (isInputSafe(updatedReview.getReviewText())) {
                review.setReviewText(updatedReview.getReviewText());
            } else {
                review.setReviewText(""); // error message
            }
            review.setReviewImageURL(updatedReview.getReviewImageURL());
            return reviewRepository.save(review);
        }).orElse(null);
    }

    public void deleteReview(Integer reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    // Check safe input
    private boolean isInputSafe(String input) {
        return Jsoup.isValid(input, Safelist.none()); // Check validity
    }
}


