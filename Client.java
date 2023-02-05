import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	
	private static Socket client;
	public static int port = 9999, myClientNum, numPlayers, counter;
	public static String ip = "127.0.0.1", numbers, operator, numberGroup, op, clientGroup, serverGroup, playerName;
	public static DataOutputStream serverOut, clientOut;
	public static DataInputStream serverIn, clientIn;
	public static InputStream inputFromServer;
	public static OutputStream outputToServer;
	public static String[] hackablePlayers, hostList;
	private static boolean alive = true, dead = true, playerNotChosen = true;
	public static Scanner scan = new Scanner(System.in);
	
	public static void main(String [] args)throws IOException{
		
		createClientSocket();
		inputFromServer = client.getInputStream();
		serverIn = new DataInputStream(inputFromServer);
		outputToServer = client.getOutputStream();
		clientOut = new DataOutputStream(outputToServer);
		getClientNum();
		sendPlayerName();
		
		if(myClientNum == 1){
			System.out.println("You are a host!");
			hostList = new String[15];
			while(alive){
				hostCode();
			}
			while(dead){
				deadCode();
			}
		}
		
		if(myClientNum == 2){
			System.out.println("You are a technician!");
			hostList = new String[15];
			while(alive){
				techCode();
			}
			while(dead){
				deadCode();
			}
		}
		
		if(myClientNum == 3){
			System.out.println("You are a hacker!!");
			hackablePlayers = new String[13];
			while(alive){
				hackerCode();
			}
			while(dead){
				deadCode();
			}
		}
	}
	
			
	//create the client socket that will connect to the server
	public static void createClientSocket() throws IOException{
		client = new Socket(ip,port);
		System.out.println("**** Client Connection Established ****");
	}
	
	public static void getClientNum(){
		try {
			myClientNum = serverIn.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IO Exception in getClientNum()");
		}
	}
	
	public static void sendClientInput() throws IOException{
		//send number of players if client number is 0//
			String myMessage = scan.nextLine();
			clientOut.writeUTF(myMessage);
	}
	
	public static void sendPlayerName() throws IOException{
		System.out.print("Please enter a player name... ");
		playerName = scan.nextLine();
		clientOut.writeUTF(playerName);
	}
	
	public static void readServerOutput() throws IOException{
		
		//receive the client numbers 1 and 2 from the server and store them into numberGroup
		System.out.println("Wating for server message\n");
		System.out.println(serverIn.readUTF());
	}
	
	public static void deadCode() throws IOException{
		System.out.println(serverIn.readUTF());
	}
	
	public static void techCode() throws IOException{
		String hackerGuess;
		String answer;
		String serverMessage;
		String tempHostName;
		String hackedMessage = "YOU HAVE BEEN HACKED!!!", 
			   erasedMalware = "YOUR MALWARE HAS BEEN ERASED!!", 
			   suspectedHacker = "YOU WERE SUSPECTED OF HACKING!!!",
			   machineNotRecovered = "YOUR MACHINE WAS NOT RECOVERED";
 
		serverMessage = serverIn.readUTF();
		
			if(serverMessage.equals(hackedMessage)){
				System.out.println();
				System.out.println(serverMessage);
				if(serverIn.readUTF().equals(erasedMalware)){
					System.out.println(erasedMalware);
				}else if(serverMessage.equals(suspectedHacker)){
					System.out.println(suspectedHacker);
					alive = false;
				}/*else if(serverMessage.equals(machineNotRecovered)){
					System.out.println(machineNotRecovered);
					alive = false;
				}*/else{
					System.out.println(machineNotRecovered);
					alive = false;
				}
				
			}else{
				
				System.out.println(serverMessage);
				System.out.println();
				
				System.out.println(serverIn.readUTF());
				System.out.println();
				
				answer = scan.nextLine();
				answer = answer.toLowerCase();
				clientOut.writeUTF(answer);
				
				if(answer.equals("yes")){
					System.out.println(serverIn.readUTF());
					
					System.out.println();
					
					for(int i = 0; i < hostList.length; i++){
						tempHostName = serverIn.readUTF();
						hostList[i] = tempHostName;
						if(!hostList[i].equals(playerName))
							System.out.println(hostList[i]);
					}
					
					
					outerloop:
					while(playerNotChosen){
						answer = scan.nextLine();
						for(int i = 0; i < hostList.length; i++){
							if(answer.equals(hostList[i])){
								clientOut.writeUTF(answer);
								System.out.println("You have healed " + answer +"!");
								playerNotChosen = false;
								break outerloop;
							}else if(i != (hostList.length - 1) && !answer.equals(hostList[i])){
								System.out.println("Attempting to find " + answer);
							}else{
								System.out.println(answer + " not found!!!");
							}
						}
					}
				}else{
					
					for(int i = 0; i < 11; i++){
						tempHostName = serverIn.readUTF();
						hostList[i] = tempHostName;
						if(!hostList[i].equals(playerName))
							System.out.println(hostList[i]);
					}
				
				
					System.out.println("Who do you want to prosectue?");
				
					playerNotChosen = true;
				
					outerloop:
					while(playerNotChosen = true){
						hackerGuess = scan.nextLine();
						for(int i = 0; i < hostList.length; i++){
							if(hackerGuess.equals(hostList[i])){
							System.out.println(hackerGuess + " has been prosecuted!");
							clientOut.writeUTF(hackerGuess);
							playerNotChosen = false;
							break outerloop;
						}else if(i != (hostList.length - 1) && !hackerGuess.equalsIgnoreCase(hostList[i])){
							System.out.println("Attempting to find " + hackerGuess);
						}else{
							System.out.println(hackerGuess + " not found!!!");
						}
					}
				}
			}
		}		
	}
	
	
	public static void hostCode() throws IOException{
		String hackerGuess;
		String serverMessage = serverIn.readUTF();
		String tempHostName;
			
			
		if(serverMessage.equals("YOU HAVE BEEN HACKED!!!")){
			System.out.println();
			System.out.println(serverMessage);
			serverMessage = serverIn.readUTF();
			if(serverMessage.equals("YOUR MALWARE HAS BEEN ERASED!!")){
				System.out.println("YOUR MALWARE HAS BEEN ERASED!!");	
			}else if(serverMessage.equals("YOU WERE SUSPECTED OF HACKING!!!")){
				System.out.println("YOU WEREN'T THE HACKER, IF ONLY THEY KNEW THEY MESSED UP!");
			}else{
				System.out.println(serverMessage);
				System.out.println("YOUR MALWARE WAS NOT ERASED!!");
				alive = false;
			}
		}else{
			System.out.println();
			for(int i = 0; i < hostList.length; i++){
				tempHostName = serverIn.readUTF();
				hostList[i] = tempHostName;
			}
				
			System.out.println();
				
			for(int i = 0; i < hostList.length; i++){	
				if(!hostList[i].equals(playerName))
					System.out.println("Player " + (i + 1) + ": " + hostList[i]);
			}
					
			System.out.println(serverMessage);
			System.out.println();
					
			System.out.println(serverIn.readUTF());
					
			System.out.println();
					
					
					
			playerNotChosen = true;
			outerloop:
			while(playerNotChosen = true){
				hackerGuess = scan.nextLine();
				for(int i = 0; i < hostList.length; i++){
					if(hackerGuess.equals(hostList[i])){
						System.out.println("Sending your vote to the technician!");
						clientOut.writeUTF(hackerGuess);
						playerNotChosen = false;
						break outerloop;
					}else if(i != (hostList.length - 1) && !hackerGuess.equalsIgnoreCase(hostList[i])){
						System.out.println("Attempting to find " + hackerGuess);
					}else{
						System.out.println(hackerGuess + " not found!!!");
					}
				}
			}
		}
	}
		
	public static void hackerCode() throws IOException{
				
		String tempName;
		String hackedPlayer;
		String serverMessage;
			
		serverMessage = serverIn.readUTF();
		if( serverMessage.equals("YOU HAVE BEEN CAUGHT!!") ){
			System.out.println(serverMessage);
			alive = false;
		}else{
				
			System.out.println();
			System.out.println();
			System.out.println();
						
			System.out.println("nmap hosts");
			System.out.println();
			System.out.println("Scanning for hosts on the network...");
			System.out.println();
					
			for(int i = 0; i < hackablePlayers.length; i++){
				tempName = serverIn.readUTF();
				hackablePlayers[i] = tempName;
				System.out.println(tempName);	
			}
					
					
			playerNotChosen = true;
				
			outerloop:
			while(playerNotChosen = true){
				hackedPlayer = scan.nextLine();
				for(int i = 0; i < hackablePlayers.length; i++){
						if(hackedPlayer.equalsIgnoreCase(hackablePlayers[i])){
							System.out.println("Exploiting " + hackablePlayers[i]);
							clientOut.writeUTF(hackablePlayers[i]);
							playerNotChosen = false;
							break outerloop;
						}else if(i != (hackablePlayers.length - 1) && !hackedPlayer.equalsIgnoreCase(hackablePlayers[i])){
							System.out.println("Attempting to exploit " + hackedPlayer + "...");
						}else{
							System.out.println("That host does not exist on the network!!!");
					}
				}
			}
		}
	}
}		