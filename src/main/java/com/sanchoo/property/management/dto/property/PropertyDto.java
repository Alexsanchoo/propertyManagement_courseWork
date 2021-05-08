package com.sanchoo.property.management.dto.property;

import com.sanchoo.property.management.entity.photo.Photo;
import com.sanchoo.property.management.entity.property.PropertyStatus;
import com.sanchoo.property.management.entity.user.User;
import com.sanchoo.property.management.validator.floor.CorrectFloor;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@CorrectFloor
public class PropertyDto {
	private int id;
	private int serviceTypeId;
	private int propertyTypeId;

	private List<Photo> photos;

	private User user;

	@NotBlank(message = "*Пожалуйста, введите название города")
	@Pattern(regexp = "^[A-Za-zА-Яа-я\\ \\-\\.]+$", message = "*Пожалуйста, используйте только A-Z, А-Я, пробел, \"-\" и \".\"")
	private String city;

	@NotBlank(message = "*Пожалуйста, введите название района")
	@Pattern(regexp = "^[A-Za-zА-Яа-я\\ \\-\\.]+$", message = "*Пожалуйста, используйте только A-Z, А-Я, пробел, \"-\" и \".\"")
	private String district;

	@NotBlank(message = "*Пожалуйста, введите название улицы")
	@Pattern(regexp = "^[A-Za-zА-Яа-я\\ \\-\\.]+$", message = "*Пожалуйста, используйте только A-Z, А-Я, пробел, \"-\" и \".\"")
	private String street;

	@NotBlank(message = "*Пожалуйста, введите номер дома")
	@Pattern(regexp = "^[0-9A-Za-zА-Яа-я\\-]+$", message = "*Пожалуйста, используйте только 0-9, A-Z, А-Я и \"-\" для номера дома")
	private String houseNumber;

	@Min(value = 0, message = "*Номер этажа не может быть отрицательным")
	private int floor;

	@Min(value = 0, message = "*Количество этажей не может быть отрицательным")
	private int floorsNumber;

	@Min(value = 0, message = "*Количество комнат не может быть отрицательным")
	private int roomNumber;

	@Min(value = 1, message = "*Метраж не может быть отрицательным или равен 0")
	private double area;

	@Min(value = 0, message = "*Стоимость не может быть отрицательной")
	private double price;

	@Size(min = 5, max = 100, message = "*Описание должно содержать от 5 до 100 символов включительно")
	private String description;

	private PropertyStatus status;
}
