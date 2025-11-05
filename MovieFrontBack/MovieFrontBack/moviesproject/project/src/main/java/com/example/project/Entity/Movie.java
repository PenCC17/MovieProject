package com.example.project.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

//import javax.persistence.*;


@Entity
@Table(name="movie")
@Getter @Setter @NoArgsConstructor
public class Movie {

    @NotBlank(message = "Movie name can't be blank!")
    String name;

    @NotBlank(message = "Director name can't be blank!")
    String director;

    // @Min(value = 1888, message = "Earliest movie is 1888!")
    Long releaseYear;
    
    String posterImage; // Stores the filename of the poster image
    

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long movieId;

    public Movie(String name, String director, Long releaseYear) {
        this.name = name;
        this.director = director;
        this.releaseYear = releaseYear;
    }

    public Movie(String name, String director, Long releaseYear, String posterImage) {
        this.name = name;
        this.director = director;
        this.releaseYear = releaseYear;
        this.posterImage = posterImage;
    }

   
}
