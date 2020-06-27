import java.io.*;
import java.net.*;
import java.util.Date;

public class packet_receiver {

    private static String packetSectioning(String hex) {
	String packet = "";
	String temp = "";
	for (int i = 0; i < hex.length(); i++) {
	    temp += hex.charAt(i);
	    if ((i + 1) % 4 == 0) {
		packet += temp;
		packet += " ";
		temp = "";
	    }
	}
	if (temp.length() % 4 > 0) {
	    packet += temp;
	}
	return packet;
    }

    private static boolean checksum(String[] header) {
	boolean noError = false;
	String ans = "0000";
	int temp, temp1;
	int iAns = Integer.parseInt(ans, 16);
//	System.out.println(iAns);
	for (int i = 0; i < header.length; i++) {
	    int iAdd = Integer.parseInt(header[i], 16);
	    iAns += iAdd;
//	    System.out.println(String.format("%04X",iAdd));
//	    System.out.println(String.format("%04X",iAns) + "\n");
	}
	temp = iAns / 0x10000;
	temp1 = iAns % 0x10000;
	iAns = temp + temp1;
	iAns = (0xffff - iAns);
//	System.out.println(iAns);
	if (ans.equals(String.format("%04X", iAns)))
	    noError = true;
	return noError;
    }

    private static String retreaveHeader(String[] hexMessage) {
	String header = "";
	for (int i = 0; i < 10; i++) {
	    header += hexMessage[i];
	}
	return header;
    }

    private static String retreavePayload(String[] hexMessage) {
	String tail = "";
	for (int i = 10; i < hexMessage.length; i++) {
	    tail += hexMessage[i];
	}
	return tail;
    }

    // Local method tester \/;
    /*
     * private static String[] testAL() { String[] testArrayList = new String[] { //
     * Header "4500", "0028", "1c46", "4000", "4006", "9D35", "C0A8", "0003",
     * "C0A8", "0001", // Payload "434f", "4c4f", "4d42", "4941", "2032", "202d",
     * "204d", "4553", "5349", "2030" }; return testArrayList; }
     */

    public static void main(String[] args) {
	if (args.length < 1)
	    return;

	int port = Integer.parseInt(args[0]);

	try (ServerSocket serverSocket = new ServerSocket(port)) {

	    System.out.println("Server is listening on port " + port);

	    while (true) {
		// Establish socket
		Socket socket = serverSocket.accept();

		System.out.println("New client connected");

		InputStream input = socket.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));

		OutputStream output = socket.getOutputStream();
		PrintWriter writer = new PrintWriter(output, true);

		// Connection Feedback
		writer.println("Connection time: " + new Date().toString());
		String text;

		//Decoder \/
		do {
		    text = reader.readLine();
		    System.out.println("Received: " + text);

		    // Testing \/
		    /*
		     * String[] cs = { "82C8", "f000", "f000", "9D35" };
		     * System.out.println(checksum(cs));
		     * 
		     * System.out.println(packetSectioning(retreaveHeader(testAL())));
		     * System.out.println(packetSectioning(retreavePayload(testAL())));
		     */

		    String head;
		    head = retreaveHeader((text).split("\\s+"));
		    System.out.println("Header: " + packetSectioning(head));
		    // System.out.println(checksum((packetSectioning(head)).split("\\s+")));

		    if (checksum((packetSectioning(head)).split("\\s+"))) {
			String payload;
			payload = retreavePayload((text).split("\\s+"));

			System.out.println("Payload: " + packetSectioning(payload));

			String decodedText = new String();
			char[] charArray = payload.toCharArray();
			for (int i = 0; i < charArray.length; i = i + 2) {
			    String st = "" + charArray[i] + "" + charArray[i + 1];
			    char ch = (char) Integer.parseInt(st, 16);
			    decodedText = decodedText + ch;
			}

			System.out.println("decode complete\n--> " + decodedText);
			if (decodedText.equals("bye")) {
			    System.out.println("Client disconnected");
			    break;
			}
			writer.println("Server reply: " + decodedText + "\n");

		    } else {
			
			System.out.println("The verification of the checksum demonstrates that the packet received is corrupted. Packet discarded!");
			writer.println("The verification of the checksum demonstrates that the packet received is corrupted. Packet discarded!");

		    }

		} while (!text.equals("bye"));

		socket.close();
	    }

	} catch (IOException ex) {
	    System.out.println("Server exception: " + ex.getMessage());
	    ex.printStackTrace();
	}
    }
}