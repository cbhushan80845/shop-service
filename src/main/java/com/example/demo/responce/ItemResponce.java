package com.example.demo.responce;

import java.sql.Blob;

import org.apache.tomcat.util.codec.binary.Base64;




public class ItemResponce {
	private Long id;
	private String name;
	private String category;
	private String status;
	private String photo;
	



	public ItemResponce(Long id, String name, String category, String status, byte[] photoBytes) {
		super();
		this.id = id;
		this.name = name;
		this.category = category;
		this.status = status;
		this.photo = photoBytes != null ? Base64.encodeBase64String(photoBytes):null;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getPhoto() {
		return photo;
	}


	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	 

	
	

}
