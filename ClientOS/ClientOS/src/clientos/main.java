package clientos;

import java.net.Socket;
import java.util.Vector;

import ServerP.VectorNode;

public class main {

    Vector<VectorNode> vectorNodes;

    public static void main(String[] args) {

        int number = 50;
        int range = 100;

        if (args.length == 2) {
            number = Integer.parseInt(args[0]);
            range = Integer.parseInt(args[1]);
        }

        for (int i = 0; i < number; i++) {
            int rand = (int) (Math.random() * range) + 1;
            Client client = new Client("./ProbabilityFiles/" + rand + ".txt",+i);
            client.start();
        }

    }

    public class ReturnAnsToClients extends Thread {

        private int Data;
        private int ID;

        ReturnAnsToClients(VectorNode newVectorNode) {
            Data = newVectorNode.Data;
            ID = newVectorNode.ID;
        }

        @Override
        public void run() {
            Socket socket = vectorNodes.get(ID).socket;

            try {

                //				ObjectOutputStream outToClient = new ObjectOutputStream(socket.getOutputStream());
                //				ObjectInputStream inFromClient = new ObjectInputStream(socket.getInputStream());
                //				
                //				outToClient.writeObject(new VectorNode(34,45));
                //				inFromClient.readObject();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // TODO Auto-generated method stub
            super.run();
        }

    }

}
