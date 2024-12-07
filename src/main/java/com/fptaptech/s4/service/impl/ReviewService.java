package com.fptaptech.s4.service.impl;

import com.fptaptech.s4.dto.Response;
import com.fptaptech.s4.dto.ReviewDTO;
import com.fptaptech.s4.entity.Branch;
import com.fptaptech.s4.entity.Review;
import com.fptaptech.s4.entity.Room;
import com.fptaptech.s4.entity.User;
import com.fptaptech.s4.repository.BranchRepository;
import com.fptaptech.s4.repository.ReviewRepository;
import com.fptaptech.s4.repository.RoomRepository;
import com.fptaptech.s4.repository.UserRepository;
import com.fptaptech.s4.utils.Utils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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
    private final UserRepository userRepository;
    private final Cloudinary cloudinary;

    public Response createReview(Long branchId, Long roomId, Integer rating, String reviewText, MultipartFile reviewImage, String userEmail) {
        Review review = new Review();
        review.setRating(rating);

        if (isInputSafe(reviewText)) {
            review.setReviewText(reviewText);
        } else {
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Review text contains unsafe content");
            return response;
        }

        if (reviewImage != null && !reviewImage.isEmpty()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(reviewImage.getBytes(), ObjectUtils.emptyMap());
                review.setReviewImageURL(uploadResult.get("url").toString());
            } catch (IOException e) {
                Response response = new Response();
                response.setStatusCode(500);
                response.setMessage("Error uploading image: " + e.getMessage());
                return response;
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
        reviewRepository.save(review);

        Response response = new Response();
        response.setStatusCode(200);
        response.setMessage("Review created successfully");
        response.setData(Utils.mapReviewEntityToReviewDTO(review, userEmail));
        return response;
    }

    public Response getAllReviews(String userEmail) {
        List<Review> reviews = reviewRepository.findAll(); // Lấy tất cả reviews từ repository
        List<ReviewDTO> reviewDTOs = reviews.stream()
                .map(review -> Utils.mapReviewEntityToReviewDTO(review, userEmail)) // Chuyển đổi thành DTO
                .collect(Collectors.toList());

        Response response = new Response();
        response.setStatusCode(200);
        response.setMessage("Fetched all reviews successfully");
        response.setData(reviewDTOs);
        return response;
    }


    public Response getReviewById(Integer reviewId, String userEmail) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        Response response = new Response();
        if (review != null) {
            ReviewDTO reviewDTO = Utils.mapReviewEntityToReviewDTO(review, userEmail);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setData(reviewDTO);
        } else {
            response.setStatusCode(404);
            response.setMessage("Review not found");
        }
        return response;
    }

    public Response getReviewsByBranch(Long branchId, String userEmail) {
        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new RuntimeException("Branch Not Found"));
        List<ReviewDTO> reviews = reviewRepository.findByBranchAndRoomIsNull(branch).stream()
                .map(review -> Utils.mapReviewEntityToReviewDTO(review, userEmail))
                .collect(Collectors.toList());
        Response response = new Response();
        response.setStatusCode(200);
        response.setMessage("successful");
        response.setData(reviews);
        return response;
    }

    public Response getReviewsByRoom(Long roomId, String userEmail) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room Not Found"));
        List<ReviewDTO> reviews = reviewRepository.findByRoom(room).stream()
                .map(review -> Utils.mapReviewEntityToReviewDTO(review, userEmail))
                .collect(Collectors.toList());
        Response response = new Response();
        response.setStatusCode(200);
        response.setMessage("successful");
        response.setData(reviews);
        return response;
    }

    public Response updateReview(Integer reviewId, Review updatedReview, String userEmail) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review Not Found"));
        if (!userEmail.equals(userEmail)) {
            throw new AccessDeniedException("You can only update your own reviews.");
        }

        review.setRating(updatedReview.getRating());
        if (isInputSafe(updatedReview.getReviewText())) {
            review.setReviewText(updatedReview.getReviewText());
        } else {
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Review text contains unsafe content");
            return response;
        }
        if (updatedReview.getReviewImageURL() != null && !updatedReview.getReviewImageURL().isEmpty()) {
            review.setReviewImageURL(updatedReview.getReviewImageURL());
        }
        reviewRepository.save(review);

        Response response = new Response();
        response.setStatusCode(200);
        response.setMessage("Review updated successfully");
        response.setData(Utils.mapReviewEntityToReviewDTO(review, userEmail));
        return response;
    }

    public Response deleteReview(Integer reviewId, String userEmail) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review Not Found"));

        if (!userEmail.equals(userEmail)) {
            throw new AccessDeniedException("You can only delete your own reviews.");
        }

        reviewRepository.deleteById(reviewId);

        Response response = new Response();
        response.setStatusCode(200);
        response.setMessage("Review deleted successfully");
        return response;
    }

    // Check safe input
    private boolean isInputSafe(String input) {
        return Jsoup.isValid(input, Safelist.none());
    }
}
