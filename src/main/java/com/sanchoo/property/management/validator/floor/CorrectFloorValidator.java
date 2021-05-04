package com.sanchoo.property.management.validator.floor;

import com.sanchoo.property.management.dto.property.PropertyDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CorrectFloorValidator implements ConstraintValidator<CorrectFloor, PropertyDto> {
	@Override
	public boolean isValid(PropertyDto propertyDto, ConstraintValidatorContext constraintValidatorContext) {
		return propertyDto.getFloor() <= propertyDto.getFloorsNumber();
	}
}
