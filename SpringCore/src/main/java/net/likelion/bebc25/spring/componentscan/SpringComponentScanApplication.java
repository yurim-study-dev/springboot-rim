package net.likelion.bebc25.spring.componentscan;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringComponentScanApplication {
    void main(){

        try{
            // 1. 스프링 컨테이너 생성(Bean 정보 분석을 위한 Config 객체 지정)
            ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

            // 2. driver 빈을 컨테이너에서 꺼냄
            Driver driver = context.getBean(Driver.class);

            System.out.println("Driver 객체: " + driver);

            // 3. 비즈니스 로직 실행
            driver.driveCar(100);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}