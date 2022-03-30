package com.app.disney.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.disney.dto.Message;
import com.app.disney.dto.MovieDTO;
import com.app.disney.models.Genre;
import com.app.disney.models.Movie;
import com.app.disney.serviceImpl.GenreServiceImpl;
import com.app.disney.serviceImpl.MovieServiceImpl;

@RestController
@RequestMapping(value = "/movie")
public class MovieController {
	@Autowired
	private MovieServiceImpl movieService;

	@Autowired
	private GenreServiceImpl genreService;

	@Autowired
	private ModelMapper modelMapper;
	

	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody MovieDTO movieDTO, BindingResult result) {
		// validaciones:
		if (result.hasErrors()) {
			return new ResponseEntity<Message>(new Message(result.getFieldError().getDefaultMessage()),
					HttpStatus.BAD_REQUEST);
		}

		// convert DTO to entity

		Movie movieRequest = modelMapper.map(movieDTO, Movie.class);
		movieRequest.setEnable(true);
		
		movieService.save(movieRequest);
		this.movieService.setGenres(movieDTO,movieRequest);
		
		MovieDTO movieResp = modelMapper.map(movieRequest, MovieDTO.class);

		return new ResponseEntity<MovieDTO>(movieResp, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@Valid@RequestBody MovieDTO movieDTO,BindingResult result,
			@PathVariable(value = "id") Long movieId) {
		
		// validaciones:
				if (result.hasErrors()) {
					return new ResponseEntity<Message>(new Message(result.getFieldError().getDefaultMessage()),
									HttpStatus.BAD_REQUEST);
						}
				
		// convert DTO to Entity
		Movie movieRequest = modelMapper.map(movieDTO, Movie.class);

		movieService.update(movieRequest, movieId);
		
		//convert entity to dto
		MovieDTO movieResponse = modelMapper.map(movieRequest, MovieDTO.class);

		return new ResponseEntity<>(movieResponse,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long movieId) {
		// convert DTO to Entity
		Optional<Movie> movie = movieService.findById(movieId);
		if(movie.isPresent()) {
			movieService.deleteById(movieId);
			return new ResponseEntity<Message>(new Message("La Pelicula/Serie "+movie.get().getTitle() +" fue eliminada!"), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<Message>(new Message("La Pelicula/Serie no existe"), HttpStatus.BAD_REQUEST);
		}
		
		
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<?> getAllMoviesByName(@PathVariable(value = "name") String name) {
		return ResponseEntity.ok(movieService.findAllByTitle(name));
	}
	
	@GetMapping("/genre/{genre}")
	public ResponseEntity<?> getAllMoviesById(@PathVariable(value = "genre") Long genreId) {
	  Optional<Genre>genreRequest=genreService.findById(genreId); 
	  return ResponseEntity.ok(genreRequest.get().getMovies());
	}
	
	@GetMapping("/order/{order}")
	public ResponseEntity<?> getAllMoviesByOrder(@PathVariable(value = "order") String order) {
		
		if(order.equals("ASC")) {
			return ResponseEntity.ok(movieService.findAllOrderByAsc());
		}
		else {
			if(order.equals("DESC")) {
			   return ResponseEntity.ok(movieService.findAllOrderByDesc());
			}
			else {
				return new ResponseEntity<Message>(new Message("ERROR"), HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	@GetMapping
	public ResponseEntity<?> getAllMovies() {
		
		return ResponseEntity.ok(movieService.findAll());
		}
		

}
