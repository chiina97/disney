package com.app.disney.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.app.disney.models.Movie;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class CharacterDTO {
	@NotBlank
	private String name;
	@NotNull
	private int age;
	@NotNull
	private double weight ;
	@NotNull
	private String history;
	@NotNull
	private String image;
	private boolean enable=true;


	private List<Movie> movies;
	
	public CharacterDTO(String name, int age, double weight, String history, boolean enable, String image) {
		this.name = name;
		this.age = age;
		this.weight = weight;
		this.history = history;
		this.enable = enable;
		this.movies=new ArrayList<>();
		this.image = image;
	}
	
	public CharacterDTO() {
		this.movies=new ArrayList<>();
	};

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}


	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public List<Movie> getMovies() {
		return movies;
	}

	public void setMovies(List<Movie> movies) {
		this.movies = movies;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	
	
	
}
