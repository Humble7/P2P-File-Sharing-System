import java.io.BufferedReader;
import java.io.InputStreamReader;

public class FSS {
    public static void main(String args[]) throws Exception {
        System.out.println("Choose the running mode:");
        System.out.println("1. Indexing Server ");
        System.out.println("2. Client");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int option = Integer.parseInt(br.readLine());
        while (option != 1 && option != 2) {
            System.out.println("Your option should be 1 or 2");
            option = Integer.parseInt(br.readLine());
        }
        if (option == 1) {
            IndexServer server = new IndexServer();
        } else if (option == 2) {
            Client client = new Client();
            client.start();
        }
    }
}