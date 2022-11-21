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

import static com.springBatch.pokemon.constants.PokemonColumnConstants.*;
import com.springBatch.pokemon.entity.Pokemon;
import com.springBatch.pokemon.processor.FirePokemonProcessor;
import com.springBatch.pokemon.processor.LegendaryPokemonProcessor;
import com.springBatch.pokemon.processor.PokemonProcessor;
import com.springBatch.pokemon.processor.WaterPokemonProcessor;
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
		lineTokenizerForPokemon.setNames(ID, NAME, TYPE_1, TYPE_2, TOTAL, HP, ATTACK, DEFENSE, SPECIAL_ATTACK,
				SPECIAL_DEFENSE, SPEED, GENERATION, LEGENDARY);

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
	public WaterPokemonProcessor waterPokemonProcessor() {
		return new WaterPokemonProcessor();
	}
	
	@Bean
	public LegendaryPokemonProcessor legendaryPokemonProcessor() {
		return new LegendaryPokemonProcessor();
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
		return stepBuilderFactory.get("pokemon-All").<Pokemon, Pokemon>chunk(10)
				.reader(pokemonReader())
				.processor(pokemonProcessor())
				.writer(pokemonWriter())
				.build();
	}

	@Bean
	public Step stepToFetchFirePokemon() {
		return stepBuilderFactory.get("pokemon-Fire").<Pokemon, Pokemon>chunk(10)
				.reader(pokemonReader())
				.processor(firePokemonProcessor())
				.writer(pokemonWriter())
				.build();
	}
	
	@Bean
	public Step stepToFetchWaterPokemon() {
		return stepBuilderFactory.get("pokemon-Water").<Pokemon, Pokemon>chunk(10)
				.reader(pokemonReader())
				.processor(waterPokemonProcessor())
				.writer(pokemonWriter())
				.build();
	}
	
	@Bean
	public Step stepToFetchLegendaryPokemon() {
		return stepBuilderFactory.get("pokemon-Legendary").<Pokemon, Pokemon>chunk(10)
				.reader(pokemonReader())
				.processor(legendaryPokemonProcessor())
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
	@Qualifier("Fire-Pokemon")
	public Job jobToFetchFirePokemon() {
		return jobBuilderFactory.get("fetchAllFirePokemon")
				.flow(stepToFetchFirePokemon())
				.end()
				.build();
	}
	
	@Bean
	@Qualifier("Water-Pokemon")
	public Job jobToFetchWaterPokemon() {
		return jobBuilderFactory.get("fetchAllWaterPokemon")
				.flow(stepToFetchWaterPokemon())
				.end()
				.build();
	}
	
	@Bean
	@Qualifier("Legendary-Pokemon")
	public Job jobToFetchLegendaryPokemon() {
		return jobBuilderFactory.get("fetchAllLegendaryPokemon")
				.flow(stepToFetchLegendaryPokemon())
				.end()
				.build();
	}
	
}
