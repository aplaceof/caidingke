package net.caidingke.nio.fakeAsync;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * socket 时间服务器
 *
 * @author bowen
 * @create 2017-03-20 11:37
 */

public class TimeService {

    public static void main(String[] args) throws IOException {

        int port = 8081;

        if (args != null && args.length > 0) {

            port = Integer.valueOf(args[0]);
        }

        ServerSocket server = null;

        try {
            server = new ServerSocket(port);
            System.out.println("the time server is start in port : " + port);
            Socket socket = null;
            //ExecutorService singleExecutor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
            //        50, 120L, TimeUnit.SECONDS,
            //        new ArrayBlockingQueue<Runnable>(10000));

            ExecutorService singleExecutor = Executors.newFixedThreadPool(50);
            while (true) {
                socket = server.accept();
                singleExecutor.execute(new TimeServerHandler(socket));
            }
        } finally {
            if (server != null) {
                System.out.println("the time server close.");
                server.close();
                server = null;
            }
        }
    }
}
