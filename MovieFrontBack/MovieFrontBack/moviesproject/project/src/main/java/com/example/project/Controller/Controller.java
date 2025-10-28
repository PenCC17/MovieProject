package com.example.project.Controller;

import com.example.project.Entity.Account;
import com.example.project.Entity.Movie;
import com.example.project.Entity.Review;

import com.example.project.Service.AccountService;
import com.example.project.Service.MovieService;
import com.example.project.Service.ReviewService;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@RestController
public class Controller {
    private final AccountService accountService;
    private final MovieService movieService;
    private final ReviewService reviewService;

    public Controller(AccountService accountService, MovieService movieService, ReviewService reviewService) {
        this.accountService = accountService;
        this.movieService = movieService;
        this.reviewService = reviewService;
    }

   //THEJA ACCOUNT CODE


    @PostMapping("/register")
    public ResponseEntity<Account> userRegistration(@RequestBody Account account){
    //     System.out.println("username of trying to register is " + account.getUsername());
        
        
       for (Account a : accountService.findAllAccounts()){
        if (account.getUsername().equals(a.getUsername())){
            return ResponseEntity.status(409).body(account);
        }
       }
        accountService.register(account);
        return ResponseEntity.status(200).body(account);
    }
    //THEJA ACCOUNT CODE END ^   

     //Isaac - Movie Edits- Begin

    @GetMapping("/movie")
    public ResponseEntity<List<Movie>> getAllMovies(){
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/movie/{name}")
    public ResponseEntity<Movie> getMovieByName(@PathVariable String name){ 
        if (movieService.getMovieByName(name) == null){
            return ResponseEntity.status(400).body(null); //No returns as of right now return via status code
        }
        return ResponseEntity.ok(movieService.getMovieByName(name));
    }
    @PostMapping("/movie/add")
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie){
        for (Movie m : movieService.getAllMovies()){
            if (m == movie){
                return ResponseEntity.status(400).body(null); 
            }
        }
        return ResponseEntity.status(201).body(movieService.addMovie(movie));
    }

    @DeleteMapping("/movie/delete/{id}")
    public void deleteMovie(@PathVariable Long id){
        movieService.deleteMovie(id);
    }

    // @GetMapping("/movie/{director}")
    // public ResponseEntity<List<Movie>> getMoviesByDirector(@PathVariable String director){
    //     return ResponseEntity.ok(movieService.getMoviesByDirector(director));
    // }

    // @GetMapping("/movie/{releaseYear}")
    // public ResponseEntity<List<Movie>> getMoviesByReleaseYear(@PathVariable int releaseYear){
    //     return ResponseEntity.ok(movieService.getMoviesByReleaseYear(releaseYear));
    // }

    //Isaac - Movie Edits- End

    @PostMapping("/reviews/{movieName}")
    public ResponseEntity<Review> submitReview(@RequestBody Review rev, @PathVariable String movieName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //get current auth info
        String currentUsername = authentication.getName(); //get current username based on auth info

        System.out.println(authentication);
        if (accountService.findByUsername(currentUsername) == null){
            return ResponseEntity.status(401).body(null);  //unauthorized
        }
        if (movieService.getMovieByName(movieName) == null){
            return ResponseEntity.status(400).body(null); //bad request
        }
        rev.setAccountId(accountService.findByUsername(currentUsername));
        rev.setMovieId(movieService.getMovieByName(movieName));

        return ResponseEntity.status(201).body(reviewService.WriteAndSubmitReview(rev));
    }

    @GetMapping("/reviews/movie/{movieId}")
    public ResponseEntity<List<Review>> getReviewsByMovieID(@PathVariable Long movieId) {
        if( movieService.getMovieById(movieId) == null) 
        {
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.ok(reviewService.getAllReviewsByMovieId(movieService.getMovieById(movieId)));
    }

    @GetMapping("/reviews/account/{accountId}")
    public ResponseEntity<List<Review>> getReviewsByAccountID(@PathVariable Long accountId) {
        if (accountService.getAccountById(accountId) == null) {return ResponseEntity.status(400).body(null);}
        return ResponseEntity.ok(reviewService.getAllReviewsByAccountId(accountService.getAccountById(accountId)));
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<Review> editReview(@PathVariable Long reviewId, @RequestBody Review rev) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //get current auth info
        Account currentAccount = accountService.findByUsername(authentication.getName()); //get current account based on auth info
        if (reviewService.getReviewById(reviewId) == null){return ResponseEntity.status(400).body(null);}
        if (reviewService.getReviewById(reviewId).getAccountId() != currentAccount){ //check if the review to be edited belongs to the current account
            return ResponseEntity.status(403).body(null);
        }
        return ResponseEntity.ok(reviewService.editReview(reviewId, rev));
    }
    //Tamson ~Review Edit~

}
