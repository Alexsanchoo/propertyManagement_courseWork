package com.sanchoo.property.management.service.photo.impl;

import com.sanchoo.property.management.entity.photo.Photo;
import com.sanchoo.property.management.repository.PhotoRepository;
import com.sanchoo.property.management.service.photo.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PhotoServiceImpl implements PhotoService {

	@Autowired
	private PhotoRepository photoRepository;

	@Override
	public Optional<Photo> findById(int id) {
		return photoRepository.findById(id);
	}

	@Override
	public void delete(int id) {
		this.photoRepository.deleteById(id);
	}

	@Override
		public Photo save(Photo photo) {
		return this.photoRepository.save(photo);
	}

	@Override
	public List<Photo> saveAll(List<Photo> photos) {
		return this.photoRepository.saveAll(photos);
	}
}
