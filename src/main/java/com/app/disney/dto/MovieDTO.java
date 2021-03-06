package com.app.disney.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.ManyToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.app.disney.models.Characters;
import com.app.disney.models.Genre;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class MovieDTO {
	private Long id;
	private String image;
	private String title;
	private String creationDate;
	@Min(value=1,message="el puntaje como minimo debe ser de 1 punto")
	@Max(value=5,message="el puntaje como maximo debe ser de 5 puntos")
	private int score;
	private boolean enable;
	private String genre;
	private List<Genre>genres;
	private List<Characters>characters;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public List<Genre> getGenres() {
		return genres;
	}
	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}
	public List<Characters> getCharacters() {
		return characters;
	}
	public void setCharacters(List<Characters> characters) {
		this.characters = characters;
	}
	
	
	
	
}
