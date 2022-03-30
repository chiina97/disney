package com.app.disney.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.disney.Iservice.ICharacterService;
import com.app.disney.dto.CharacterFilterDTO;
import com.app.disney.models.Characters;
import com.app.disney.repository.CharacterRepository;


@Service
public class CharacterServiceImpl implements ICharacterService {
	@Autowired
	CharacterRepository characterRepo;

	@Override
	public List<Characters> listAll() {
		return  this.characterRepo.findAll();
		

	}
	
	@Override
	@Transactional(readOnly = true)
	public Optional<Characters> findById(Long id) {
		return characterRepo.findById(id);
	}

	@Override
	public Characters save(Characters character) {
		return this.characterRepo.save(character);
	}

	@Override
	public Characters update(Characters characterRequest, Long id) {
		Optional<Characters> character = characterRepo.findById(id);
		if (character.isPresent()) {
			character.get().setAge(characterRequest.getAge());
			character.get().setImage(characterRequest.getImage());
			character.get().setName(characterRequest.getName());
			character.get().setHistory(characterRequest.getHistory());
			character.get().setEnable(characterRequest.isEnable());
			return characterRepo.save(character.get());
		} else
			return null;
	}

	public List<Characters> findAllByName(String name) {
		return this.characterRepo.findAllByNameAndEnable(name, true);
	}

	public List<Characters> findAllByIdMovie(Long id) {
		return this.characterRepo.findAllByMoviesIdAndEnable(id, true);
	}

	public List<Characters> findAllByWeight(double weight) {
		return this.characterRepo.findAllByWeightAndEnable(weight, true);
	}

	public List<Characters> findAllByAgeAndEnable(int age) {
		return this.characterRepo.findAllByAgeAndEnable(age, true);
	}

	@Override
	public void deleteById(Long id) {
		characterRepo.deleteById(id);

	}

}
