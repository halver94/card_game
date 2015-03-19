package card_game;

import java.io.*;
import java.net.*;
import java.util.Vector;

public class Server {

	public static int NULL_STATE = 0;
	public static int READY = 1;
	public static int GAME_CREATED = 2;
	public static int CARD_DISTRIBUTED = 3;
	public static int CARD_PICKED = 4;
	public static int TURN_PLAYED = 5;

	private int number_of_players_wanted;
	private int number_of_players_connected;
	private Vector<PrintWriter> players_connected;
	int[] player_state;
	private int portnumb;
	Game game;



	public Server(int portnumb, int number_of_player) {

		this.portnumb = portnumb;
		this.number_of_players_wanted = number_of_player;
		this.number_of_players_connected = 0;
		this.players_connected = new Vector<PrintWriter>();

		player_state = new int[number_of_players_wanted];

		for (int i = 0; i < number_of_players_wanted; ++i)
			player_state[i] = NULL_STATE;

	}

	public void run() {

		ServerSocket ss;
		
		try {
			ss = new ServerSocket(portnumb); // ouverture d'un socket serveur sur port  
			System.out.println("En attente de toutes les connections");
			while (players_connected.size() < number_of_players_wanted) // attente en boucle de connexion (bloquant sur ss.accept)
			{
				new ClientThread(ss.accept(),this); // un client se connecte, un nouveau thread client est lancé
			}

			//check si les joueurs sont pret
			sendAll("ready", " (type ready if ready)?\n");

			while(!check_state(READY)) {  
				Thread.sleep(1000);
			}
			System.out.println("ALL PLAYERS READY");

			//attribution des numeros de joueurs a chacun
			for (int i = 0; i < players_connected.size(); ++i)
				send("your_number ", String.valueOf(i), i);

			// Creation du jeu pour tout le monde
			sendAll("create_game ", String.valueOf(number_of_players_connected));
			game = new Game(number_of_players_connected);


			while(!check_state(GAME_CREATED)) {  
				Thread.sleep(1000);
			}

			send_nickanme();
			Thread.sleep(1000); // synchro
			
			// distribution de cartes
			game.card_distribution();
			game.printstate();
			send_all_hands();
			while(!check_state(CARD_DISTRIBUTED)) {  
				Thread.sleep(1000);
			}
			System.out.println("hands send");


			//game loop
			boolean end_round = false;
			for (int i = 0; i < game.getNumber_of_players(); ++i) {
				game.give_hp();
				sendAll("give_hp", "");
				while (!end_round) {
					game.printstate();
					sendAll("printstate", "");

					game.pick_inondation();
					send_innondation();

					sendAll("pick_card", "");
					while(!check_state(CARD_PICKED)) {  
						Thread.sleep(1000);
					}			
					send_card_picked();

					Thread.sleep(1500); // tempo de synchro
					
					game.play_turn();
					game.remove_hp();
					sendAll("play_turn", "");
					while(!check_state(TURN_PLAYED)) {  
						Thread.sleep(1000);
					}	
					
					
					end_round = game.check_end_round();
					System.out.println("Next round");
				}

				sendAll("next_round", "");
				game.calc_score();
				game.switch_hand();
				game.reinitialization();
				end_round = false;

			}

			game.end_game();
			sendAll("endgame", "");

		}
		catch (Exception e) { 

		}
	}


	synchronized public void treat(String message, String sLast) {

		int player_number = Integer.parseInt(message);

		System.out.println("Client number " + player_number + " send : \"" + sLast + "\"");

		if (sLast.startsWith("ready")) 
			player_state[player_number] = READY;
		else if (sLast.startsWith("game_created")) {
			player_state[player_number] = GAME_CREATED;
			String[] splitted = sLast.split("\\s+");
			game.getPlayers().get(player_number).setNickname(splitted[1]);
		}
		else if (sLast.startsWith("card_distributed"))
			player_state[player_number] = CARD_DISTRIBUTED;
		else if (sLast.startsWith("card_picked")) {
			String[] splitted = sLast.split("\\s+");
			Player player = game.getPlayers().get(player_number);
			for (int i = 0; i < player.getHand().size(); ++i)
				if (player.getHand().get(i).getValue() == Integer.parseInt(splitted[1]))
					player.setCard_pickeddd(player.getHand().get(i));
			player_state[player_number] = CARD_PICKED;

		}
		else if (sLast.startsWith("turn_played"))
			player_state[player_number] = TURN_PLAYED;

	}

