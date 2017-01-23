package clientos;

public class ReturnAnsToClients {

    public static void main(String[] args) {
//		int R1=2;
//		int R2=24;
//		for (int i = 0; i < 45; i++)
//		{
//			//Client client = new Client(i,0,100);
//			Client client = new Client(i,R1,R2);			
//			client.start();	
//		}
        Client client;
        if (args.length == 3) {

            client = new Client(args[2]);
        } else {
            client = new Client("1.txt");
        }

        //Client client = new Client("1.txt");
        client.start();

    }
}
