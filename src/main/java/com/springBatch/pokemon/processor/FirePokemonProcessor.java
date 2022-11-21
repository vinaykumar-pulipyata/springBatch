package com.springBatch.pokemon.processor;

import org.springframework.batch.item.ItemProcessor;

import com.springBatch.pokemon.constants.PokemonTypeConstant;
import com.springBatch.pokemon.entity.Pokemon;

public class FirePokemonProcessor implements ItemProcessor<Pokemon, Pokemon> {

	@Override
	public Pokemon process(Pokemon pokemon) throws Exception {
		if (PokemonTypeConstant.FIRE.equals(pokemon.getType1())) {
			return pokemon;
		}
		return null;
	}
}
