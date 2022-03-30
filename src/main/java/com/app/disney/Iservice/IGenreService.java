package com.app.disney.Iservice;

import java.util.Optional;

import com.app.disney.models.Genre;

public interface IGenreService {
public Iterable<Genre> findAll();
	
	public Optional<Genre> findById(Long id);
	
	public Genre save(Genre genre);
	
	public Genre update(Genre genre,Long id);
	
	public void deleteById(Long id);
}
