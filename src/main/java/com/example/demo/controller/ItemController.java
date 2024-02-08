package com.example.demo.controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;

import javax.sql.rowset.serial.SerialException;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.exception.PhotoRetrievalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Item;
import com.example.demo.responce.ItemResponce;
import com.example.demo.services.ItemServices;

@RestController
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "*", allowedHeaders="*")
public class ItemController {
	@Autowired
	private ItemServices itemServices;

	public ItemController(ItemServices itemServices) {
		super();
		this.itemServices = itemServices;
	}

	@PostMapping("/addItem")
	public ResponseEntity<Item> addItem(@RequestParam("photo") MultipartFile photo,
			@RequestParam("category") String category, @RequestParam("name") String name,
			@RequestParam("status") String status) throws  IOException, SQLException {
		try {
			Item item = itemServices.addItem(photo, name, category, status);
			return ResponseEntity.ok().body(item);
		} catch (Exception e) {

		}
		//ItemResponce itemResponce = new ItemResponce()
		return ResponseEntity.status(HttpStatus.CREATED).build();

	}

	@GetMapping("/items")
	@ResponseBody
//	@Produces(MediaType.APPLICATION_JSON)
	
	public ResponseEntity<List<ItemResponce>> getItems()
			throws SQLException, ResourceNotFoundException, PhotoRetrievalException {
		List<Item> item = itemServices.getAllItems();
		List<ItemResponce> itemResponces = new ArrayList<>();
		for (Item item1 : item) {
			byte[] PhotoBytes = itemServices.getItemPhotoById(item1.getId());
			if (PhotoBytes != null && PhotoBytes.length > 0) {
				String base64Photo = Base64.encodeBase64String(PhotoBytes);

				ItemResponce itemResponce = getItemResponse(item1);
				itemResponces.add(itemResponce);
				itemResponce.setPhoto(base64Photo);

			}
		}
		
		return ResponseEntity.ok(itemResponces);

	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Void > deleteItem(@PathVariable Long id){
		itemServices.delteItem(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
  
	
	
	
	private ItemResponce getItemResponse(Item item) throws PhotoRetrievalException {
		byte[] photoBytes = null;
		Blob photoBlob = item.getPhoto();

		if (photoBlob != null) {
			try {
				photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
			} catch (SQLException e) {
				// TODO: handle exception
				throw new PhotoRetrievalException("error");
			}
		}

		return new ItemResponce(item.getId(), item.getCategory(), item.getName(), item.getStatus(), photoBytes);
	}

}
