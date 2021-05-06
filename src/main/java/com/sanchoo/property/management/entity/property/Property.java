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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
	@NotBlank(message = "*Пожалуйста, введите название города")
	@Pattern(regexp = "^[A-Za-zА-Яа-я\\ \\-\\.]+$", message = "*Пожалуйста, используйте только A-Z, А-Я, пробел, \"-\" и \".\"")
	private String city;

	@Column(name = "district")
	@NotBlank(message = "*Пожалуйста, введите название района")
	@Pattern(regexp = "^[A-Za-zА-Яа-я\\ \\-\\.]+$", message = "*Пожалуйста, используйте только A-Z, А-Я, пробел, \"-\" и \".\"")
	private String district;

	@Column(name = "street")
	@NotBlank(message = "*Пожалуйста, введите название улицы")
	@Pattern(regexp = "^[A-Za-zА-Яа-я\\ \\-\\.]+$", message = "*Пожалуйста, используйте только A-Z, А-Я, пробел, \"-\" и \".\"")
	private String street;

	@Column(name = "house_number")
	@NotBlank(message = "*Пожалуйста, введите номер дома")
	@Pattern(regexp = "^[0-9A-Za-zА-Яа-я\\-]+$", message = "*Пожалуйста, используйте только 0-9, A-Z, А-Я и \"-\" для номера дома")
	private String houseNumber;

	@Column(name = "floor")
	@Min(value = 0, message = "*Номер этажа не может быть отрицательным")
	private int floor;

	@Column(name = "floors_number")
	@Min(value = 0, message = "*Количество этажей не может быть отрицательным")
	private int floorsNumber;

	@Column(name = "room_number")
	@Min(value = 0, message = "*Количество комнат не может быть отрицательным")
	private int roomNumber;

	@Column(name = "area")
	@Min(value = 1, message = "*Метраж не может быть отрицательным или равен 0")
	private double area;

	@Column(name = "price")
	@Min(value = 0, message = "*Стоимость не может быть отрицательной")
	private int price;

	@Column(name = "description")
	@Size(min = 5, max = 100, message = "*Описание должно содержать от 5 до 100 символов включительно")
	private String description;

	@Column(name = "status")
	private PropertyStatus status;
}
