package com.example.project.Service;

import org.springframework.stereotype.Service;
import com.example.project.Repository.ReviewRepository;
import org.springframework.transaction.annotation.Transactional;
import com.example.project.Entity.Review;
import java.util.List;
import com.example.project.Entity.Account;
import com.example.project.Entity.Movie;



@Service
@Transactional
public class ReviewService {
    
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository){    
        this.reviewRepository = reviewRepository;
    }

    public Review WriteAndSubmitReview(Review rev){
        return reviewRepository.save(rev);
    }

    public List<Review> getAllReviewsByAccountId(Account accountId){
        return reviewRepository.findReviewByAccountId(accountId);
    }

    public List<Review> getAllReviewsByMovieId(Movie movieId){
        return reviewRepository.findReviewByMovieId(movieId);
    }

    public Review getReviewById(Long id){
        return reviewRepository.findReviewByReviewId(id);
    }

    public Review editReview(Long reviewId, Review rev){ 
       Review existingReview =  reviewRepository.findReviewByReviewId(reviewId);
        existingReview.setReviewText(rev.getReviewText());
        existingReview.setRating(rev.getRating());
        return reviewRepository.save(existingReview);
    }


    public void deleteReview(Review rev){
        reviewRepository.delete(rev);
    }


    //Review findReviewByName(String username);



}
