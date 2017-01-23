package clientos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

public class Client extends Thread
{
	String DELIM = ",";
	private int ID;
	public Integer message;
	private int num;
	public Socket socket;
	private int r2=0;;
	///////////
	private int r1 = 0;
    private String name;
    private String fileName;
    private double[] S;

    private static int id = 0;

	private ObjectOutputStream outToServer;
	private ObjectInputStream inFromClient ;

//	 public Client(String fileName, String name) {
//	        this.fileName = fileName;
//	        this.name = name;
//	    }

	    /**
	     * Constructor
	     *
	     * @param fileName file with R1, R2 and probability value
	     */
	    public Client(String fileName) {
	        this.fileName = fileName;
	        this.name = "Client ";
	    }
            
            public Client(String fileName, int ID) {
	        this.fileName = fileName;
	        this.ID = ID;
	    }
		private void buildSumArray() {
	        BufferedReader br = null;

	        try {
	            br = new BufferedReader(new FileReader(this.fileName));
	            String fileData = br.readLine();
	            StringTokenizer stringTokenizer = new StringTokenizer(fileData, DELIM);

	            int r1 = Integer.parseInt(stringTokenizer.nextToken());
	            int r2 = Integer.parseInt(stringTokenizer.nextToken());

	            this.r1 = r1;

	            S = new double[r2 - r1 + 1];

	            S[0] = Double.parseDouble(stringTokenizer.nextToken());
	            for (int i = 1; i < r2 - r1 + 1; i++) {
	                S[i] = Double.parseDouble(stringTokenizer.nextToken()) + S[i - 1];
	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                if (br != null) {
	                    br.close();
	                }
	            } catch (IOException ex) {
	                ex.printStackTrace();
	            }
	        }
	    }
	    public static int binarySearchBetween(double[] arr, double value) {
	        int end = arr.length - 1;
	        int low = 0, high = end;
	        if (value < arr[0]) {
	            return 0;
	        }
	        if (value > arr[end]) {
	            return end + 1;
	        }
	        while (low <= high) {
	            int middle = (low + high) / 2;
	            if (low == high) {// stop search
	                return low;
	            } else {
	                if (arr[middle] == value) {//value was found
	                    return middle;
	                }
	                if (value < arr[middle]) {// value suppose to be left
	                    high = middle;
	                } else {// value suppose to be right
	                    low = middle + 1;
	                }
	            }
	        }
	        return -1;
	    }
	    private int getNextQuery() {
	        double rand = Math.random();
	        return binarySearchBetween(S, rand) + this.r1;
	    }
//////////////////////////////////
//	public Client(int Id,int r1,int r2)
//	{
//		this.ID =Id;
//		this.num =0;
//		this.r1 = r1;
//		this.r2 = r2;
//
//		try {
//			socket = new Socket("localhost",4444);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	@Override
	public void run()
	{
		try
		{
			socket = new Socket("localhost",4444);
			ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream inFromClient = new ObjectInputStream(socket.getInputStream());
            buildSumArray();
            
			while(true)
			{
				this.num  = getNextQuery();
				this.message = (Integer)(num);

				//	ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
				//	ObjectInputStream inFromClient = new ObjectInputStream(socket.getInputStream());

				//System.out.println("Question from client ID: "+ID+" is: "+num);
				System.out.println("Client<"+ID+"> : sending "+num);
				outToServer.writeObject(message);

				message = (Integer)inFromClient.readObject();

				//inFromClient.readObject();
				//System.out.println("ans from server to client ID: "+ID+" : "+message);
				System.out.println("Client<"+ID+">  : got reply "+message+" for query "+num);
				//socket.close();
				System.out.println("client Id: "+ID+ " is closed");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	void sendAndRecieve()
	{

	}
	//	public void startClient() throws UnknownHostException, Exception
	//	{
	//		ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
	//		ObjectInputStream inFromClient = new ObjectInputStream(socket.getInputStream());
	//				
	//		System.out.println("Question from client ID: "+ID+" is: "+num);
	//		
	//		outToServer.writeObject(message);
	//		
	//		message = (String) inFromClient.readObject();
	//		//inFromClient.readObject();
	//		System.out.println("ans from server to client ID: "+ID+" : "+message);
	//		
	////		//wait to ans from server
	////		message = bufferedReader.readLine();
	////		num = Integer.parseInt(message);
	//		
	//		socket.close();
	//		System.out.println("client Id: "+ID+ " is closed");
	//	}
	public Socket GetSocket(){
		return this.socket;
	}

}
