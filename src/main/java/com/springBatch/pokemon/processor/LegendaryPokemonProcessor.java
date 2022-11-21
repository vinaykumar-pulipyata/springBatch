package com.springBatch.pokemon.processor;

import org.springframework.batch.item.ItemProcessor;

import com.springBatch.pokemon.entity.Pokemon;

public class LegendaryPokemonProcessor implements ItemProcessor<Pokemon, Pokemon> {

	@Override
	public Pokemon process(Pokemon pokemon) throws Exception {
		if ("TRUE".equals(pokemon.getLegendary())) {
			return pokemon;
		}
		return null;
	}
}

