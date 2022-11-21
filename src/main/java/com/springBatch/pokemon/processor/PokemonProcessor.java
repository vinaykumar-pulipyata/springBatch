package com.springBatch.pokemon.processor;

import org.springframework.batch.item.ItemProcessor;

import com.springBatch.pokemon.entity.Pokemon;

public class PokemonProcessor implements ItemProcessor<Pokemon, Pokemon> {

	@Override
	public Pokemon process(Pokemon pokemon) throws Exception {
	return pokemon;	
	}

}
