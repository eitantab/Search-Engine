package ServerP;

import java.net.Socket;

public class VectorNode
{
	public int Data;
	public int ID;
	public Socket socket;
	public boolean sent;
	
	public VectorNode(int id, int data)
	{
		Data = data;
		ID = id;
		socket = null;
		sent = false;
	}
	public VectorNode(int id, Socket s)
	{
		Data = 0;
		ID = id;
		socket = s;
		sent = false;
	}

	@Override
	public String toString() {
		return "VectorNode [Data=" + Data + ", ID=" + ID + ", socket=" + socket + ", sent=" + sent + "]";
	}
	
	
}
