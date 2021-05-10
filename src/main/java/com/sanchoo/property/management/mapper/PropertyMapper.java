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

	@Mapping(target = "serviceType", source = "serviceTypeId")
	@Mapping(target = "propertyType", source = "propertyTypeId")
	public abstract Property propertyDtoToProperty(PropertyDto propertyDto);

	@Mapping(target = "serviceTypeId", source = "serviceType.id")
	@Mapping(target = "propertyTypeId", source = "propertyType.id")
	@Mapping(target = "priceFrom", ignore = true)
	@Mapping(target = "priceTo", ignore = true)
	@Mapping(target = "areaFrom", ignore = true)
	@Mapping(target = "areaTo", ignore = true)
	public abstract PropertyDto propertyToPropertyDto(Property property);

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

	double mapPrice(int price) {
		return price / 100.0;
	}
}
