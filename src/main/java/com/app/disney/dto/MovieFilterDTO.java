package com.app.disney.dto;

public class MovieFilterDTO {
	private String image;
	private String title;
	private String creationDate;
	
	public MovieFilterDTO(String image, String title, String creationDate) {
		super();
		this.image = image;
		this.title = title;
		this.creationDate = creationDate;
	}
	
	public MovieFilterDTO() {}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	
	
	
	

}
