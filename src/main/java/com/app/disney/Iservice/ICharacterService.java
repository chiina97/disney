package com.app.disney.Iservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.app.disney.dto.CharacterFilterDTO;
import com.app.disney.models.Characters;



public interface ICharacterService {
	public List<Characters> listAll();

	public Characters save(Characters character);

	public Characters update(Characters character, Long id);

	public void deleteById(Long id);
	
	public Optional<Characters> findById(Long id);

	
}
