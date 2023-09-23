import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class IndexServer {
    public static ArrayList<FileDetail> globalArray = new ArrayList<FileDetail>();

    public IndexServer() {
        
        ServerSocket sSocket = null;
        Socket socket = null;

        try {
            sSocket = new ServerSocket(7799);
            System.out.println("Indexing Server Runing...");
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                socket = sSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            new Session(socket, globalArray).start();
        }
    }
}

class Session extends Thread {
    private Socket socket;
    ArrayList<FileDetail> indexList;
    public Session(Socket clientSocket, ArrayList<FileDetail> indexList) {
        this.socket = clientSocket;
        this.indexList = indexList;
    }

    ArrayList<FileDetail> appendList = new ArrayList<FileDetail>();
    ObjectOutputStream oos;
    ObjectInputStream ois;
    String targetFileName;
    
    public void run() {
        buildIndexing();
        searchPeers();
    }

    private void buildIndexing() {
        try {
            InputStream is = socket.getInputStream();
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(is);

            appendList = (ArrayList<FileDetail>)ois.readObject();
            indexList.addAll(appendList);
            System.out.println("Indexing Server updated!, total files count is " + indexList.size());
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void searchPeers() {

        try {
            targetFileName = (String) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        ArrayList<FileDetail> targetPeers = new ArrayList<FileDetail>();
        System.out.println("Searching...");

        for (int i = 0; i < indexList.size(); i++) {
            FileDetail fileDetail = indexList.get(i);
            if (fileDetail.fileName.equals(targetFileName)) {
                targetPeers.add(fileDetail);
            }
        }
        

        try {
            oos.writeObject(targetPeers);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
