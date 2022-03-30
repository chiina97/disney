package com.app.disney.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.disney.models.Genre;
import com.app.disney.models.Movie;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
	
	public boolean existsByName(String name);

	public Optional<Genre> findByName(String name);
	
	

}
