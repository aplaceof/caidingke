package net.caidingke.nio.nonBlockIo;

import java.nio.channels.MulticastChannel;

/**
 * non block io time server
 *
 * @author bowen
 * @create 2017-03-21 11:56
 */

public class TimeServer {

    public static void main(String[] args) {

        int port = 8080;

        if (args != null && args.length > 0) {
            port = Integer.valueOf(args[0]);
        }
        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
        new Thread(timeServer, "NON-MultiplexerTimeServer-001").start();
    }
}
