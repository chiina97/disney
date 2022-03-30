package com.app.disney.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.disney.dto.CharacterFilterDTO;
import com.app.disney.models.Characters;

public interface CharacterRepository extends JpaRepository<Characters, Long> {
	List<Characters>findAllByNameAndEnable(String name,boolean enable);
	
	List<Characters>findAllByAgeAndEnable(int age,boolean enable);
	
	List<Characters>findAllByMoviesIdAndEnable(Long id,boolean enable);
	
	List<Characters> findAllByWeightAndEnable(double weight,boolean enable);
	
}
