package com.example.random.service.business;

import java.security.DrbgParameters;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import com.example.random.service.Adapter;
import com.example.random.service.QualityLevel;
import com.example.random.service.RandomNumberService;
import com.example.random.service.ServiceQuality;

@ServiceQuality(QualityLevel.POSTQUANTUM_SECURE)
@Adapter(target = RandomNumberService.class, adaptee = SecureRandom.class)
public class PostQuantumRandomNumberService implements RandomNumberService {

	private SecureRandom random;

	public PostQuantumRandomNumberService() {
		DrbgParameters.Instantiation params = DrbgParameters.instantiation(256, DrbgParameters.Capability.RESEED_ONLY,
				null);
		try {
			random = SecureRandom.getInstance("DRBG", params);
		} catch (NoSuchAlgorithmException e) {
			random = new SecureRandom();
		}
	}

	@Override
	public int generate(int min, int max) {
		System.err.println("PostQuantumRandomNumberService::generate");		
		return random.nextInt(min, max);
	}

}
