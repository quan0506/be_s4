package com.fptaptech.s4.service;


import com.fptaptech.s4.entity.Comment;
import com.fptaptech.s4.entity.Review;
import com.fptaptech.s4.repository.CommentRepository;
import com.fptaptech.s4.repository.ReviewRepository;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public Comment createComment(Comment comment, Integer reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        String sanitizedCommentText = sanitizeInput(comment.getCommentText());
        comment.setCommentText(sanitizedCommentText);

        comment.setReview(review);
        return commentRepository.save(comment);
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Comment getCommentById(Integer commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }


    public Comment updateComment(Integer commentId, Comment updatedComment) {
        return commentRepository.findById(commentId).map(comment -> {
            String sanitizedCommentText = sanitizeInput(updatedComment.getCommentText());
            comment.setCommentText(sanitizedCommentText);

            comment.setCommentImageURL(updatedComment.getCommentImageURL());
            return commentRepository.save(comment);
        }).orElse(null);
    }

    public void deleteComment(Integer commentId) {
        commentRepository.deleteById(commentId);
    }


    private String sanitizeInput(String input) {

        if (input == null) {
            return null;
        }

        return Jsoup.clean(input, Safelist.none());
    }
}



