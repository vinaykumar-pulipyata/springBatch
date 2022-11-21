package com.springBatch.pokemon.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "POKEMON")
public class Pokemon {

	@Id
	@Column(name = "POKEMON_ID")
	private int id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "Type_1")
	private String type1;

	@Column(name = "Type_2")
	private String type2;

	@Column(name = "TOTAL")
	private int total;

	@Column(name = "HP")
	private int hp;

	@Column(name = "ATTACK")
	private int attack;

	@Column(name = "DEFENSE")
	private int defense;

	@Column(name = "SPECIAL_ATTACK")
	private int specialAttack;

	@Column(name = "SPECIAL_DEFENSE")
	private int specialDefense;

	@Column(name = "SPEED")
	private int speed;

	@Column(name = "GENERATION")
	private int generation;

	@Column(name = "LEGENDARY")
	private String legendary;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType1() {
		return type1;
	}

	public void setType1(String type1) {
		this.type1 = type1;
	}

	public String getType2() {
		return type2;
	}

	public void setType2(String type2) {
		this.type2 = type2;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

	public int getSpecialAttack() {
		return specialAttack;
	}

	public void setSpecialAttack(int specialAttack) {
		this.specialAttack = specialAttack;
	}

	public int getSpecialDefense() {
		return specialDefense;
	}

	public void setSpecialDefense(int specialDefense) {
		this.specialDefense = specialDefense;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getGeneration() {
		return generation;
	}

	public void setGeneration(int generation) {
		this.generation = generation;
	}

	public String getLegendary() {
		return legendary;
	}

	public void setLegendary(String legendary) {
		this.legendary = legendary;
	}

}
