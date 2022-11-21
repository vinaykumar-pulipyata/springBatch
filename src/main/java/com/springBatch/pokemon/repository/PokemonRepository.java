package com.springBatch.pokemon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springBatch.pokemon.entity.Pokemon;

public interface PokemonRepository extends JpaRepository<Pokemon, Integer> {

}
