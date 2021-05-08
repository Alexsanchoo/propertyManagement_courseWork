package com.sanchoo.property.management.controller;

import com.sanchoo.property.management.entity.photo.Photo;
import com.sanchoo.property.management.service.photo.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.util.Optional;

@Controller
@RequestMapping("/photo")
public class PhotoController {

	@Value("${upload.path}")
	private String uploadPath;

	@Autowired
	private PhotoService photoService;

	@PostMapping("/delete")
	public String deletePropertyPhoto(@RequestParam int id,
									  @RequestParam String url,
									  Model model) {
		Optional<Photo> photoOptional = this.photoService.findById(id);

		photoOptional.ifPresent(photo -> {
			this.photoService.delete(id);
			File filePhoto = new File(uploadPath + "/property/" + photo.getName());
			filePhoto.delete();
		});

		return "redirect:" + url;
	}
}
