package pl.jg.iam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import pl.jg.iam.common.IdAppGenerator;

import javax.annotation.PostConstruct;
import java.util.TimeZone;


@SpringBootApplication
@EntityScan(basePackageClasses = {
		FchcIamAppApplication.class,
		Jsr310JpaConverters.class
})
//@EnableEurekaClient
public class FchcIamAppApplication {

	public static final String APPLICATION_ID = IdAppGenerator.uuid();

	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

	}

	public static void main(String[] args) {
		SpringApplication.run(FchcIamAppApplication.class, args);
	}


}
