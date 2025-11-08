package SE.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class MoaBomApplication {
    public static void main(String[] args) {
        SpringApplication.run(MoaBomApplication.class, args);
    }
}
