package sample;

import java.io.*;
import java.net.Socket;

/**
 * Created by Jonat on 2017-03-16.
 */
public class Joueur {
    int port;
    String ip;
    Socket s;
    BufferedReader rd;
    PrintWriter writer;

    public Joueur()
    {
        try {
            port = 51007;
            ip = "149.56.47.97";
            s = new Socket(ip, port);
            rd = new BufferedReader(new InputStreamReader(s.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void CONNEXION() {
        writer.print("HELLO Saheelirais jocharphil123" + "\n");
        writer.flush();
    }

    public void GOTO(int NodeId) {
        writer.print("GOTO " + NodeId + "\n");
        writer.flush();
    }

    public void BUILD() {
        writer.print("BUILD" + "\n");
        writer.flush();
    }

}