	public void send_nickanme() {
		String to_send = "nickname bourrage";

		for (int i = 0; i < players_connected.size(); ++i)
			to_send = to_send + " " + String.valueOf(i) + " " + game.getPlayers().get(i).getNickname();

		System.out.println("send nickname");
		sendAll(to_send, "");		
	}
	
	public void send_card_picked() {
		String to_send = "card_picked bourrage";

		for (int i = 0; i < players_connected.size(); ++i)
			to_send = to_send + " " + String.valueOf(i) + " " + String.valueOf(game.getPlayers().get(i).getCard_pickeddd().getValue());

		sendAll(to_send, "");
	}


	public synchronized void send_innondation() {
		String to_send = "innondation ";

		int innondation1 = game.getCurrent1().getValue();
		int innondation2 = game.getCurrent2().getValue();

		to_send = to_send + String.valueOf(innondation1) + " " + String.valueOf(innondation2);

		sendAll(to_send, "");
	}

	public synchronized void send_all_hands() {

		Player player;
		int current_card_value = 0;
		String to_send = "card_distribution ";

		for (int i = 0; i < players_connected.size(); ++i) {
			player = game.getPlayers().get(i);
			to_send += String.valueOf(i);
			for (int j = 0; j < player.getHand().size(); ++j){
				current_card_value = player.getHand().elementAt(j).getValue();
				to_send = to_send + " " + String.valueOf(current_card_value);
			}
			sendAll(to_send, "");
			to_send = "card_distribution ";
		}

	}

	public boolean check_state(int state) {
		for (int i = 0; i < number_of_players_wanted; ++i)
			if (player_state[i] != state)
				return false;
		return true;
	}

	public void print_state() {
		for (int i = 0; i < number_of_players_wanted; ++i)
			System.out.println("Client " + i +" is " + player_state[i]);
	}


	//** Methode : envoie le message à un seul client **
	synchronized public void send(String message,String sLast, int index)
	{
		PrintWriter out = (PrintWriter) players_connected.elementAt(index); // extraction de l'élément courant (type PrintWriter)
		if (out != null) // sécurité, l'élément ne doit pas être vide
		{
			// ecriture du texte passé en paramètre (et concaténation d'une string de fin de chaine si besoin)
			out.println(message+sLast);
			out.flush(); // envoi dans le flux de sortie
		}
	}



	//** Methode : envoie le message à tous les clients **
	synchronized public void sendAll(String message,String sLast)
	{
		PrintWriter out; // declaration d'une variable permettant l'envoi de texte vers le client
		for (int i = 0; i < players_connected.size(); i++) // parcours de la table des connectés
		{
			out = (PrintWriter) players_connected.elementAt(i); // extraction de l'élément courant (type PrintWriter)
			if (out != null) // sécurité, l'élément ne doit pas être vide
			{
				// ecriture du texte passé en paramètre (et concaténation d'une string de fin de chaine si besoin)
				out.println(message+sLast);
				out.flush(); // envoi dans le flux de sortie
			}
		}
	}

	//** Methode : détruit le client no i **
	synchronized public void delClient(int i)
	{
		number_of_players_connected--; // un client en moins ! snif
		if (players_connected.elementAt(i) != null) // l'élément existe ...
		{
			players_connected.removeElementAt(i); // ... on le supprime
		}
	}

	//** Methode : ajoute un nouveau client dans la liste **
	synchronized public int addClient(PrintWriter out)
	{
		number_of_players_connected++; // un client en plus ! ouaaaih
		players_connected.addElement(out); // on ajoute le nouveau flux de sortie au tableau
		return players_connected.size()-1; // on retourne le numéro du client ajouté (size-1)
	}

	public int getNumber_of_players_wanted() {
		return number_of_players_wanted;
	}
	public void setNumber_of_players_wanted(int number_of_players_wanted) {
		this.number_of_players_wanted = number_of_players_wanted;
	}
	public int getNumber_of_players_connected() {
		return number_of_players_connected;
	}
	public void setNumber_of_players_connected(int number_of_players_connected) {
		this.number_of_players_connected = number_of_players_connected;
	}

}