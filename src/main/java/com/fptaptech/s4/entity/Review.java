package com.fptaptech.s4.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewId;

//    @ManyToOne
//    @JoinColumn(name = "customerId", nullable = false)
//    private Customer customer;

//    @ManyToOne
//    @JoinColumn(name = "serviceId", nullable = false)
//    private Service service;

    @Column(nullable = false)
    private Integer rating;

    @Column(length = 500)
    private String reviewText;

    @Column(length = 255)
    private String reviewImageURL;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Review() {
    }

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getReviewImageURL() {
        return reviewImageURL;
    }

    public void setReviewImageURL(String reviewImageURL) {
        this.reviewImageURL = reviewImageURL;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
