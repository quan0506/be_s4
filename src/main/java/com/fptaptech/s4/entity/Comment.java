//package com.fptaptech.s4.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Table(name = "Comments")
//public class Comment {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer commentId;
//
////    @ManyToOne
////    @JoinColumn(name = "customerId", nullable = false)
////    private Customer customer;
//
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "reviewId", nullable = false)
//    private Review review;
//
//
//    @Column(length = 500)
//    private String commentText;
//
//    @Column(columnDefinition = "LONGBLOB")
//    private byte[] commentImageURL;
//
//    @Column(nullable = false)
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//}
