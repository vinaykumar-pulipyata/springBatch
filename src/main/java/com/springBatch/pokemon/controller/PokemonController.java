package com.springBatch.pokemon.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("pokemon")
public class PokemonController {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	@Qualifier("All-Pokemon")
	private Job job;
	
	@Autowired
	@Qualifier("Fire-Pokemon")
	private Job jobFire;

	@GetMapping("welcome")
	public String welcome() {
		return "Pokemon - Gotta catch em all!!";
	}

	@PostMapping("fetchAll")
	public String fetchAllPokemon() {
		JobParameters jobParameter = new JobParametersBuilder().addLong("startAt", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(job, jobParameter);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			e.printStackTrace();
			return "Error in Job";
		}
		return "Added all pokemons to DB";
	}
	
	@PostMapping("fetchAllFirePokemon")
	public String fetchAllFirePokemon() {
		JobParameters jobParameter = new JobParametersBuilder().addLong("startAt", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobFire, jobParameter);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			e.printStackTrace();
			return "Error in Job";
		}
		return "Added Fire Pokemons to DB";
	}
}
