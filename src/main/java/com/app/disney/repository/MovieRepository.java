package com.app.disney.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.disney.models.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
	
	public List<Movie> findAllByTitleAndEnable(String title,boolean enable);

	@Query(value = "SELECT m FROM Movie m WHERE m.enable=true ORDER BY m.creationDate ASC")
	public List<Movie> findAllOrderByAsc();

	@Query(value = "SELECT m FROM Movie m WHERE m.enable=true ORDER BY m.creationDate DESC")
	public List<Movie> findAllOrderByDesc();
	
	public Optional<Movie>findByTitleAndEnable(String title,boolean enable);
}
