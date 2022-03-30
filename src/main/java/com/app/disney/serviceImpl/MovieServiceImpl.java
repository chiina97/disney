package com.app.disney.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.disney.Iservice.IMovieService;
import com.app.disney.dto.MovieDTO;
import com.app.disney.models.Genre;
import com.app.disney.models.Movie;
import com.app.disney.repository.GenreRepository;
import com.app.disney.repository.MovieRepository;

@Service
public class MovieServiceImpl implements IMovieService {

	@Autowired
	private MovieRepository movieRepo;
	@Autowired
	private GenreRepository genreRepo;

	@Override
	@Transactional(readOnly = true)
	public Iterable<Movie> findAll() {

		return movieRepo.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Movie> findById(Long id) {

		return movieRepo.findById(id);
	}

	@Override
	public Movie save(Movie movie) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
		String dateString = sdf.format(new Date());
		movie.setCreationDate(dateString);

		return movieRepo.save(movie);
	}

	@Override
	public Movie update(Movie movieRequest, Long id) {
		Optional<Movie> movie = movieRepo.findById(id);
		if (movie.isPresent()) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
			String dateString = sdf.format(new Date());
			movie.get().setCreationDate(dateString);
			movie.get().setImage(movieRequest.getImage());
			movie.get().setScore(movieRequest.getScore());
			movie.get().setTitle(movieRequest.getTitle());
			movie.get().setEnable(movieRequest.isEnable());
			return movieRepo.save(movie.get());
		} else
			return null;
	}

	@Override
	public void deleteById(Long id) {
		movieRepo.deleteById(id);

	}

	@Transactional(readOnly = true)
	public Iterable<Movie> findAllOrderByAsc() {
		return movieRepo.findAllOrderByAsc();
	}

	@Transactional(readOnly = true)
	public Iterable<Movie> findAllOrderByDesc() {
		return movieRepo.findAllOrderByDesc();
	}

	@Transactional(readOnly = true)
	public List<Movie> findAllByTitle(String title) {
		return movieRepo.findAllByTitleAndEnable(title,true);
	}

	public Optional<Movie> findByTitle(String title) {
		return movieRepo.findByTitleAndEnable(title, true);
	}


	public void setGenres(MovieDTO movieRequest, Movie movieReq) {
		for (Genre genre : movieRequest.getGenres()) {
			Optional<Genre> genreResp = this.genreRepo.findByName(genre.getName());
			if (!genreResp.isEmpty()) {
				genreResp.get().getMovies().add(movieReq);
				this.genreRepo.save(genreResp.get());
			} else {
				Genre newGenre = new Genre(genre.getName(), genre.getImage(), true);
				newGenre.getMovies().add(movieReq);
				this.genreRepo.save(newGenre);
			}
		}
	}

}
