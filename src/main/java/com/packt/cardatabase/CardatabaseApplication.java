package com.packt.cardatabase;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.packt.cardatabase.domain.Car;
import com.packt.cardatabase.domain.CarRepository;
import com.packt.cardatabase.domain.Owner;
import com.packt.cardatabase.domain.OwnerRepository;
import com.packt.cardatabase.domain.User;
import com.packt.cardatabase.domain.UserRepository;

@SpringBootApplication
public class CardatabaseApplication implements CommandLineRunner{
	private static final Logger logger = LoggerFactory.getLogger(CardatabaseApplication.class);

	@Autowired
	private CarRepository repository;
	@Autowired
	private OwnerRepository orepository;
	@Autowired
	private UserRepository urepository;
	
	public static void main(String[] args) {
		SpringApplication.run(CardatabaseApplication.class, args);			
	}
	
	@Override
	public void run(String... args) throws Exception {
		// 소유자 객체를 추가하고 db에 저장
		Owner owner1 = new Owner("John", "Johnson");
		Owner owner2 = new Owner("Mary", "Robinson");
		orepository.saveAll(Arrays.asList(owner1, owner2));
		
		// 자동차 객체를 추가하고 소유자와 연결한 후 db에 저장
		Car car1 = new Car("Ford", "Mustang", "Red", "ADF-1121", 2021, 59000, owner1);
		Car car2 = new Car("Nissan", "Leaf", "White", "SSJ-3002", 2019, 29000, owner2);
		Car car3 = new Car("Toyota", "Prius", "Silver", "KKO-0212", 2020, 39000, owner2);
		repository.saveAll(Arrays.asList(car1, car2, car3));
		
		
		//모든 자동차를 가져와서 콘솔에 로깅
		for (Car car : repository.findAll()) {
			logger.info(car.getBrand() + " " + car.getModel());
		}
		
		// 사용자 이름 : user, 암호 : user
		urepository.save(new User("user", "$2y$04$X6lAZRTXyy/XI9E4YrEibe7YTgekNUMPr27VZIOwa/xJbVQC26H6G", "USER"));
		
		// 사용자 이름 : admin, 암호 : admin
		urepository.save(new User("admin", "$2y$04$2eOFyJUyuTfsZ.HG5kJnUOuoYwhNed9y2YG/Jk2VZj4JCotc/VsKO", "ADMIN"));
	}
		

}
