import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import java.util.*;


/*
 * created nonHackerList & playerList
 * made nonHackerList size 13
 * made playerList size 15
 * 
 * in sendClientNum()
 * 		if host or tech
 * 			add to nonHackerList
 * 		if host or tech or hacker
 * 			add to playerList
 * 
 * in sendClientMessage()
 * 		write(nonHackerList[i]) //send hacker the nonHackerList
 * 
 * */

public class Server{
	
	private static Socket socket1, socket2, socket3, socket4, socket5, socket6, socket7, socket8, socket9, socket10, socket11, socket12, socket13, socket14, socket15;
	private static Socket [] socketList;
	private static ServerSocket listener;
	public static int port = 9999, counter = 0, numPlayers = 15;
	public static String ip = "127.0.0.1",  numbers, operator, numberGroup, op, clientGroup, serverGroup, answer;
	static DataOutputStream serverOut1, serverOut2, serverOut3, serverOut4, serverOut5, serverOut6, serverOut7, serverOut8, serverOut9, serverOut10, serverOut11, serverOut12, serverOut13, serverOut14, serverOut15;
	private static DataOutputStream [] outputStreamList, hackerOutputStreamList, hostOutputStreamList, techOutputStreamList;
	public static DataInputStream clientIn1, clientIn2, clientIn3, clientIn4, clientIn5, clientIn6, clientIn7, clientIn8, clientIn9, clientIn10, clientIn11, clientIn12, clientIn13, clientIn14, clientIn15;
	private static DataInputStream [] inputStreamList, hackerInputStreamList, hostInputStreamList, techInputStreamList;
	private static boolean serverRunning = true;
	public static Scanner scan = new Scanner(System.in);
	private static int[] characterNumber = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 3, 3};
	//create list to save players to nonHackerList to pass to hackers & playerList for techs & hosts
	public static String[] nonHackerList, playerList, hackedPlayers, hostList, techList, probableHackers, hackerList, techHealed, recoveredHosts, techHealedForTurn; 
	
