package net.likelion.bebc25.spring.di.setter;

public class AfterTest {
    void main(){
//        Car car = new GasolineCar();
        Car car = new HybridCar();

        Driver driver = new Driver(); // 수정: 생성자 대신 기본 생성자 사용 (setter 주입 방식)
        driver.setCar(car);           // 수정: setter로 car 주입
        driver.driveCar();
    }
}
