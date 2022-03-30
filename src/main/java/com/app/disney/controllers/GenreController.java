package com.app.disney.controllers;


import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.disney.dto.GenreDTO;
import com.app.disney.dto.Message;
import com.app.disney.models.Genre;
import com.app.disney.serviceImpl.GenreServiceImpl;
import com.app.disney.serviceImpl.MovieServiceImpl;


@RestController
@RequestMapping(value = "/genre")
public class GenreController {
	
	@Autowired
	private MovieServiceImpl movieService;
	
	@Autowired
	private GenreServiceImpl genreService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PostMapping
	public ResponseEntity<?> create(@Valid@RequestBody GenreDTO genreDTO,BindingResult result) {
		// validaciones:
		if (result.hasErrors()) {
			return new ResponseEntity<Message>(new Message(result.getFieldError().getDefaultMessage()),
							HttpStatus.BAD_REQUEST);
				}
		
		if (genreService.existsByName(genreDTO.getName())) {
			return new ResponseEntity<Message>(
					new Message("El genero con ese nombre ya existe"),HttpStatus.BAD_REQUEST);
		}
		
		// convert DTO to entity
		
		Genre genreRequest = modelMapper.map(genreDTO, Genre.class);
		
		
        
		genreRequest.setEnable(true);
        
        Genre genre = genreService.save(genreRequest);

		// convert entity to DTO
        GenreDTO genreResponse = modelMapper.map(genre, GenreDTO.class);

		return new ResponseEntity<GenreDTO>(genreResponse, HttpStatus.CREATED);
	}
}