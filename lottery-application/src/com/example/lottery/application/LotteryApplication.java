package com.example.lottery.application;

/*
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.ServiceLoader;
*/

import com.example.lottery.service.business.StandardLotteryService;
// import com.example.random.service.QualityLevel;
//import com.example.random.service.RandomNumberService;
//import com.example.random.service.ServiceQuality;
import module com.example.random; // java 23
import module java.base;

public class LotteryApplication {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		var lotteryService = new StandardLotteryService();
		var services = ServiceLoader.load(RandomNumberService.class);
		var props = new Properties();
		props.load(new FileInputStream(new File("src","application.properties")));
		var qualityLevel = QualityLevel.valueOf(props.getProperty("service.quality"));
		for (var service : services) {
			var clazz = service.getClass();
			if (clazz.isAnnotationPresent(ServiceQuality.class)) {
				var serviceQuality = clazz.getAnnotation(ServiceQuality.class);
				if (serviceQuality.value() == qualityLevel)
					lotteryService.setRandomNumberService(service);
			}
		}
		lotteryService.draw(60, 6, 10)
		              .forEach(System.out::println);
	}

}
