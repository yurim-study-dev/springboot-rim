package net.likelion.bebc25.oop.after;

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
