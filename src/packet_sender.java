import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class packet_sender {
    private static String ipv4 = "4";
    private static String headerLength = "5";
    private static String tos = "00";
    private static String foffset = "4000";
    private static String ttl = "40";
    private static String tcpprotocol = "06";
    private static String sourceIp;
    private static String destinationIp;
    private static ArrayList<String> ary = new ArrayList<String>();

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
	    // int leftToFill = 4 - temp.length();
	    // for (int i = 0; i > leftToFill; i++)
	    // temp += "0";
	    packet += temp;
	}
	return packet;
    }

    private static String headerTos() {
	String packet = "";
	packet += ipv4;
	packet += headerLength;
	packet += tos;
	return packet;
    }

    private static String totalLengtHex(int headerLen, int payloadLen) {
	String packet = "";
	int totalLen = headerLen + payloadLen;
	packet += String.format("%04X", totalLen);
	return packet;
    }

    private static String iDField() {
	// String packet = "";
	// String idF = "1c46";
	// packet += idF;
	// return packet;

	String hex, hex0, hex1, hex2, hex3;
	int num0, num1, num2, num3;
	num0 = 0 + (int) (Math.random() * (15 - 0 + 1));
	hex0 = Integer.toHexString(num0);
	num1 = 0 + (int) (Math.random() * (15 - 0 + 1));
	hex1 = Integer.toHexString(num1);
	num2 = 0 + (int) (Math.random() * (15 - 0 + 1));
	hex2 = Integer.toHexString(num2);
	num3 = 0 + (int) (Math.random() * (15 - 0 + 1));
	hex3 = Integer.toHexString(num3);
	hex = hex0 + hex1 + hex2 + hex3;
	if (ary.contains(hex)) {
	    while (ary.contains(hex)) {
		num0 = 0 + (int) (Math.random() * (15 - 0 + 1));
		hex0 = Integer.toHexString(num0);
		num1 = 0 + (int) (Math.random() * (15 - 0 + 1));
		hex1 = Integer.toHexString(num1);
		num2 = 0 + (int) (Math.random() * (15 - 0 + 1));
		hex2 = Integer.toHexString(num2);
		num3 = 0 + (int) (Math.random() * (15 - 0 + 1));
		hex3 = Integer.toHexString(num3);
		hex = hex0 + hex1 + hex2 + hex3;
	    }
	}
	ary.add(hex);
	return hex;
    }

    private static String ttltcpProtocol() {
	String packet = "";
	packet += ttl;
	packet += tcpprotocol;
	return packet;
    }

    private static int hexBytenum(String hexPayload) {
	int byteNum = hexPayload.length() / 2;
	return byteNum;
    }

    private static boolean checksumcheck(String[] header) {
	boolean noError = false;
	String ans = "0000";
	int temp, temp1;
	int iAns = Integer.parseInt(ans, 16);
	// System.out.println(iAns);
	for (int i = 0; i < header.length; i++) {
	    int iAdd = Integer.parseInt(header[i], 16);
	    iAns += iAdd;
	    // System.out.println(String.format("%04X",iAdd));
	    // System.out.println(String.format("%04X",iAns) + "\n");
	}
	temp = iAns / 0x10000;
	temp1 = iAns % 0x10000;
	iAns = temp + temp1;
	iAns = (0xffff - iAns);
	// System.out.println(iAns);
	if (ans.equals(String.format("%04X", iAns)))
	    noError = true;
	return noError;
    }

    private static String checksum(String[] header) {
	String ans = "0000";
	int temp, temp1;
	int iAns = Integer.parseInt(ans, 16);
	// System.out.println(iAns);
	for (int i = 0; i < header.length; i++) {
	    int iAdd = Integer.parseInt(header[i], 16);
	    iAns += iAdd;
	    // System.out.println(String.format("%04X",iAdd));
	    // System.out.println(iAns);
	}
	temp = iAns / 0x10000;
	temp1 = iAns % 0x10000;
	iAns = temp + temp1;
	// System.out.println(String.format("%04X", iAns));
	iAns = (0xffff - iAns);
	ans = String.format("%04X", iAns);
	return ans;
    }

    private static String ofst() {
	return foffset;
    }

    public static void main(String[] args) {
	if (args.length < 2)
	    return;

	String hostname = args[0];
	int port = Integer.parseInt(args[1]);

	// System.out.println(hostname.compareTo("localhost"));

	StringBuffer hosthexa = new StringBuffer();
	StringBuffer localhexa = new StringBuffer();
	InetAddress host;

	// Getting and encode IPs
	try {
	    host = InetAddress.getByName(hostname);
	    String ipAddr = host.getHostAddress();
	    System.out.println("Destination IP: " + ipAddr);
	    String[] ipArray = ipAddr.split("\\.");
	    for (int i = 0; i < ipArray.length; i++) {
		// System.out.println(ipArray[i]);
		int temp = Integer.parseInt(ipArray[i]);
		if (temp >= 0 && temp <= 255) {

		    hosthexa.append(String.format("%02X", temp));
		    // System.out.println(hosthexa.toString());
		    // hosthexa.append('.');
		}
	    }
	    System.out.println("Destination IP Hex: " + packetSectioning(hosthexa.toString()).toString() + "\n");

	    InetAddress inetAddress = InetAddress.getLocalHost();
	    String localIpAddr = inetAddress.getHostAddress();
	    System.out.println("Source IP: " + localIpAddr);
	    String[] localIpArray = localIpAddr.split("\\.");
	    for (int i = 0; i < localIpArray.length; i++) {
		int temp = Integer.parseInt(localIpArray[i]);
		if (temp >= 0 && temp <= 255) {

		    localhexa.append(String.format("%02X", temp));
		    // localhexa.append('.');
		}
	    }
	    System.out.println("Source IP Hex: " + packetSectioning(localhexa.toString()).toString() + "\n");
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	}

	// IP Hex
	sourceIp = localhexa.toString();
	destinationIp = hosthexa.toString();

	// Test Methods \/;
	/*
	 * InetAddress self = InetAddress.getLoopbackAddress();
	 * System.out.println(self); System.out.println(headerTos());
	 * System.out.println(totalLengtHex(20, 20)); System.out.println(iDField());
	 * System.out.println(ttltcpProtocol());
	 * System.out.println(String.format("%04x",Integer.parseInt("262C8",
	 * 16)/0x10000));
	 * System.out.println(String.format("%04x",Integer.parseInt("262C8",
	 * 16)%0x10000));
	 * System.out.println(String.format("%04x",0xffff-(Integer.parseInt("262C8",
	 * 16)/0x10000 + Integer.parseInt("262C8", 16)%0x10000))); String[] cs = {
	 * "82C8", "f000", "f000" }; System.out.println(checksum(cs));
	 * System.out.println(ofst());
	 */

	try (Socket socket = new Socket(hostname, port)) {

	    OutputStream output = socket.getOutputStream();
	    PrintWriter writer = new PrintWriter(output, true);

	    Console console = System.console();
	    String text;

	    do {

		InputStream input = socket.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));

		String time = reader.readLine();

		System.out.println(time);

		text = console.readLine("Enter text: ");

		char ch[] = text.toCharArray();
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < ch.length; i++) {
		    String hexString = Integer.toHexString(ch[i]);
		    sb.append(hexString);
		}
		String result = sb.toString();
		System.out.println("Payload: " + result);
		System.out.println("Payload Length: " + hexBytenum(result) + "Byte(s)");

		String id = iDField();

		String[] toCheck = (packetSectioning(headerTos() + totalLengtHex(20, hexBytenum(result)) + id + ofst()
			+ ttltcpProtocol() + "0000" + sourceIp + destinationIp)).split("\\s+");

		String cksum = checksum(toCheck);

		if (checksumcheck(packetSectioning(headerTos() + totalLengtHex(20, hexBytenum(result)) + id + ofst()
			+ ttltcpProtocol() + cksum + sourceIp + destinationIp).split("\\s+"))) {

		}

		String sendCode = headerTos() + totalLengtHex(20, hexBytenum(result)) + id + ofst() + ttltcpProtocol()
			+ cksum + sourceIp + destinationIp + result;

		sendCode = packetSectioning(sendCode);
		System.out.println("Send Code: " + sendCode);
		// System.out.println(
		// "Comp Code: 4500 0028 1c46 4000 4006 9D35 C0A8 0003 C0A8 0001 434f 4c4f 4d42
		// 4941 2032 202d 204d 4553 5349 2030");
		if (text.equals("bye"))
		    System.out.println("Disconnected from Server");
		// writer.println(result);

		writer.println(sendCode);

	    } while (!text.equals("bye"));

	    socket.close();

	} catch (UnknownHostException ex) {

	    System.out.println("Server not found: " + ex.getMessage());

	} catch (IOException ex) {

	    System.out.println("I/O error: " + ex.getMessage());
	}
    }
}