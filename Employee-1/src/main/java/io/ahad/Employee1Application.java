package io.ahad;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import io.ahad.Repository.Emp_Direct_Repo;

@SpringBootApplication
//@EnableJpaRepositories(basePackageClasses=Emp_Direct_Repo.class)
public class Employee1Application {

	public static void main(String[] args) {
		SpringApplication.run(Employee1Application.class, args);
	}
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}


}
