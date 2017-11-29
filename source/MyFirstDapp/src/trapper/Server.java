package trapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    interface ServerContract {
        void onMessageReceived(String message);
    }

    private final int port;
    private boolean running;
    private final ExecutorService threadPool;
    private Thread serverThread;
    private Socket client;
    private ServerContract contract;

    public Server(int port, ServerContract serverContract) {
        this.port = port;
        this.contract = serverContract;
        threadPool = Executors.newFixedThreadPool(10, new ThreadFactory() {
            private final AtomicInteger instanceCount = new AtomicInteger();

            @Override
            public Thread newThread(Runnable r) {
                Thread t = Executors.defaultThreadFactory().newThread(r);
                t.setDaemon(true);
                t.setName("HANDLER_" + instanceCount.getAndIncrement());
                return t;
            }
        });
    }

    public void start() {
        running = true;
        System.out.println("port = " + port);
        serverThread = new Thread(() ->
        {
            try {
                ServerSocket server = new ServerSocket(port);
                while (running) {
                    client = server.accept();
                    System.out.println("accepting connection");
                    threadPool.submit(() ->
                    {
                        try {
                            try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
                                String line;
                                while ((line = in.readLine()) != null) {
                                    contract.onMessageReceived(line);
                                }
                            }
                            client.close();
                        } catch (IOException ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        serverThread.setName("LISTENER");
        serverThread.start();
    }

    public void sendMessage(String message) {
        if (this.client != null) {
            try {
                this.client.getOutputStream().write(message.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        running = false;
        if (serverThread != null) {
            serverThread.interrupt();
        }
        threadPool.shutdown();
        serverThread = null;

    }

}