package com.sanchoo.property.management.entity.property;

import com.sanchoo.property.management.entity.photo.Photo;
import com.sanchoo.property.management.entity.user.User;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "property")
public class Property {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "property_id")
	private int id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToOne
	@JoinColumn(name = "service_type_id")
	private ServiceType serviceType;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "property_id")
	private List<Photo> photos;

	@OneToOne
	@JoinColumn(name = "property_type_id")
	private PropertyType propertyType;

	@Column(name = "city")
	private String city;

	@Column(name = "district")
	private String district;

	@Column(name = "street")
	private String street;

	@Column(name = "house_number")
	private String houseNumber;

	@Column(name = "floor")
	private int floor;

	@Column(name = "floors_number")
	private int floorsNumber;

	@Column(name = "room_number")
	private int roomNumber;

	@Column(name = "area")
	private double area;

	@Column(name = "price")
	private int price;

	@Column(name = "description")
	private String description;

	@Column(name = "status")
	private PropertyStatus status;
}
