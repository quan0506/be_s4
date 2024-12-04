package com.fptaptech.s4.service.impl;

import com.fptaptech.s4.dto.ReviewDTO;
import com.fptaptech.s4.entity.Branch;
import com.fptaptech.s4.entity.Review;
import com.fptaptech.s4.entity.Room;
import com.fptaptech.s4.repository.BranchRepository;
import com.fptaptech.s4.repository.ReviewRepository;
import com.fptaptech.s4.repository.RoomRepository;
import com.fptaptech.s4.utils.Utils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BranchRepository branchRepository;
    private final RoomRepository roomRepository;
    private final Cloudinary cloudinary;

    public ReviewDTO createReview(Long branchId, Long roomId, Integer rating, String reviewText, MultipartFile reviewImage) {
        Review review = new Review();
        review.setRating(rating);

        if (isInputSafe(reviewText)) {
            review.setReviewText(reviewText);
        } else {
            review.setReviewText(""); // Sanitize or set error message
        }

        if (reviewImage != null && !reviewImage.isEmpty()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(reviewImage.getBytes(), ObjectUtils.emptyMap());
                review.setReviewImageURL(uploadResult.get("url").toString());
            } catch (IOException e) {
                throw new RuntimeException("Error uploading image: " + e.getMessage());
            }
        }

        if (roomId != null) {
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room Not Found"));
            review.setRoom(room);
            review.setBranch(room.getBranch()); // Automatically set the branch based on the room
        } else if (branchId != null) {
            Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new RuntimeException("Branch Not Found"));
            review.setBranch(branch);
        }

        review.setCreatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);
        return Utils.mapReviewEntityToReviewDTO(savedReview);
    }

    public List<ReviewDTO> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(Utils::mapReviewEntityToReviewDTO)
                .collect(Collectors.toList());
    }

    public ReviewDTO getReviewById(Integer reviewId) {
        return reviewRepository.findById(reviewId)
                .map(Utils::mapReviewEntityToReviewDTO)
                .orElse(null);
    }

    public ReviewDTO updateReview(Integer reviewId, Review updatedReview) {
        return reviewRepository.findById(reviewId).map(review -> {
            review.setRating(updatedReview.getRating());
            if (isInputSafe(updatedReview.getReviewText())) {
                review.setReviewText(updatedReview.getReviewText());
            } else {
                review.setReviewText(""); // Sanitize or set error message
            }
            if (updatedReview.getReviewImageURL() != null && !updatedReview.getReviewImageURL().isEmpty()) {
                review.setReviewImageURL(updatedReview.getReviewImageURL());
            }
            Review savedReview = reviewRepository.save(review);
            return Utils.mapReviewEntityToReviewDTO(savedReview);
        }).orElse(null);
    }

    public void deleteReview(Integer reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    // Check safe input
    private boolean isInputSafe(String input) {
        return Jsoup.isValid(input, Safelist.none());
    }
}
