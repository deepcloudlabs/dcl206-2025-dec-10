package com.example.exercises;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.dao.CountryDao;
import com.example.dao.InMemoryWorldDao;
import com.example.domain.City;

/**
 * 
 * @author Binnur Kurt <binnur.kurt@gmail.com>
 *
 */
public class Exercise21 {
	private static final CountryDao countryDao = InMemoryWorldDao.getInstance();

	public static void main(String[] args) {
		var countries = countryDao.findAllCountries();
		// Find the most populated city of each continent
		Comparator<ContinentCityPair> cityPopulation = (p1,p2) -> p1.city().getPopulation() - p2.city().getPopulation(); 
		 // Map<String,Optional<ContinentCityPair>>
		var stream = countries.stream();               // Stream<Country>;
		if (countries.size() > 100_000)
			stream = stream.parallel();
		var continentMostPopulatedCity = stream.map( country -> country.getCities().stream().map(city -> new ContinentCityPair(country.getContinent(),city)).toList())      // Stream<List<ContinentCityPair>>;
		          .flatMap(List::stream)  // Stream<ContinentCityPair>
		          .collect(Collectors.groupingBy(ContinentCityPair::continent,Collectors.maxBy(cityPopulation)));
		continentMostPopulatedCity.forEach((continent,pair)->System.out.println("%12s: %24s %d".formatted(continent,pair.orElseThrow().city().getName(),pair.orElseThrow().city().getPopulation())));          
			
	}

}

record ContinentCityPair(String continent,City city) {}