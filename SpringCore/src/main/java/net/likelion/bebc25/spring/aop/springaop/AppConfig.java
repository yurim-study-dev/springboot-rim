package net.likelion.bebc25.spring.aop.springaop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

// 스프링 컨테이너에 알려주는 앱 설정 클래스
@Configuration
@EnableAspectJAutoProxy // 스프링 컨테이너에 @Aspect 어노테이션이 붙은 빈들을 찾아서 프로시 처리를 하도록 지시
public class AppConfig {
    @Bean // 스프링 빈으로 등록 (메서드명인 car가 빈의 이름이 됨)
    public Car car(){
        return new GasolineCar();
//        return new HybridCar();
    }

    @Bean
    public Driver driver(Car car){
        return new Driver(car); // DI
    }

    @Bean
    public LoggingAspect loggingAspect(){
        return new LoggingAspect();
    }
}