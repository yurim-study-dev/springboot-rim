package net.likelion.bebc25.spring;

public class Driver {
    private Car car;

    // DI
    Driver(Car car){
        this.car = car;
    }

    public void driveCar(){
        car.startEngine();
        car.drive();
        car.stopEngine();
    }
}
