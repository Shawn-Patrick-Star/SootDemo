package BasicDemos.examples;
class Person {
    public void sayHello(){
        System.out.println("Hello person!");
    }

}

class Man extends Person{
    @Override
    public void sayHello() {
        System.out.println("Hello man!");
    }
}

class Women extends Person{
    @Override
    public void sayHello() {
        System.out.println("Hello women!");
    }
}

public class test {

    public static void main(String[] args) {
        Person man = new Man();
        Person women = new Women();

        man.sayHello();
        women.sayHello();
    }

}
