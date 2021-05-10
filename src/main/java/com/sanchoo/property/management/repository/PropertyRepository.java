package com.sanchoo.property.management.repository;

import com.sanchoo.property.management.entity.property.Property;
import com.sanchoo.property.management.entity.property.PropertyStatus;
import com.sanchoo.property.management.entity.property.ServiceType;
import com.sanchoo.property.management.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Integer>, PropertySearchRepository {
	List<Property> findByStatusAndUser(PropertyStatus propertyStatus, User user);
	List<Property> findByStatus(PropertyStatus propertyStatus);
	List<Property> findByStatusAndServiceType(PropertyStatus propertyStatus, ServiceType serviceType);

	/*@Query("select p " +
			"from Property p " +
			"where p.propertyType.id = :propertyTypeId " +
			"and p.city = :city " +
			"and p.district = :district " +
			"and p.roomNumber = :roomNumber " +
			"and p.price between :priceFrom and :priceTo")
	List<Property> findByPropertyDto(@Param("propertyTypeId") int propertyTypeId,
									 @Param("city") String city,
									 @Param("district") String district,
									 @Param("roomNumber") int roomNumber,
									 @Param("priceFrom") int priceFrom,
									 @Param("priceTo") int priceTo);*/
}
