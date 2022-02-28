package emp.portal;

import emp.portal.repository.UserRepository;
import emp.portal.repository.NewAppRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication//(scanBasePackages = {"emp.portal.signin_signup.repository.UserRepository","emp.portal.repository.NewAppRepository"})
@OpenAPIDefinition(info=@Info(title="EmpPortalApplication",version="1.0",description="With SignIn,SignUp Service"))
@EnableJpaRepositories(basePackageClasses=UserRepository.class)
public class EmpPortalNewApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmpPortalNewApplication.class, args);

	}
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}


}
