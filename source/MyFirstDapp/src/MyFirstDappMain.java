
/*
 * This file is public domain.
 *
 * SWIRLDS MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF 
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED 
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SWIRLDS SHALL NOT BE LIABLE FOR 
 * ANY DAMAGES SUFFERED AS A RESULT OF USING, MODIFYING OR 
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import com.swirlds.platform.Browser;
import com.swirlds.platform.Platform;
import com.swirlds.platform.SwirldMain;
import com.swirlds.platform.SwirldState;


public class MyFirstDappMain implements SwirldMain, JsonHashGraphNode {
    private static final int PORT = 2027;
    private Platform platform;
    private int selfId;
    private final int sleepPeriod = 100;

    private String name;
    private Server server;

    public MyFirstDappMain() {
        System.out.println("MyFirstDappMain()");

        if (server == null) {
            System.out.println("server == null");
            this.server = new Server(PORT, this);
            this.server.start();
            // this.server.write();
        }
    }

    public static void main(String[] args) {
        Browser.main(null);
        System.out.println("main");
    }

    // ///////////////////////////////////////////////////////////////////

    @Override
    public void preEvent() {
        System.out.println("preEvent()");
    }

    @Override
    public void init(Platform platform, int id) {
        System.out.println("init()");
        this.platform = platform;
        this.selfId = id;

        platform.setAbout("My First Dapp!"); // set the browser's "about" box
        platform.setSleepAfterSync(sleepPeriod);
    }

    @Override
    public void run() {
        System.out.println("run()");

        this.name = platform.getState().getAddressBookCopy()
                .getAddress(selfId).getSelfName();

        String lastReceived = "";

        while (true) {
            MyFirstDappState state = (MyFirstDappState) platform
                    .getState();
            String received = state.getReceived();
            if (!lastReceived.equals(received)) {
                lastReceived = received;
                server.write(lastReceived);
            }
            try {
                Thread.sleep(sleepPeriod);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public SwirldState newState() {
        System.out.println("newState()");
        return new MyFirstDappState();
    }

    @Override
    public void addMessage(String json) {
        byte[] transaction = json.getBytes(StandardCharsets.UTF_8);
        platform.createTransaction(transaction, null);
    }

    @Override
    public String getMessages(int index) {
        MyFirstDappState state = ((MyFirstDappState) platform.getState());
        return state.getReceived();
    }
}