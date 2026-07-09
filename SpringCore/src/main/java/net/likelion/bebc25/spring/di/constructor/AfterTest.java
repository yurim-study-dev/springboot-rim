package net.likelion.bebc25.spring.di.constructor;

public class AfterTest {
    void main(){
//        Car car = new GasolineCar();
        Car car = new HybridCar();

        Driver driver = new Driver(car);
        driver.driveCar();
    }
}
