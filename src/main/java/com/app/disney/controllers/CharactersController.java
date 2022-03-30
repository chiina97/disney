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

import com.app.disney.dto.CharacterDTO;
import com.app.disney.dto.CharacterReturnDTO;
import com.app.disney.dto.Message;
import com.app.disney.models.Characters;
import com.app.disney.models.Movie;
import com.app.disney.serviceImpl.CharacterServiceImpl;
import com.app.disney.serviceImpl.MovieServiceImpl;



@RestController
@RequestMapping(value = "/characters")
public class CharactersController {
	@Autowired
	CharacterServiceImpl characterService;
	@Autowired
	MovieServiceImpl movieService;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping
	public ResponseEntity<?> getAll() {
		try {
			return new ResponseEntity<List<CharacterReturnDTO>>(this.characterService.listAll(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Message>(new Message("Ocurrio un error al obtener el listado"),
					HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping
	public ResponseEntity<?> save(@Valid @RequestBody CharacterDTO characterDTO, BindingResult result) {
		if (result.hasErrors())
			return new ResponseEntity<Message>(new Message(result.getFieldError().getDefaultMessage()),
					HttpStatus.BAD_REQUEST);

		Characters characterRequest = modelMapper.map(characterDTO, Characters.class);
		characterRequest.getMovies().clear();

		for(Movie movie: characterDTO.getMovies()) {
			 Optional<Movie> movieResp = this.movieService.findByTitle(movie.getTitle());
			 if(!movieResp.isEmpty()) {
				 characterRequest.getMovies().add(movieResp.get());
			 }
		 }

		characterRequest.setEnable(true);
		this.characterService.save(characterRequest);

		CharacterDTO characterResponse = modelMapper.map(characterRequest, CharacterDTO.class);

		return new ResponseEntity<>(characterResponse, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody CharacterDTO characterDTO, BindingResult result,
			@PathVariable(value = "id") Long characterId) {
		try {
			Characters characterRequest = modelMapper.map(characterDTO, Characters.class);
			characterService.update(characterRequest, characterId);
			CharacterDTO characterResponse = modelMapper.map(characterRequest, CharacterDTO.class);
			return new ResponseEntity<>(characterResponse, HttpStatus.CREATED);

		} catch (Exception e) {
			return new ResponseEntity<Message>(new Message("Se produjo un error al actualizar"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/age/{age}")
	public ResponseEntity<?> getCharactersByAge(@PathVariable("age") int age) {
		try {
			List<Characters> request = this.characterService.findAllByAgeAndEnable(age);
			if (request.isEmpty())
				return new ResponseEntity<Message>(new Message("No se encontraron personajes con la edad ingresada"),
						HttpStatus.BAD_REQUEST);
			return new ResponseEntity<List<Characters>>(request, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Message>(new Message("Se produjo un error"), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<?> getCharactersByName(@PathVariable("name") String name) {
		try {
			List<Characters> request = this.characterService.findAllByName(name);
			if (request.isEmpty())
				return new ResponseEntity<Message>(new Message("No se encontraron personajes con el nombre ingresado"),
						HttpStatus.BAD_REQUEST);
			return new ResponseEntity<List<Characters>>(request, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Message>(new Message("Se produjo un error"), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/movie/{id}")
	public ResponseEntity<?> getCharactersByMovie(@PathVariable("id") Long id) {
		try {
			List<Characters> request = this.characterService.findAllByIdMovie(id);
			if (request.isEmpty())
				return new ResponseEntity<Message>(
						new Message("No se encontraron personajes para la pelicula ingresada"), HttpStatus.BAD_REQUEST);
			return new ResponseEntity<List<Characters>>(request, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Message>(new Message("Se produjo un error"), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/weight/{weight}")
	public ResponseEntity<?> getCharactersByWeight(@PathVariable("weight") double weight) {
		try {
			List<Characters> request = this.characterService.findAllByWeight(weight);
			if (request.isEmpty())
				return new ResponseEntity<Message>(new Message("No se encontraron personajes con el peso ingresado"),
						HttpStatus.BAD_REQUEST);
			return new ResponseEntity<List<Characters>>(request, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Message>(new Message("Se produjo un error"), HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long characterId) {
		// convert DTO to Entity
		Optional<Characters> character = characterService.findById(characterId);
		if(character.isPresent()) {
			characterService.deleteById(characterId);
			return new ResponseEntity<Message>(new Message("Personaje "+character.get().getName() +" eliminado!"), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<Message>(new Message("El personaje no existe"), HttpStatus.BAD_REQUEST);
		}
		
		
	}
}
