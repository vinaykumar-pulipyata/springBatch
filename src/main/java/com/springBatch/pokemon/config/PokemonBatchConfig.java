package com.springBatch.pokemon.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.springBatch.pokemon.entity.Pokemon;
import com.springBatch.pokemon.processor.FirePokemonProcessor;
import com.springBatch.pokemon.processor.PokemonProcessor;
import com.springBatch.pokemon.repository.PokemonRepository;

@Configuration
@EnableBatchProcessing
public class PokemonBatchConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private PokemonRepository pokemonRepository;

	@Bean
	public FlatFileItemReader<Pokemon> pokemonReader() {
		FlatFileItemReader<Pokemon> pokemonItemReader = new FlatFileItemReader<>();
		pokemonItemReader.setResource(new FileSystemResource("src/main/resources/pokemon.csv"));
		pokemonItemReader.setName("CustomerCSVReader");
		pokemonItemReader.setLinesToSkip(1);
		pokemonItemReader.setLineMapper(lineMapperForPokemon());
		return pokemonItemReader;
	}

	private LineMapper<Pokemon> lineMapperForPokemon() {
		DelimitedLineTokenizer lineTokenizerForPokemon = new DelimitedLineTokenizer();
		lineTokenizerForPokemon.setDelimiter(",");
		lineTokenizerForPokemon.setStrict(false);
		lineTokenizerForPokemon.setNames("Id", "Name", "Type 1", "Type 2", "Total", "HP", "Attack", "Defense",
				"Special Attack", "Special Defense", "Speed", "Generation", "Legendary");

		BeanWrapperFieldSetMapper<Pokemon> fieldSetMapperForPokemon = new BeanWrapperFieldSetMapper<>();
		fieldSetMapperForPokemon.setTargetType(Pokemon.class);

		DefaultLineMapper<Pokemon> lineMapperForPokemon = new DefaultLineMapper<>();
		lineMapperForPokemon.setFieldSetMapper(fieldSetMapperForPokemon);
		lineMapperForPokemon.setLineTokenizer(lineTokenizerForPokemon);
		return lineMapperForPokemon;
	}

	@Bean
	public PokemonProcessor pokemonProcessor() {
		return new PokemonProcessor();
	}

	@Bean
	public FirePokemonProcessor firePokemonProcessor() {
		return new FirePokemonProcessor();
	}
	
	@Bean
	public RepositoryItemWriter<Pokemon> pokemonWriter() {
		RepositoryItemWriter<Pokemon> pokemonItemWriter = new RepositoryItemWriter<>();
		pokemonItemWriter.setRepository(pokemonRepository);
		pokemonItemWriter.setMethodName("save");
		return pokemonItemWriter;
	}

	@Bean
	public Step stepToFetchPokemon() {
		return stepBuilderFactory.get("pokemon-step").<Pokemon, Pokemon>chunk(10)
				.reader(pokemonReader())
				.processor(pokemonProcessor())
				.writer(pokemonWriter())
				.build();
	}

	@Bean
	@Qualifier("All-Pokemon")
	public Job jobToFetchPokemon() {
		return jobBuilderFactory.get("fetchAll")
				.flow(stepToFetchPokemon())
				.end()
				.build();
	}
	
	@Bean
	public Step stepToFetchFirePokemon() {
		return stepBuilderFactory.get("pokemon-step").<Pokemon, Pokemon>chunk(10)
				.reader(pokemonReader())
				.processor(firePokemonProcessor())
				.writer(pokemonWriter())
				.build();
	}
	
	@Bean
	@Qualifier("Fire-Pokemon")
	public Job jobToFetchFirePokemon() {
		return jobBuilderFactory.get("fetchAllFirePokemon")
				.flow(stepToFetchFirePokemon())
				.end()
				.build();
	}
	
}
