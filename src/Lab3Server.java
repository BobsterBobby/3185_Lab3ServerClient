import java.net.*; 
import java.io.*;

public class Lab3Server {
    
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream input = null;
    
    public Lab3Server(int port) {
	
	try{
	    
            server = new ServerSocket(port);
            System.out.println("Server started");
            
            System.out.println("Waiting for a client ...");
            
            socket = server.accept();
            System.out.println("Client accepted");
            
            input = new DataInputStream(new BufferedInputStream(socket.getInputStream())); 
  
            String line = "";
            
            while (!line.equals("Over")) {
        	
                try {
                    
                    line = input.readUTF(); 
                    
                    System.out.println(line); 
                    
                } 
                catch(IOException i) {
                    
                    System.out.println(i);
                    
                } 
                
            }
            
            System.out.println("Closing connection");
            
            
        } 
        catch(IOException i) 
        { 
            System.out.println(i); 
        }
	finally
	{
            try {
		socket.close();
		input.close();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	
    }
    
    public static void main(String args[]) {
	
	Lab3Server server = new Lab3Server(5000);
	
    }

}
