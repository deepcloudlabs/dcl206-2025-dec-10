package com.example.exercises;

import static java.util.Comparator.comparingDouble;
import static java.util.Comparator.comparingLong;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

import com.example.dao.CountryDao;
import com.example.dao.InMemoryWorldDao;
import com.example.domain.City;
import com.example.domain.Country;

/**
 * 
 * @author Binnur Kurt <binnur.kurt@gmail.com>
 *
 */
public class Exercise2 {
	private static final CountryDao countryDao = InMemoryWorldDao.getInstance();

	public static void main(String[] args) {
		// Find the most populated city of each continent
		countryDao.findAllCountries()
		          .stream()
		          .max(comparingLong(Country::getPopulation))
		          .ifPresent(System.out::println);
		countryDao.findAllCountries()
				.stream()
				//.peek(System.err::println)
				.filter(country -> Double.isFinite(country.getSurfaceArea()))
				//.peek(System.err::println)
				.max(comparingDouble(Country::getSurfaceArea))
				.ifPresent(System.out::println);
		countryDao.findAllCountries()
		          .stream()
		          .map(Country::getCities)
		          .flatMap(Collection::stream)
		          .max(comparingLong(City::getPopulation))
		          .ifPresent(System.out::println);	
/*
		Comparator<CountryCityPair> cityPopulation = (pair1,pair2) -> pair1.city().getPopulation()-pair2.city().getPopulation(); 
		countryDao.findAllCountries()
			      .stream()
			      .map(country -> country.getCities().stream().map(city -> new CountryCityPair(country,city)).toList())
			      .flatMap(List::stream)
			      .collect(Collectors.groupingBy(CountryCityPair::country,Collectors.maxBy(cityPopulation)))
			      .forEach((country,pair)->System.out.println("%24s: %-24s".formatted(country.getName(),pair.orElseThrow().city().getName())));
*/	
		countryDao.findAllCountries()
		.stream()
		.map(ctry -> new CountryCityPair(ctry,ctry.getCities().stream().max(Comparator.comparingLong(City::getPopulation))))
		.filter(pair -> pair.city().isPresent())
		.toList()		
		.forEach( System.out::println);
		
	}

}

record CountryCityPair(Country country,Optional<City> city) {}