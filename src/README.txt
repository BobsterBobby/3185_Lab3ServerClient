3185_Lab3ServerClientChecksum
Students:
Haochu Chen 300067058
Robert Zhang 300077171

How to Run codes:
First Step:
	Compile both java Codes using an environment that can run java (i.e.: Eclipse)
		
		or 
		
	Go to each directories on two different Command Prompt where you put the two files and run:
			>javac packet_receiver.java
			>javac packet_sender.java

Second Step:
	Go to each directories on two different Command Prompt where you put the two files and run:
			>java packet_receiver 9090			(Server)
		then
			>java packet_sender localhost 9090		(Client)

Third Step:
	In the packet_sender Command Prompt Enter Transmitted Value. i.e.:
			Enter text: COLOMBIA 2 - MESSI 0
	Then wait for reply
	
	Curruption case:
			Enter text: CorruptedCode

Final Step:
	In the packet_sender Command Prompt Enter:
			Enter text: bye
	To disconnect packet_sender.
	Ctrl + c to stop packet_receiver server
