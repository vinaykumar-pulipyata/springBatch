package com.springBatch.pokemon.processor;

import org.springframework.batch.item.ItemProcessor;

import static com.springBatch.pokemon.constants.PokemonTypeConstants.*;
import com.springBatch.pokemon.entity.Pokemon;

public class FirePokemonProcessor implements ItemProcessor<Pokemon, Pokemon> {

	@Override
	public Pokemon process(Pokemon pokemon) throws Exception {
		if (FIRE.equals(pokemon.getType1())) {
			return pokemon;
		}
		return null;
	}
}
