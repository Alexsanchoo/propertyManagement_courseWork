package com.sanchoo.property.management.repository.impl;

import com.sanchoo.property.management.dto.property.PropertyDto;
import com.sanchoo.property.management.entity.property.Property;
import com.sanchoo.property.management.entity.property.PropertyStatus;
import com.sanchoo.property.management.entity.property.PropertyType;
import com.sanchoo.property.management.entity.property.ServiceType;
import com.sanchoo.property.management.repository.PropertySearchRepository;
import com.sanchoo.property.management.service.property.PropertyTypeService;
import com.sanchoo.property.management.service.property.ServiceTypeService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PropertySearchRepositoryImpl implements PropertySearchRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ServiceTypeService serviceTypeService;

	@Autowired
	private PropertyTypeService propertyTypeService;

	@Override
	public List<Property> findSalesAdsByPropertyDto(PropertyDto propertyDto, int serviceTypeId) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Property> query = cb.createQuery(Property.class);
		Root<Property> root = query.from(Property.class);

		List<Predicate> predicates = new ArrayList<>();

		Optional<ServiceType> serviceTypeOptional = this.serviceTypeService.findById(serviceTypeId);
		serviceTypeOptional.ifPresent(serviceType -> predicates.add(cb.equal(root.get("serviceType"), serviceType)));

		predicates.add(cb.equal(root.get("status"), PropertyStatus.APPROVED));

		Optional<PropertyType> propertyTypeOptional = this.propertyTypeService.findById(propertyDto.getPropertyTypeId());
		propertyTypeOptional.ifPresent(propertyType -> {
			if (propertyDto.getPropertyTypeId() != 1) {
				predicates.add(cb.equal(root.get("propertyType"), propertyType));
			}
		});


		if (propertyDto.getCity() != null && !propertyDto.getCity().isEmpty()) {
			predicates.add(cb.like(root.get("city"), "%" + propertyDto.getCity() + "%"));
		}

		if (propertyDto.getDistrict() != null && !propertyDto.getDistrict().isEmpty()) {
			predicates.add(cb.like(root.get("district"), "%" + propertyDto.getDistrict() + "%"));
		}

		if (propertyDto.getRoomNumber() != 0) {
			predicates.add(cb.equal(root.get("roomNumber"), propertyDto.getRoomNumber()));
		}

		int priceFrom = (int) propertyDto.getPriceFrom() * 100;
		int priceTo = (int) propertyDto.getPriceTo() * 100;

		if (priceFrom != 0 || priceTo != 0) {
			predicates.add(cb.between(root.get("price"), priceFrom, priceTo));
		}

		if (propertyDto.getFloor() != 0) {
			predicates.add(cb.equal(root.get("floor"), propertyDto.getFloor()));
		}

		if (propertyDto.getFloorsNumber() != 0) {
			predicates.add(cb.equal(root.get("floorsNumber"), propertyDto.getFloorsNumber()));
		}

		if (propertyDto.getStreet() != null && !propertyDto.getStreet().isEmpty()) {
			predicates.add(cb.like(root.get("street"), "%" + propertyDto.getStreet() + "%"));
		}

		if(propertyDto.getAreaFrom() != 0.0 || propertyDto.getAreaTo() != 0.0) {
			predicates.add(cb.between(root.get("area"), propertyDto.getAreaFrom(), propertyDto.getAreaTo()));
		}

		query.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
	//сделать отдельную страницу для поиска и проверить
		return entityManager.createQuery(query).getResultList();
	}
}
