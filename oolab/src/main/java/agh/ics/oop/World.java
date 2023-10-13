package agh.ics.oop;

public class World {
    public static void main(String[] args) {
        System.out.println("system wystartował");
        run(args);
        System.out.println("system zakończył działanie");
    }

    private static void run(String[] args) {
        System.out.println("zwierzak idzie do przodu");
        String arguments = String.join(", ", args);
        System.out.println(arguments);
    }

}
