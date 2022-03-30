package com.app.disney.dto;

public class CharacterFilterDTO {
	
	private String image;
	private String name;
	
	public CharacterFilterDTO() {
	}
	public CharacterFilterDTO(String image, String name) {
		this.image = image;
		this.name = name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
