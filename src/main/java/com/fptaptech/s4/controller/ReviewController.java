package com.fptaptech.s4.controller;


import com.fptaptech.s4.dto.ReviewDTO;
import com.fptaptech.s4.entity.Review;
import com.fptaptech.s4.service.impl.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    @Autowired
    private final ReviewService reviewService;

    @PostMapping("/createReview")
    public ResponseEntity<ReviewDTO> createReview(
            @RequestParam Long branchId,
            @RequestParam(required = false) Long roomId,
            @RequestParam Integer rating,
            @RequestParam String reviewText,
            @RequestParam(required = false) MultipartFile reviewImage) {
        ReviewDTO createdReview = reviewService.createReview(branchId, roomId, rating, reviewText, reviewImage);
        return ResponseEntity.ok(createdReview);
    }

    @GetMapping("/getReview/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Integer id) {
        ReviewDTO review = reviewService.getReviewById(id);
        return review != null ? ResponseEntity.ok(review) : ResponseEntity.notFound().build();
    }

    @PutMapping("/updateReview/{id}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Integer id, @RequestBody Review review) {
        ReviewDTO updatedReview = reviewService.updateReview(id, review);
        return updatedReview != null ? ResponseEntity.ok(updatedReview) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/deleteReview/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Integer id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
