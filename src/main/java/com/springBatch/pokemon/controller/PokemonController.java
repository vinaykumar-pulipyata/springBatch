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

	@Autowired
	@Qualifier("Water-Pokemon")
	private Job jobWater;

	@Autowired
	@Qualifier("Legendary-Pokemon")
	private Job jobLegendary;

	@Autowired
	@Qualifier("Fetch-All-Pokemon")
	private Job jobFetchPokemon;
	
	@Autowired
	@Qualifier("Fetch-All-Fire-Pokemon")
	private Job jobFetchFirePokemon;
	
	@GetMapping("welcome")
	public String welcome() {
		return "Pokemon - Gotta catch em all!!";
	}

	@PostMapping("loadAll")
	public String loadAllPokemon() {
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

	@PostMapping("loadAllFirePokemon")
	public String loadAllFirePokemon() {
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

	@PostMapping("loadAllWaterPokemon")
	public String loadAllWaterPokemon() {
		JobParameters jobParameter = new JobParametersBuilder().addLong("startAt", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobWater, jobParameter);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			e.printStackTrace();
			return "Error in Job";
		}
		return "Added Water Pokemons to DB";
	}

	@PostMapping("loadAllLegendaryPokemon")
	public String loadAllLegendaryPokemon() {
		JobParameters jobParameter = new JobParametersBuilder().addLong("startAt", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobLegendary, jobParameter);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			e.printStackTrace();
			return "Error in Job";
		}
		return "Added Legendary Pokemons to DB";
	}
	
	@PostMapping("fetchAll")
	public String fetchAllPokemon() {
		JobParameters jobParameter = new JobParametersBuilder().addLong("startAt", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobFetchPokemon, jobParameter);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			e.printStackTrace();
			return "Error in Job";
		}
		return "Fetched Pokemons from DB";
	}
	
	@PostMapping("fetchAllFirePokemon")
	public String fetchAllFirePokemon() {
		JobParameters jobParameter = new JobParametersBuilder().addLong("startAt", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobFetchFirePokemon, jobParameter);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			e.printStackTrace();
			return "Error in Job";
		}
		return "Fetched Pokemons from DB";
	}
}
