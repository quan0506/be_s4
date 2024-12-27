package com.fptaptech.s4.controller;


import com.fptaptech.s4.dto.Response;
import com.fptaptech.s4.entity.Review;
import com.fptaptech.s4.service.impl.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.web.multipart.MultipartFile;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    @Autowired
    private final ReviewService reviewService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> createReview(
            @RequestParam(required = false) Long branchId,
            @RequestParam(required = false) Long roomId,
            @RequestParam Integer rating,
            @RequestParam String reviewText,
            @RequestParam(required = false) List<MultipartFile> photos) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Response response = reviewService.createReview(branchId, roomId, rating, reviewText, photos, userEmail);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> getAllReviews() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Response response = reviewService.getAllReviews(userEmail);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> getReviewById(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Response response = reviewService.getReviewById(id, userEmail);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<Response> getReviewsByBranch(@PathVariable Long branchId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Response response = reviewService.getReviewsByBranch(branchId, userEmail);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<Response> getReviewsByRoom(@PathVariable Long roomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Response response = reviewService.getReviewsByRoom(roomId, userEmail);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> updateReview(
            @PathVariable Integer id,
            @RequestBody Review review,
            @RequestParam(required = false) List<MultipartFile> reviewImages) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Response response = reviewService.updateReview(id, review, reviewImages, userEmail);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> deleteReview(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Response response = reviewService.deleteReview(id, userEmail);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
