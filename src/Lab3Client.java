import java.net.*;
import java.io.*;

public class Lab3Client {

    private Socket socket = null;
    private BufferedReader input = null;
    private DataOutputStream output = null;

    public Lab3Client(String address, int port) {

	try {

	    socket = new Socket(address, port);
	    System.out.println("Connected");

	    input = new BufferedReader(new InputStreamReader(System.in));
	    output = new DataOutputStream(socket.getOutputStream());

	    System.out.println("Hi");

	    String line = "";

	    while (!line.equals("Over")) {

		try {

		    line = input.readLine();
		    
		    output.writeUTF(line);

		} catch (IOException i) {

		    System.out.println(i);

		}
	    }

	} catch (UnknownHostException u) {

	    System.out.println("u");
	    System.out.println(u);

	} catch (IOException i) {

	    System.out.println("i");
	    System.out.println(i);

	} finally {
	    try {
		if (input != null)
		    input.close();
		if (output != null)
		    output.close();
		if (socket != null)
		    socket.close();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

    }

    public static void main(String args[]) {

	Lab3Client client = new Lab3Client("192.168.0.3", 5000);

    }

}
