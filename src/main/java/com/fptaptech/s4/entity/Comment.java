package com.fptaptech.s4.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;

//    @ManyToOne
//    @JoinColumn(name = "customerId", nullable = false)
//    private Customer customer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reviewId", nullable = false)
    private Review review;


    @Column(length = 500)
    private String commentText;

    @Column(length = 255)
    private String commentImageURL;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Comment() {
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getCommentImageURL() {
        return commentImageURL;
    }

    public void setCommentImageURL(String commentImageURL) {
        this.commentImageURL = commentImageURL;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
