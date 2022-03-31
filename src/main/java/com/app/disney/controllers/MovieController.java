package com.app.disney.controllers;

import java.util.Arrays;
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

import com.app.disney.dto.CharacterDTO;
import com.app.disney.dto.Message;
import com.app.disney.dto.MovieDTO;
import com.app.disney.dto.MovieFilterDTO;
import com.app.disney.models.Characters;
import com.app.disney.models.Genre;
import com.app.disney.models.Movie;
import com.app.disney.serviceImpl.CharacterServiceImpl;
import com.app.disney.serviceImpl.GenreServiceImpl;
import com.app.disney.serviceImpl.MovieServiceImpl;

@RestController
@RequestMapping(value = "/movies")
public class MovieController {
	@Autowired
	private MovieServiceImpl movieService;

	@Autowired
	private GenreServiceImpl genreService;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CharacterServiceImpl characterService;

	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody MovieDTO movieDTO, BindingResult result) {
		try {
			// validaciones:
			if (result.hasErrors()) {
				return new ResponseEntity<Message>(new Message(result.getFieldError().getDefaultMessage()),
						HttpStatus.BAD_REQUEST);
			}
			// convert DTO to entity
			if(this.movieService.findByTitle(movieDTO.getTitle()).isEmpty()) {
				Movie movieRequest = modelMapper.map(movieDTO, Movie.class);
				movieRequest.setEnable(true);
				movieService.save(movieRequest);
				movieService.saveCharacters(movieDTO);
				this.movieService.setGenres(movieDTO, movieRequest);
				// convert entity to DTO
				return new ResponseEntity<MovieDTO>(movieDTO, HttpStatus.CREATED);
			}else {
				return new ResponseEntity<Message>(new Message("La pelicula ingresada ya se encuentra registrada"),HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<Message>(new Message("Se produjo un error"), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody MovieDTO movieDTO, BindingResult result,
			@PathVariable(value = "id") Long movieId) {
		try {
			
			// validaciones:
			if (result.hasErrors()) {
				return new ResponseEntity<Message>(new Message(result.getFieldError().getDefaultMessage()),
						HttpStatus.BAD_REQUEST);
			}
			// convert DTO to Entity
			Movie movieRequest = modelMapper.map(movieDTO, Movie.class);
			movieService.update(movieRequest, movieId);
			List<Characters> listCharacters = this.characterService.findAllByMovieId(movieId);
			List<Genre> listGenre = this.genreService.findAllByMoviesIdAndEnable(movieId);
			// convert entity to dto
			MovieDTO movieResponse = modelMapper.map(movieRequest, MovieDTO.class);
			movieResponse.setCharacters(listCharacters);
			movieResponse.setGenres(listGenre);
			return new ResponseEntity<>(movieResponse, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<Message>(new Message("Se produjo un error"), HttpStatus.BAD_REQUEST);
		}

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long movieId) {
		// convert DTO to Entity
		Optional<Movie> movie = movieService.findById(movieId);
		if (movie.isPresent()) {
			movieService.deleteById(movieId);
			return new ResponseEntity<Message>(
					new Message("La Pelicula/Serie " + movie.get().getTitle() + " fue eliminada!"), HttpStatus.OK);
		} else {
			return new ResponseEntity<Message>(new Message("La Pelicula/Serie no existe"), HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("/name/{name}")
	public ResponseEntity<?> getAllMoviesByName(@PathVariable(value = "name") String name) {
		try {
			List<Movie> movies = movieService.findAllByTitle(name);
			if (movies.isEmpty())
				return new ResponseEntity<Message>(new Message("No se encontraron peliculas con el nombre ingresado"),
						HttpStatus.BAD_REQUEST);
			return new ResponseEntity<List<Movie>>(movies, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Message>(new Message("Se produjo un error"), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/genre/{genre}")
	public ResponseEntity<?> getAllMoviesById(@PathVariable(value = "genre") Long genreId) {
		try {
		Optional<Genre> genreRequest = genreService.findById(genreId);
		List<Movie>listMovies=genreRequest.get().getMovies();
		List<MovieFilterDTO> listReturn = Arrays.asList(modelMapper.map(listMovies,MovieFilterDTO[].class)); 
		if (genreRequest.isEmpty())
			return new ResponseEntity<Message>(new Message("No se encontraron peliculas con ese id de genero"),
					HttpStatus.BAD_REQUEST);
		return ResponseEntity.ok(listReturn);
	
		}
		catch (Exception e) {
			return new ResponseEntity<Message>(new Message("Se produjo un error"), HttpStatus.BAD_REQUEST);
		}
	}
	@GetMapping("/order/{order}")
	public ResponseEntity<?> getAllMoviesByOrder(@PathVariable(value = "order") String order) {
		try {
			String orderUppercase=order.toUpperCase();
			if (orderUppercase.equals("ASC")) {
				List<Movie>listMovies=movieService.findAllOrderByAsc();
				List<MovieFilterDTO> listReturn = Arrays.asList(modelMapper.map(listMovies,MovieFilterDTO[].class));
				return ResponseEntity.ok(listReturn);
			} else {
				if (orderUppercase.equals("DESC")) {
					List<Movie>listMovies=movieService.findAllOrderByDesc();
					List<MovieFilterDTO> listReturn = Arrays.asList(modelMapper.map(listMovies,MovieFilterDTO[].class));
					return ResponseEntity.ok(listReturn);
				}
			}
			return new ResponseEntity<Message>(new Message("El orden ingresado es incorrecto"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<Message>(new Message("Se produjo un error"), HttpStatus.BAD_REQUEST);
		}
		

	}
	
	@GetMapping("/details/{id}")
	public ResponseEntity<MovieDTO> getById(@PathVariable(value = "id") Long characterId) {
		Optional<Movie> movie = movieService.findById(characterId);
		// convert entity to DTO
		MovieDTO movieResponse = modelMapper.map(movie.get(), MovieDTO.class);
		if (movie.isPresent()) {
			return ResponseEntity.ok(movieResponse);
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	@GetMapping
	 public ResponseEntity<?> getAll(){
		 try {
			List<Movie> listMovies=  this.movieService.listAll();
			List<MovieFilterDTO> listReturn = Arrays.asList(modelMapper.map(listMovies,MovieFilterDTO[].class)); 
			 return new ResponseEntity<List<MovieFilterDTO>> (listReturn,HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Message> (new Message("Ocurrio un error al obtener el listado"),HttpStatus.BAD_REQUEST);
		}
		 
	 }
}
