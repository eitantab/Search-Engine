package serveros;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class main {

    public static void main(String[] args) throws UnknownHostException, Exception {
        //Server server = new Server(6,100,5,3,20);
        //Client client = new Client(i,0,100);
        int S = 6;//number of allowed S-threads.
        int C = 100;//size of the cache.
        int M = 5;//the least number of times a query has to be requested in order to be allowed to enter the cache.
        int L = 25;//to specify the range [1; L] from which missing replies will be drawn uniformly at random.
        int Y = 20;//number of reader threads.

        if (args.length == 5) {
            S = Integer.parseInt(args[0]);
            C = Integer.parseInt(args[1]);
            M = Integer.parseInt(args[2]);
            L = Integer.parseInt(args[3]);
            Y = Integer.parseInt(args[4]);
        }
        
        Server server = new Server(Y, L, Y, M, C);


        server.start();

        //ClientGenerator clientGenerator = new ClientGenerator(S,C,M,L,Y,R1,R2,numOfClients);
    }

}
