package switus.user.back.studywithus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class StudyWithUsApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudyWithUsApplication.class, args);
	}

}
