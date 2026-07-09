package net.likelion.bebc25.spring.di.setter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 스프링 컨테이너에 알려주는 앱 설정 클래스
@Configuration
public class AppConfig {

    @Bean
    public Car car() {
//        return new GasolineCar();
        return new HybridCar();
    }

    @Bean
    public Driver driver(Car car) {
        Driver driver = new Driver();
        driver.setCar(car); // Setter Injection
        return driver;
    }
}
