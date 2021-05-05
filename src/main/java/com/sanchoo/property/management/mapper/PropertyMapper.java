package com.sanchoo.property.management.mapper;

import com.sanchoo.property.management.dto.property.PropertyDto;
import com.sanchoo.property.management.entity.property.Property;
import com.sanchoo.property.management.entity.property.PropertyType;
import com.sanchoo.property.management.entity.property.ServiceType;
import com.sanchoo.property.management.repository.PropertyTypeRepository;
import com.sanchoo.property.management.repository.ServiceTypeRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(componentModel = "spring")
abstract public class PropertyMapper {

	@Autowired
	private ServiceTypeRepository serviceTypeRepository;

	@Autowired
	private PropertyTypeRepository propertyTypeRepository;

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "serviceType", source = ".")
	@Mapping(target = "propertyType", source = ".")
	@Mapping(target = "price", source = ".")
	@Mapping(target = "status", ignore = true)
	public abstract Property propertyDtoToProperty(PropertyDto propertyDto);

	ServiceType mapServiceType(PropertyDto propertyDto) {
		Optional<ServiceType> serviceTypeOptional = this.serviceTypeRepository.findById(propertyDto.getServiceTypeId());
		return serviceTypeOptional.get();
	}

	PropertyType mapPropertyType(PropertyDto propertyDto) {
		Optional<PropertyType> propertyTypeOptional = this.propertyTypeRepository.findById(propertyDto.getPropertyTypeId());
		return propertyTypeOptional.get();
	}

	int mapPrice(PropertyDto propertyDto) {
		double price = propertyDto.getPrice();
		return (int) (price * 100.0);
	}
}
