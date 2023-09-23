import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    String filePath = "";
    int peerServerPortNumber = 0;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    Socket serverSocket;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    String downloadFileName;
    
    public Client() {
        try {
            serverSocket = new Socket("localhost",7799);
            ois = new ObjectInputStream(serverSocket.getInputStream());
            oos = new ObjectOutputStream(serverSocket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void start() {
        try {
            System.out.println("Client is running...");
            System.out.println("Register your directory:");
            filePath = br.readLine();
            System.out.println("Enter port number where the peer should act as a server:");
            peerServerPortNumber = Integer.parseInt(br.readLine());

            launchPeerSever();

            registerFilesToSever();

            downloadFile();

        } catch (Exception e) {
            System.out.println("Error in building connection.");
        }
    }

    private void launchPeerSever() {
        PeerServer peerServer = new PeerServer(peerServerPortNumber, filePath);
        peerServer.start();
    }

    private void registerFilesToSever() {
        try {
            System.out.println("Connection with Indexing Server has been established.");

            System.out.println("Enter the peer ID for the directory");
            int pid = Integer.parseInt(br.readLine());
            File folder = new File(filePath);
            File[] files = folder.listFiles();
            FileDetail curFileDetail;
            
            List<FileDetail> fileList = new ArrayList<>();
            for (int i = 0; i < files.length; i++) {
                curFileDetail = new FileDetail();
                File file = files[i];
                curFileDetail.fileName = file.getName();
                curFileDetail.peerID = pid;
                curFileDetail.portNumber = peerServerPortNumber;
                fileList.add(curFileDetail);
            }

            oos.writeObject(fileList);
            System.out.println("Register files success." + fileList);

            System.out.println("Enter the download file name:");
            downloadFileName = br.readLine();
            oos.writeObject(downloadFileName);

            System.out.println("Downloading from Indexing Server...");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void downloadFile() {
        try {
            List<FileDetail> peers = (ArrayList<FileDetail>) ois.readObject();
            for (int i = 0; i < peers.size(); i++) {
                int pid = peers.get(i).peerID;
                int port = peers.get(i).portNumber;
                System.out.println("The peer id of the file is " + pid + "and port is " + port);
                System.out.println("Choose a port number to download file: ");
                int downloadPort = Integer.parseInt(br.readLine());
                System.out.println("Choose the peer id to download file");
                int downloadPid = Integer.parseInt(br.readLine());

                Socket downloadSocket = new Socket("localhost", downloadPort);
                ObjectOutputStream downloadOOS = new ObjectOutputStream(downloadSocket.getOutputStream());
                ObjectInputStream downloadOIS = new ObjectInputStream(downloadSocket.getInputStream());
                downloadOOS.writeObject(downloadFileName);
                int bytesOfFile = (int) downloadOIS.readObject();
                byte[] bytes = new byte[bytesOfFile];
                downloadOIS.readFully(bytes);

                // save the file
                OutputStream fileStream = new FileOutputStream(filePath + "//" + downloadFileName);
                BufferedOutputStream bos = new BufferedOutputStream(fileStream);
                bos.write(bytes, 0, bytesOfFile);

                System.out.println(downloadFileName + "has been downloaded.");
                System.out.println("total bytes:" + bytesOfFile + "bytes." );
                System.out.println("Display file " + downloadFileName);
                bos.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}