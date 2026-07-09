package net.likelion.bebc25.spring.di.setter;

public class Driver {
    private Car car;


    //setter
    public void setCar(Car car){
        System.out.println("Setter Injection 호출됨");
        this.car = car;
    }


    public void driveCar(){
        car.startEngine();
        car.drive();
        car.stopEngine();
    }
}
