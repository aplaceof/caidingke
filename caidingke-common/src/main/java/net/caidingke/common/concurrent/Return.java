package net.caidingke.common.concurrent;

/**
 * @author bowen
 * @create 2017-03-07 17:54
 */

public class Return {

    private Return() {

    }

    public static int get() {
        int x = 1;
        try {
           return ++x;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return ++x;
        }
        //return x;
    }

    public static void main(String[] args) {

        System.out.println(get());
    }
}
