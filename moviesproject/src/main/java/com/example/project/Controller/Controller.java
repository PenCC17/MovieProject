package com.example.project.Controller;

import com.example.project.Entity.Account;
import com.example.project.Entity.Movie;
import com.example.project.Entity.Review;
import com.example.project.Entity.Role;

import com.example.project.Service.AccountService;
import com.example.project.Service.MovieService;
import com.example.project.Service.ReviewService;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
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
       
       // Set default role if not provided
       if (account.getRole() == null) {
           account.setRole(Role.USER);
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

    @GetMapping("/account/{accountId}")
    public ResponseEntity<String> getAccountByAccountId(@PathVariable Long accountId){ 
        if (accountService.getAccountById(accountId) == null){
            return ResponseEntity.status(400).body(null); //No returns as of right now return via status code
        }
        return ResponseEntity.ok(accountService.getAccountById(accountId).getUsername());
    }

    @GetMapping("/movie/{name}")
    public ResponseEntity<Movie> getMovieByName(@PathVariable String name){ 
        if (movieService.getMovieByName(name) == null){
            return ResponseEntity.status(400).body(null); //No returns as of right now return via status code
        }
        return ResponseEntity.ok(movieService.getMovieByName(name));
    }
    @PostMapping("/movie/add")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<Movie> addMovie(
            @RequestParam String name,
            @RequestParam String director,
            @RequestParam String releaseYear,
            @RequestParam(required = false) MultipartFile posterImage) {
        
        try {
            // Check if movie already exists
            for (Movie m : movieService.getAllMovies()) {
                if (m.getName().equalsIgnoreCase(name)) {
                    return ResponseEntity.status(400).body(null);
                }
            }

            String posterImageName = null;
            
            // Handle file upload if provided
            if (posterImage != null && !posterImage.isEmpty()) {
                // Get file extension
                String originalFilename = posterImage.getOriginalFilename();
                String fileExtension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                
                // Create filename based on movie name
                posterImageName = name.replaceAll("[^a-zA-Z0-9]", "_") + fileExtension;
                
                // Define the path to save the file
                String uploadDir = "src/main/resources/static/posterImages/";
                Path uploadPath = Paths.get(uploadDir);
                
                // Create directory if it doesn't exist
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                
                // Save the file
                Path filePath = uploadPath.resolve(posterImageName);
                Files.copy(posterImage.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                
                // Also copy to frontend public folder for development
                String frontendPath = "../../../movie-project-frontend/public/posterImages/";
                Path frontendUploadPath = Paths.get(frontendPath);
                if (!Files.exists(frontendUploadPath)) {
                    Files.createDirectories(frontendUploadPath);
                }
                Path frontendFilePath = frontendUploadPath.resolve(posterImageName);
                Files.copy(posterImage.getInputStream(), frontendFilePath, StandardCopyOption.REPLACE_EXISTING);
            }

            // Create and save movie
            Movie movie = new Movie(name, director, Long.parseLong(releaseYear), posterImageName);
            return ResponseEntity.status(201).body(movieService.addMovie(movie));
            
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @DeleteMapping("/movie/delete/{id}")
    public void deleteMovie(@PathVariable Long id){
        movieService.deleteMovie(id);
    }

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
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<List<Review>> getReviewsByMovieID(@PathVariable Long movieId) {
        if( movieService.getMovieById(movieId) == null) 
        {
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.ok(reviewService.getAllReviewsByMovieId(movieService.getMovieById(movieId)));
    }

    @GetMapping("/reviews/account/{accountId}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<List<Review>> getReviewsByAccountID(@PathVariable String accountId) {
        if (accountService.findByUsername(accountId) == null) {return ResponseEntity.status(400).body(null);}
        return ResponseEntity.ok(reviewService.getAllReviewsByAccountId(accountService.findByUsername(accountId)));
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<?> editReview(@PathVariable Long reviewId, @RequestBody Review rev) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //get current auth info
        Account currentAccount = accountService.findByUsername(authentication.getName()); //get current account based on auth info
        if (reviewService.getReviewById(reviewId) == null){return ResponseEntity.status(400).body(null);}
        if (reviewService.getReviewById(reviewId).getAccountId() != (currentAccount)){ //check if the review to be edited belongs to the current account
            return ResponseEntity.status(403).body("Not Found");
        }
        return ResponseEntity.ok(reviewService.editReview(reviewId, rev));
    }

    @DeleteMapping("reviews/delete/{id}")
    public void deleteReview(@PathVariable Long id){
        reviewService.deleteReview(id);
    }

    //Tamson ~Review Edit~

}

