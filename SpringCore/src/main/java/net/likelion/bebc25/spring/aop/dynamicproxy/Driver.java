package net.likelion.bebc25.spring.aop.dynamicproxy;

public class Driver {
    private Car car;

    // DI
    Driver(Car car){
        System.out.println("called Constructon Injection:" + car);
        this.car = car;
    }

    public void driveCar(){
        car.startEngine();
        car.drive();
        car.stopEngine();
    }
}
