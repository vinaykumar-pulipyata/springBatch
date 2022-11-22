package com.springBatch.pokemon.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import static com.springBatch.pokemon.constants.PokemonCSVColumnConstants.*;

import javax.sql.DataSource;

import com.springBatch.pokemon.entity.Pokemon;
import com.springBatch.pokemon.processor.FirePokemonProcessor;
import com.springBatch.pokemon.processor.LegendaryPokemonProcessor;
import com.springBatch.pokemon.processor.PokemonProcessor;
import com.springBatch.pokemon.processor.WaterPokemonProcessor;
import com.springBatch.pokemon.repository.PokemonRepository;
import com.springBatch.pokemon.rowMapper.PokemonRowMapper;

@Configuration
@EnableBatchProcessing
public class PokemonBatchConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private PokemonRepository pokemonRepository;
	
	@Autowired
	private DataSource dataSource;

	@Bean
	public FlatFileItemReader<Pokemon> pokemonReader() {
		FileSystemResource resource = new FileSystemResource("src/main/resources/pokemon.csv");
		
		FlatFileItemReader<Pokemon> pokemonItemReader = new FlatFileItemReader<>();
		pokemonItemReader.setResource(resource);
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
	public RepositoryItemWriter<Pokemon> pokemonWriter() {
		RepositoryItemWriter<Pokemon> pokemonItemWriter = new RepositoryItemWriter<>();
		pokemonItemWriter.setRepository(pokemonRepository);
		pokemonItemWriter.setMethodName("save");
		return pokemonItemWriter;
	}

	
	@Bean
	public JdbcCursorItemReader<Pokemon> pokemonDBReader(){
		JdbcCursorItemReader<Pokemon> pokemonReader = new JdbcCursorItemReader<>();
		pokemonReader.setDataSource(dataSource);
		pokemonReader.setSql("SELECT * FROM pokemon;");
		pokemonReader.setRowMapper(new PokemonRowMapper());
		return pokemonReader;
	}

	@Bean
	public FlatFileItemWriter<Pokemon> pokemonCSVWrier(){
		FileSystemResource resource = new FileSystemResource("src/main/resources/pokemon_output.csv");
		
		BeanWrapperFieldExtractor<Pokemon> fieldExtractor = new BeanWrapperFieldExtractor<>();
		fieldExtractor.setNames(new String[] { "id", "name", "type1", "hp", "generation"}); //Attributes names in pokemon class
		
		DelimitedLineAggregator<Pokemon> lineAggregator = new DelimitedLineAggregator<>();
		lineAggregator.setFieldExtractor(fieldExtractor);
		
		FlatFileItemWriter<Pokemon> pokemonCSVFileItemWriter = new FlatFileItemWriter<>();
		pokemonCSVFileItemWriter.setResource(resource);
		pokemonCSVFileItemWriter.setLineAggregator(lineAggregator);
		return pokemonCSVFileItemWriter;
	}
	
	@Bean
	public FlatFileItemWriter<Pokemon> pokemonCSVFireWrier(){
		FileSystemResource resource = new FileSystemResource("src/main/resources/pokemonfire_output.csv");
		
		BeanWrapperFieldExtractor<Pokemon> fieldExtractor = new BeanWrapperFieldExtractor<>();
		fieldExtractor.setNames(new String[] { "id", "name", "type1", "hp", "generation"}); //Attributes names in pokemon class
		
		DelimitedLineAggregator<Pokemon> lineAggregator = new DelimitedLineAggregator<>();
		lineAggregator.setFieldExtractor(fieldExtractor);
		
		FlatFileItemWriter<Pokemon> pokemonCSVFileItemWriter = new FlatFileItemWriter<>();
		pokemonCSVFileItemWriter.setResource(resource);
		pokemonCSVFileItemWriter.setLineAggregator(lineAggregator);
		return pokemonCSVFileItemWriter;
	}
	
	//Processors
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
	
	//Steps to load data from csv to DB
	@Bean
	public Step stepToLoadPokemon() {
		return stepBuilderFactory.get("pokemon-All").<Pokemon, Pokemon>chunk(10)
				.reader(pokemonReader())
				.processor(pokemonProcessor())
				.writer(pokemonWriter())
				.build();
	}

	@Bean
	public Step stepToLoadFirePokemon() {
		return stepBuilderFactory.get("pokemon-Fire").<Pokemon, Pokemon>chunk(10)
				.reader(pokemonReader())
				.processor(firePokemonProcessor())
				.writer(pokemonWriter())
				.build();
	}
	
	@Bean
	public Step stepToLoadWaterPokemon() {
		return stepBuilderFactory.get("pokemon-Water").<Pokemon, Pokemon>chunk(10)
				.reader(pokemonReader())
				.processor(waterPokemonProcessor())
				.writer(pokemonWriter())
				.build();
	}
	
	@Bean
	public Step stepToLoadLegendaryPokemon() { 
		return stepBuilderFactory.get("pokemon-Legendary").<Pokemon, Pokemon>chunk(10)
				.reader(pokemonReader())
				.processor(legendaryPokemonProcessor())
				.writer(pokemonWriter())
				.build();
	}
	
	//Steps to extract data from DB to csv
	@Bean
	public Step stepToCSVPokemon() {
		return stepBuilderFactory.get("pokemon-All").<Pokemon, Pokemon>chunk(10)
				.reader(pokemonDBReader())
				.writer(pokemonCSVWrier())
				.build();
	}
	
	@Bean
	public Step stepToCSVFirePokemon() {
		return stepBuilderFactory.get("pokemon-All").<Pokemon, Pokemon>chunk(10)
				.reader(pokemonDBReader())
				.processor(firePokemonProcessor())
				.writer(pokemonCSVFireWrier())
				.build();
	}
	
	//Jobs to load data from csv to DB
	@Bean
	@Qualifier("All-Pokemon")
	public Job jobToLoadPokemon() {
		return jobBuilderFactory.get("loadAll")
				.flow(stepToLoadPokemon())
				.end()
				.build();
	}
	
	@Bean
	@Qualifier("Fire-Pokemon")
	public Job jobToLoadFirePokemon() {
		return jobBuilderFactory.get("loadAllFirePokemon")
				.flow(stepToLoadFirePokemon())
				.end()
				.build();
	}
	
	@Bean
	@Qualifier("Water-Pokemon")
	public Job jobToLoadWaterPokemon() {
		return jobBuilderFactory.get("loadAllWaterPokemon")
				.flow(stepToLoadWaterPokemon())
				.end()
				.build();
	}
	
	@Bean
	@Qualifier("Legendary-Pokemon")
	public Job jobToLoadLegendaryPokemon() {
		return jobBuilderFactory.get("loadAllLegendaryPokemon")
				.flow(stepToLoadLegendaryPokemon())
				.end()
				.build();
	}
	
	@Bean
	@Qualifier("Fetch-All-Pokemon")
	public Job jobToFetchPokemon() {
		return jobBuilderFactory.get("fetchAll")
				.incrementer(new RunIdIncrementer())
				.flow(stepToCSVPokemon())
				.end()
				.build();
	}
	
	@Bean
	@Qualifier("Fetch-All-Fire-Pokemon")
	public Job jobToFetchFirePokemon() {
		return jobBuilderFactory.get("fetchAllFirePokemon")
				.incrementer(new RunIdIncrementer())
				.flow(stepToCSVFirePokemon())
				.end()
				.build();
	}
}
