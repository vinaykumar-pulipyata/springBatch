package com.springBatch.pokemon.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import static com.springBatch.pokemon.constants.PokemonDBColumnConstants.*;
import com.springBatch.pokemon.entity.Pokemon;

public class PokemonRowMapper implements RowMapper<Pokemon> {

	@Override
	public Pokemon mapRow(ResultSet rs, int rowNum) throws SQLException {
		Pokemon pokemon = new Pokemon();
		pokemon.setId(rs.getInt(POKEMON_ID));
		pokemon.setName(rs.getString(NAME));
		pokemon.setType1(rs.getString(TYPE_1));
		pokemon.setHp(rs.getInt(HP));
		pokemon.setGeneration(rs.getInt(GENERATION));
		return pokemon;
	}

}
