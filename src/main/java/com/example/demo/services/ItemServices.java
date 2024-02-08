package com.example.demo.services;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Item;
import com.example.demo.repo.ItemRepo;

@Service
public class ItemServices {
	@Autowired
	private ItemRepo itemRepo;
	
	
	public ItemServices(ItemRepo itemRepo) {
		super();
		this.itemRepo = itemRepo;
	}

	public  Item addItem(MultipartFile file,String name,String category,String Status) throws IOException, SerialException, SQLException {
		Item item = new Item();
		item.setName(name);
		item.setCategory(category);
		item.setStatus(Status);
		if(!file.isEmpty()) {
			byte[] bytes = file.getBytes();
			Blob photoBlob = new SerialBlob(bytes);
			item.setPhoto(photoBlob);
		}
		
		return itemRepo.save(item);
	}
	
	public List<Item > getAllItems(){
		return itemRepo.findAll();
	}

	
	public Item updateItem(Long id,String name,String category,String Status,byte[] photoBytes) throws SerialException, SQLException {
		Item item = itemRepo.findById(id).get();
		if(name != null) item.setName(name);
		if(category != null) item.setCategory(category);
		if(Status != null) item.setStatus(Status);
		if(photoBytes != null && photoBytes.length > 0){
			 item.setPhoto( new SerialBlob(photoBytes));
			 
			
		}
		return itemRepo.save(item);
		
	}
	
	public void delteItem(Long id) {
		Optional<Item> findById = itemRepo.findById(id);
		if(findById.isPresent()) {
			itemRepo.deleteById(id);
		}
	}

	public byte[] getItemPhotoById(Long id) throws SQLException, ResourceNotFoundException {
		Optional<Item> findById = itemRepo.findById(id);
		if(findById.isEmpty()) {
			throw new ResourceNotFoundException("Soor");
		}
		
		Blob photoBlob =findById.get().getPhoto();
		if(photoBlob != null) {
			return photoBlob.getBytes(1, (int) photoBlob.length());
		}
		return null;

		
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
