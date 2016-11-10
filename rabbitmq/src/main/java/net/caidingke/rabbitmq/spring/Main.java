package net.caidingke.rabbitmq.spring;

/**
 * @author bowen
 * @create 2016-11-06 19:30
 */

public class Main {

    private static int j = 20;

    public static void main(String[] args) {
        number(1, 1);
    }


    public static void number(int n, int y) {

        if (j == 0) {
            return;
        }
        /**
         * n = 1; y = 1; print 2;
         * n = 1; y = 2; print 3;
         * n = 2; y = 3; print 5;
         * n = 3; y = 5; print 8;
         */
        System.out.println(n + y);
        j--;
        number(y, n + y);


    }


}
