package com.app.disney.Iservice;

import java.util.List;
import java.util.Optional;

import com.app.disney.models.Movie;

public interface IMovieService {
	public List<Movie> listAll();
	
	public Optional<Movie> findById(Long id);
	
	public Movie save(Movie movie);
	
	public Movie update(Movie movie,Long id);
	
	public void deleteById(Long id);

}
