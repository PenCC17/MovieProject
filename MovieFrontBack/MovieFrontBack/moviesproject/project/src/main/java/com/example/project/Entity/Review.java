package com.example.project.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;


/*
reviewId int primary key auto_increment,
    accountId int,
    movieId int,
    reviewText varchar(255),

 */
@Entity
@Table(name="review")
@Getter @Setter @NoArgsConstructor
public class Review {
   
    @ManyToOne
    @JoinColumn(name="account_id")
    Account accountId;

    @ManyToOne
    @JoinColumn(name="movie_id")
    Movie movieId;
   
   
    @Size(max=255)
    String reviewText;


     Integer rating;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long reviewId;

    public Review(Integer rating, Account accountId, Movie movieId, String reviewText) {
        this.accountId = accountId;
        this.movieId = movieId;
        this.reviewText = reviewText;
        this.rating = rating;
    }

   
}