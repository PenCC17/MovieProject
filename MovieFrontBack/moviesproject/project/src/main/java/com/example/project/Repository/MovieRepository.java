package com.example.project.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.project.Entity.Movie;
import java.util.List;



@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Movie findByName(String name);
    Movie findByMovieId(long movieId);
    List<Movie> findByDirector(String director);
    List<Movie> findByReleaseYear(int releaseYear);
}
