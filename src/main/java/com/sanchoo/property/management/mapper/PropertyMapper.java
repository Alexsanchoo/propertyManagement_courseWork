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
public abstract class PropertyMapper {

	@Autowired
	private ServiceTypeRepository serviceTypeRepository;

	@Autowired
	private PropertyTypeRepository propertyTypeRepository;

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "serviceType", source = "serviceTypeId")
	@Mapping(target = "propertyType", source = "propertyTypeId")
	@Mapping(target = "price", source = "price")
	@Mapping(target = "status", ignore = true)
	public abstract Property propertyDtoToProperty(PropertyDto propertyDto);

	ServiceType mapServiceType(int serviceTypeId) {
		Optional<ServiceType> serviceTypeOptional = this.serviceTypeRepository.findById(serviceTypeId);
		return serviceTypeOptional.get();
	}

	PropertyType mapPropertyType(int propertyTypeId) {
		Optional<PropertyType> propertyTypeOptional = this.propertyTypeRepository.findById(propertyTypeId);
		return propertyTypeOptional.get();
	}

	int mapPrice(double price) {
		return (int) (price * 100.0);
	}
}
