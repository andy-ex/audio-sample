package sample;

/**
 * Created by Dmitry on 7/17/2014.
 */
public class Runner {

    public static void main(String[] args) {

        int a = 50;
        int b = 51;

        System.out.println(a + " (" + Integer.toBinaryString(a) + ")");
        System.out.println(b + " (" + Integer.toBinaryString(b) + ")");

        System.out.println("First result: " + ((a << 8) | b));
        System.out.println("Second result: " + (a << 8 | (255 & b)));

    }
}
