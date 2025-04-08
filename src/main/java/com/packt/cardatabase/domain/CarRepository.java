package com.packt.cardatabase.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

// 쿼리 정의하기 
@RepositoryRestResource
// 다른 경로 이름을 이용하려면 @RepositoryRestResource(path="vehicles")를 사용할 수 있다.
public interface CarRepository extends CrudRepository<Car, Long> {
	//  브랜드로 자동차를 검색
	List<Car> findByBrand(@Param("brand")String brand);
	
	// 색상으로 자동차를 검색
	List<Car> findByColor(@Param("color")String color);
	
	// 연도로 자동차를 검색
	List<Car> findByYear(int year);
	
	// 브랜드와 모델로 자동차를 검색
	List<Car> findByBrandAndModel(String brand, String model);
	
	// 브랜드나 색상으로 자동차를 검색
	List<Car> findByBrandOrColor(String Brand, String color);
	
	// 브랜드로 자동차를 검색하고 연도로 정렬
	List<Car> findByBrandOrderByYearAsc(String Brand);
}
