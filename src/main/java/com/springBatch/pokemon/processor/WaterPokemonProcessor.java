package com.springBatch.pokemon.processor;

import static com.springBatch.pokemon.constants.PokemonTypeConstant.*;

import org.springframework.batch.item.ItemProcessor;

import com.springBatch.pokemon.entity.Pokemon;

public class WaterPokemonProcessor implements ItemProcessor<Pokemon, Pokemon> {

	@Override
	public Pokemon process(Pokemon pokemon) throws Exception {
		if (WATER.equals(pokemon.getType1())) {
			return pokemon;
		}
		return null;
	}
}