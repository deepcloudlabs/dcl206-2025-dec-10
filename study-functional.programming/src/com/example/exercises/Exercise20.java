package com.example.exercises;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import com.example.domain.Director;
import com.example.domain.Movie;
import com.example.service.InMemoryMovieService;
import com.example.service.MovieService;

/**
 * 
 * @author Binnur Kurt <binnur.kurt@gmail.com>
 *
 */
public class Exercise20 {
	private static final MovieService movieService = InMemoryMovieService.getInstance();

	public static void main(String[] args) {
		// Find the number of movies of each director
        final Collection<Movie> movies = movieService.findAllMovies();
        // imperative programming: procedural programming, structured programming, oop
        // 1. Global Data
        Map<Director,Long> directorMovieCounts = new HashMap<>();
        // 2. external loop
        for (var movie : movies) { // iterator pattern
        	for (var director : movie.getDirectors()) { // external loop
        		var movieCount = directorMovieCounts.get(director);
        		if (Objects.isNull(movieCount)) {
        			movieCount = 0L;
        		}
        		movieCount++;
        		directorMovieCounts.put(director, movieCount);
        	}
        }
        for (var entry : directorMovieCounts.entrySet()) {
        	System.out.println("%24s: %d".formatted(entry.getKey().getName(),entry.getValue()));
        }
        // declarative programming: functional programming: I. HoF II. Pure Function -> a) Lambda Expression b) Method Reference
        // Development                                        -> Runtime
        // Chain of functions -> HoF (Higher-order Function)  -> Pipeline: lazy evaluation?
        // Java SE 8 -> Language @FunctionalInterface
        //              Collection API      --> Stream API --> Functional Programming
        //              Memory Organization --> Processing --> HoF: MapReduce Framework: filter, map, distinct, limit, mapToObj, min, max, toList,...
        //          ArrayList/TreeSet/HashMap  ------------>   Efficient/Parallel Pipeline
        //  Multi-core Ready/Scalable
        Function<Movie,List<Director>> toDirectors = Movie::getDirectors;
        // internal loop: iterator/spliterator
        // directorMovieCounts = 
        		              movies.stream()			   // Stream<Movie>
        		                    .parallel()            // runs in parallel 
        		                    .map(toDirectors)      // Stream<List<Director>>
        		                    .flatMap(List::stream) // Stream<Director>
        		                    .sequential()
        		                    .collect(groupingBy(identity(),counting()))
        		                    .forEach((director,count)->System.out.println("%24s: %d".formatted(director.getName(),count)));
	}

}