/*

USE THIS IN THE COMMAND LINE FOR NOW. IT IS MUCH EASIER TO HANDLE MULTIPLE CLIENTS THAT WAY. AT LEAST UNTIL WE GET THE GAME RUNNING. WE CAN SEND MESSAGES TO THE SERVER FROM ALL THE CLIENTS NOW!!! YEET!

*/
	
	public static void main (String [] args) throws IOException{
      
		shuffle(characterNumber);
      
		nonHackerList = new String[numPlayers-2];//only needs 13 b/c 2 hackers
		playerList = new String[numPlayers]; //full list of players
		socketList = new Socket[numPlayers];
		hackedPlayers = new String[2];
		hostList = new String[11];
		techList = new String[2];
		hackerList = new String[2];
		techHealed = new String[2];
		techHealedForTurn = new String[2];
		recoveredHosts = new String[2];
		probableHackers = new String[numPlayers];
		outputStreamList = new DataOutputStream[numPlayers];
		hostOutputStreamList = new DataOutputStream[11];
		techOutputStreamList = new DataOutputStream[2];
		hackerOutputStreamList = new DataOutputStream[2];
		inputStreamList = new DataInputStream[numPlayers];
		hostInputStreamList = new DataInputStream[11];
		techInputStreamList = new DataInputStream[2];
		hackerInputStreamList = new DataInputStream[2];
		
		 
		outputStreamList[0] = serverOut1;
		outputStreamList[1] = serverOut2;
		outputStreamList[2] = serverOut3;
		outputStreamList[3] = serverOut4;
	    outputStreamList[4] = serverOut5;
	    outputStreamList[5] = serverOut6;
	    outputStreamList[6] = serverOut7;
	    outputStreamList[7] = serverOut8;
	    outputStreamList[8] = serverOut9;
        outputStreamList[9] = serverOut10;
        outputStreamList[10] = serverOut11;
	    outputStreamList[11] = serverOut12;
	    outputStreamList[12] = serverOut13;
	    outputStreamList[13] = serverOut14;
	    outputStreamList[14] = serverOut15;
		
		inputStreamList[0] = clientIn1;
		inputStreamList[1] = clientIn2;
		inputStreamList[2] = clientIn3;
		inputStreamList[3] = clientIn4;
		inputStreamList[4] = clientIn5;
		inputStreamList[5] = clientIn6;
		inputStreamList[6] = clientIn7;
		inputStreamList[7] = clientIn8;
		inputStreamList[8] = clientIn9;
		inputStreamList[9] = clientIn10;
		inputStreamList[10] = clientIn11;
		inputStreamList[11] = clientIn12;
		inputStreamList[12] = clientIn13;
		inputStreamList[13] = clientIn14;
		inputStreamList[14] = clientIn15;
		
		socketList[0] = socket1;
		socketList[1] = socket2;
		socketList[2] = socket3;
		socketList[3] = socket4;
		socketList[4] = socket5;
		socketList[5] = socket6;
		socketList[6] = socket7;
		socketList[7] = socket8;
		socketList[8] = socket9;
		socketList[9] = socket10;
		socketList[10] = socket11;
		socketList[11] = socket12;
		socketList[12] = socket13;
		socketList[13] = socket14;
		socketList[14] = socket15;
		
		createServerSocket();
		for(int i = 0; i < socketList.length; i++){
			outputStreamList[i] = new DataOutputStream(socketList[i].getOutputStream());
			inputStreamList[i] = new DataInputStream(socketList[i].getInputStream());
		}
		sendClientNum();
		recievePlayerNames();
		printPlayerNames();
		while(serverRunning){
			sendClientMessage();
		}
	}
	
	public static void createServerSocket() throws IOException{
		System.out.println("Wating for players to connect...");
			listener = new ServerSocket(port);
			for(int i = 0; i < socketList.length; i++){ 
				socketList[i] = listener.accept();
				System.out.println("Client " + (i + 1) + " connected!");
				System.out.println();
			}
	}

	public static void sendClientNum() throws IOException{
		
		int host = 0;
		int tech = 0;
		int hacker = 0;
		
		for(int i = 0; i < characterNumber.length; i++){
			outputStreamList[i].write(characterNumber[i]);
			if( characterNumber[i] == 1){
				hostOutputStreamList[host] = outputStreamList[i];
				hostInputStreamList[host] = inputStreamList[i];
				host++;
			}
			if( characterNumber[i] == 2){
				techOutputStreamList[tech] = outputStreamList[i];
				techInputStreamList[tech] = inputStreamList[i];
				tech++;
			}
			if( characterNumber[i] == 3){
				hackerOutputStreamList[hacker] = outputStreamList[i];
				hackerInputStreamList[hacker] = inputStreamList[i];
				hacker++;
			}
		}
	}
	
	private static void recievePlayerNames() throws IOException{
		int nonHacker = 0;
		int host = 0;
		int tech = 0;
		int hacker = 0;
		
		for(int i = 0; i < characterNumber.length; i++){
			String tempName = inputStreamList[i].readUTF();
			if( characterNumber[i] == 1 || characterNumber[i] == 2){ //if player is not hacker add to nonHackerList
				nonHackerList[nonHacker] = tempName;
				if(  characterNumber[i] == 1 ){
					hostList[host] = tempName;
					host++;
				}
				if( characterNumber[i] == 2){
					techList[tech] = tempName;
					tech++;
				}
				System.out.println();
				System.out.println("Non-hacker name recieved!!");
				
				nonHacker++;
				
			}else{
				hackerList[hacker] = tempName;
				hacker++;
			}
			
			System.out.println();
			playerList[i] = tempName;
			System.out.println("Player name recieved");
			
		}
			
	}
   
	private static void recieveHackerActions() throws IOException{
		System.out.println("Reading hacker responses...");
		
		for(int i = 0; i < hackedPlayers.length; i++){
			System.out.println("Reading hacker responses...");
			if(hackerList[i].equals("CAUGHT")){
				hackedPlayers[i] = "NONE";
			}else{
				hackedPlayers[i] = hackerInputStreamList[i].readUTF();
			}
		}
		
		System.out.println("The hacked players are");
		
		System.out.println();
		
		for(int i = 0; i < hackedPlayers.length; i++){
			
			System.out.println(hackedPlayers[i]);
		}
	}
	
	private static void recieveHostActions() throws IOException{
		System.out.println("Reading host responses....");
		for(int i = 0; i < (hostInputStreamList.length); i++ ){
			if(hostList[i].equals(hackedPlayers[0]) || hostList[i].equals("HACKED PLAYER") || hostList[i].equals("HACKED HOST")){
				System.out.println(hostList[i]);
				System.out.println("You were Hacked, get skipped!");
				probableHackers[i] = "HACKED PLAYER";
			}else if(hostList[i].equals(hackedPlayers[1]) || hostList[i].equals("HACKED PLAYER") || hostList[i].equals("HACKED HOST")){
				System.out.println(hostList[i]);
				System.out.println("You were Hacked, get skipped!");
				probableHackers[i] = "HACKED PLAYER";
			}else{
				String answer = hostInputStreamList[i].readUTF();
				probableHackers[i] = answer;//change this to account for the people who were hacked
			}
		}
	}
	
	private static void recieveTechActions() throws IOException{
			
		for(int i = 0; i < hackedPlayers.length; i++){
			if(hackedPlayers[i].equals(techHealed[0])||hackedPlayers[i].equals(techHealed[1])){
				for(int j = 0; j < hostList.length; j++){
					if(hostList[j].equals(hackedPlayers[i])){
						hostOutputStreamList[j].writeUTF("YOUR MALWARE HAS BEEN ERASED!!");
					}
				}
			}
			if(hackedPlayers[i].equals(techHealed[0])||hackedPlayers[i].equals(recoveredHosts[1])){
				for(int j = 0; j < techList.length; j++){
					if(techList[j].equals(hackedPlayers[i])){
						techOutputStreamList[j].writeUTF("YOUR MALWARE HAS BEEN ERASED!!");
					}
				}
			}
			else if(!hackedPlayers[i].equals(techHealed[0]) && !hackedPlayers[i].equals(techHealed[1])){
				for(int j = 0; j < hostList.length; j++){
					if(hackedPlayers[i].equals(hostList[j])){
						hostOutputStreamList[j].writeUTF("YOUR MACHINE WAS NOT RECOVERED");
						for(int k = 0; k < playerList.length; k++){
							if(hackedPlayers[i].equals(playerList[k])){
								playerList[k] = "HACKED HOST";
							}
						}
						for(int k = 0; k < nonHackerList.length; k++){
							if(hackedPlayers[i].equals(nonHackerList[k])){
								nonHackerList[k] = "HACKED HOST";
							}
						}
						hostList[j] = "HACKED HOST";
					}
				}
			}
			else if(!hackedPlayers[i].equals(techHealed[0]) && !hackedPlayers[i].equals(techHealed[1])){
				for(int j = 0; j < techList.length; j++){
					if(hackedPlayers[i].equals(techList[j])){
						techOutputStreamList[j].writeUTF("YOUR MACHINE WAS NOT RECOVERED");
						for(int k = 0; k < playerList.length; k++){
							if(hackedPlayers[i].equals(playerList[k])){
								playerList[k] = "HACKED HOST";
							}
						}
						for(int k = 0; k < nonHackerList.length; k++){
							if(hackedPlayers[i].equals(nonHackerList[k])){
								nonHackerList[k] = "HACKED HOST";
							}
						}
						techList[j] = "HACKED HOST";
					}
				}
				
			}
		}
		
		System.out.println("Reading tech responses...");
		for(int i = 0; i < (techInputStreamList.length); i++ ){
			if(techList[i].equals(hackedPlayers[0]) || techList[i].equals("HACKED HOST")) {
				System.out.println("You were Hacked, get skipped!");
				probableHackers[i] = "HACKED TECHNICIAN";
				System.out.println(probableHackers[i]);
			}else if(techList[i].equals(hackedPlayers[1])|| techList[i].equals("HACKED HOST")){
				System.out.println("You were Hacked, get skipped!");
				probableHackers[i] = "HACKED TECHNICIAN";
				System.out.println(probableHackers[i]);
			}else if(techHealedForTurn[i].equals("HEALED")){
				probableHackers[i] = "HEALED A PLAYER";
				System.out.println(probableHackers[i]);
			}else{
				String answer = techInputStreamList[i].readUTF();
				System.out.println(answer);
				for(int j = 0; j < hackerList.length; j++){
					if(hackerList[j].equals(answer)){
						hackerOutputStreamList[j].writeUTF("YOU HAVE BEEN CAUGHT!!");
						for(int k = 0; k < playerList.length; k++){
							if(hackerList[j].equals(playerList[k])){
								playerList[k] = "CAUGHT HACKER";
							}
						}
						hackerList[j] = "CAUGHT";
					}
				}
				for(int j = 0; j < hostList.length; j++){
					if(hostList[j].equals(answer)){
						hostOutputStreamList[i].writeUTF("YOU WERE SUSPECTED OF HACKING!!!");
					}
				}
				for(int j = 0; j < techList.length; j++){
					if(techList[j].equals(answer)){
						techOutputStreamList[i].writeUTF("YOU WERE SUSPECTED OF HACKING!!!");
					}
				}
			}
		}
	}
	
	private static void printPlayerNames(){
		
		System.out.println("Non-hacker players are: ");
		for(int i = 0; i < nonHackerList.length; i++){
			System.out.println("Player " + (i + 1) + ": " + nonHackerList[i]);
		}
		System.out.println("Host names are: ");
		for(int i = 0; i < playerList.length; i++){
			System.out.println("Player " + (i + 1) + ": " + playerList[i]);
		}
	}
	
	private static void sendClientMessage() throws IOException{
		
		
		for(int i = 0; i < hackerOutputStreamList.length; i++){
			if(hackerList[i].equals("CAUGHT")){
				hackerOutputStreamList[i].writeUTF(hackedPlayers[0] + " and " + hackedPlayers[1] + " have been hacked!!");
			}else if(techList[0].equals("HACKED HOST") && techList[1].equals("HACKED HOST")){
				for(int j = 0; j < hackerList.length; j++){
					hackerOutputStreamList[j].writeUTF("HACKERS WIN!!");
				}
			}else if(hackerList[0].equals("CAUGHT") && hackerList[1].equals("CAUGHT")){
				for(int j = 0; j < hackerList.length; j++){
					hackerOutputStreamList[j].writeUTF("HOSTS WIN!!");
				}
			}else{
			hackerOutputStreamList[i].writeUTF("Who would you like to hack?");
			for(int j = 0; j < nonHackerList.length; j++)
				hackerOutputStreamList[i].writeUTF(nonHackerList[j]);//player names
			}
		}
		
		recieveHackerActions();
		
		
		for(int i = 0; i < hostOutputStreamList.length; i++){
			if(hackedPlayers[0].equals(hostList[i]) || hackedPlayers[1].equals(hostList[i])){
				hostOutputStreamList[i].writeUTF("YOU HAVE BEEN HACKED!!!");
			}else if(techList[0].equals("HACKED HOST") && techList[1].equals("HACKED HOST")){
				for(int j = 0; j < hostList.length; j++){
					hostOutputStreamList[j].writeUTF("HACKERS WIN!!");
				}
			}else if(hostList[i].equals("HACKED HOST")){
				hostOutputStreamList[i].writeUTF(hackedPlayers[0] + " and " + hackedPlayers[1] + " have been hacked!!");
			}else{
				hostOutputStreamList[i].writeUTF(hackedPlayers[0] + " and " + hackedPlayers[1] + " have been hacked!!");
				for(int j = 0; j < playerList.length; j++){	
					hostOutputStreamList[i].writeUTF(playerList[j]);
				}
				hostOutputStreamList[i].writeUTF("Who do you think is the hacker?");
			}
		}
		
		recieveHostActions();
			
		String clientAnswer;
		String healedHost;
				
		
		for(int i = 0; i < techOutputStreamList.length; i++){
			if(hackedPlayers[0].equals(techList[i])||hackedPlayers[1].equals(techList[i])){
				techOutputStreamList[i].writeUTF("YOU HAVE BEEN HACKED!!!");
			}else if(techList[i].equals("HACKED HOST")){
				techOutputStreamList[i].writeUTF(hackedPlayers[0] + " and " + hackedPlayers[1] + " have been hacked!!");
				techHealed[i] = "HACKED";
			}else if(techList[0].equals("HACKED HOST") && techList[1].equals("HACKED HOST")){
				for(int j = 0; j < hackerList.length; j++){
					techOutputStreamList[j].writeUTF("HACKERS WIN!!");
				}
			}else if(hackerList[0].equals("CAUGHT") && hackerList[1].equals("CAUGHT")){
				for(int j = 0; j < techList.length; j++){
					techOutputStreamList[j].writeUTF("HOSTS WIN!!");
				}
			}else{
				techOutputStreamList[i].writeUTF(hackedPlayers[0] + " and " + hackedPlayers[1] + " have been hacked!!");
				
				techOutputStreamList[i].writeUTF("Would you like to run a malware scan?");
				
				clientAnswer = techInputStreamList[i].readUTF();
				
				if(clientAnswer.equals("yes")){
					techOutputStreamList[i].writeUTF("Which host deserves the malware scan?");
					
					for(int j = 0; j < playerList.length; j++){
						techOutputStreamList[i].writeUTF(playerList[j]);
					}
					
					healedHost = techInputStreamList[i].readUTF();
					
					
					for(int j = 0; j < hostList.length; j++){
						if(hostList[j].equals(healedHost)){
							techHealed[i] = healedHost;
							techHealedForTurn[i] = "HEALED";
						}
					}
					
					for(int j = 0; j < techList.length; j++)
						if(techList[j].equals(healedHost)){
							techHealed[i] = healedHost;
							techHealedForTurn[i] = "HEALED";
						}
					
				}else{
					
					techHealed[i] = "GUESSING";
					techHealedForTurn[i] = "GUESSING";
					
					for(int j = 0; j < hostList.length; j++){
						techOutputStreamList[i].writeUTF(probableHackers[j]);
					}
				}
			}
		}
		
		recieveTechActions();
		
	}
   public static void shuffle(int[] list){
      Random random = new Random();
      random.nextInt();
      for (int i= 0; i < list.length; i++){
         int change = i + random.nextInt(list.length-i);
         swap(list, i, change);
        }
    }

    private static void swap(int[] list, int i, int change) {
        int helper = list[i];
        list[i] = list[change];
        list[change] = helper;
    }
}