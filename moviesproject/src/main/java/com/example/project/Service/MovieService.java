package com.example.project.Service;

import org.springframework.stereotype.Service;
import com.example.project.Repository.MovieRepository;
import com.example.project.Entity.Movie;
import java.util.List;

@Service
public class MovieService {
    
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository){
        
        this.movieRepository = movieRepository;

    }

    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }

    public Movie getMovieByName(String name){
        return movieRepository.findByName(name);
    }

    public Movie addMovie(Movie movie){
        return movieRepository.save(movie);
    }

    public Movie getMovieById(long movieId){
        return movieRepository.findByMovieId(movieId);
    }

    public List<Movie> getMoviesByDirector(String director){
        return movieRepository.findByDirector(director);
    }

    public List<Movie> getMoviesByReleaseYear(int releaseYear){
        return movieRepository.findByReleaseYear(releaseYear);
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }


}
