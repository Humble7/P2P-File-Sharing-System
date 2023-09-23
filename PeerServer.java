import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class PeerServer extends Thread {
    int port;
    String filePath;
    ServerSocket peerServerSocket;
    Socket socket;
    public PeerServer(int port, String filePath) {
        this.port = port;
        this.filePath = filePath;
    }

    public void run() {
        try {
            peerServerSocket = new ServerSocket(port);
            socket = peerServerSocket.accept();
            new Downloader(socket, filePath).start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

class Downloader extends Thread {
    Socket clientSocket;
    String filePath;
    public Downloader(Socket clientSocket, String filePath) {
        this.clientSocket = clientSocket;
        this.filePath = filePath;
    }

    public void run() {
        try {
            ObjectOutputStream os = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream is = new ObjectInputStream(clientSocket.getInputStream());

            String fileName = (String)is.readObject();
            while (true) {
                File file = new File(filePath + "//" + fileName);
                long length = file.length();
                byte[] bytes = new byte[(int)length];

                os.writeObject((int)file.length());
                os.flush();

                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                bis.read(bytes, 0, (int)file.length());
                os.write(bytes, 0, bytes.length);
                os.flush();
                bis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}