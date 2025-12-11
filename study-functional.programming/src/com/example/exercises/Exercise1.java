package com.example.exercises;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import com.example.domain.Movie;
import com.example.service.InMemoryMovieService;
import com.example.service.MovieService;

/**
 * 
 * @author Binnur Kurt <binnur.kurt@gmail.com>
 *
 */
public class Exercise1 {
	private static final MovieService movieService = InMemoryMovieService.getInstance();

	public static void main(String[] args) {
		System.err.println("Exercise1 is started!");      
        final Collection<Movie> movies = movieService.findAllMovies();
        Predicate<Movie> movieBefore1970 = movie  -> {
        	System.err.println("movieBefore1970 is called!");
        	return movie.getYear() < 1970;
        };
        movies.parallelStream()
	          .filter(movieBefore1970)
	          .map(movie -> {
	        	  System.err.println("movie to directors...");	        			  	        	  	        	  
	        	  return movie.getDirectors();
	          })
	          .flatMap(directors -> {
	        	  System.err.println("Flattening...");	        			  	        	  
	        	  return directors.stream();
	          })
	          .distinct()
	          .sorted(
	        		  (dir1,dir2) -> { 
	        			 System.err.println("comparing two directors: %s vs %s".formatted(dir1.getName(),dir2.getName()));	        			  
	        			 return dir1.getName().compareTo(dir2.getName());
	        		  }
    		  ).forEach(director -> System.err.println("https://www.imdb.com/name/%s".formatted(director.getImdb())));
        System.err.println("Exercise1 is completed!");      
	}

}
