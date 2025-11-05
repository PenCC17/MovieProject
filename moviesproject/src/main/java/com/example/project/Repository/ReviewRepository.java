package com.example.project.Repository;

import com.example.project.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.example.project.Entity.Account;
import com.example.project.Entity.Movie;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{

    Review findReviewByReviewId(long reviewId);
    List<Review> findReviewByAccountId(Account account);
    List<Review> findReviewByMovieId(Movie movie);
    

}
