package card_game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

/**
 * A Simple Socket client that connects to our socket server
 *
 */



public class Client {

	public static int NULL_STATE = 0;
	public static int READY = 1;

	private String hostname;
	private int port;
	Socket socketClient;
	private BufferedReader in;
	private PrintWriter out;
	private Game game;
	private int player_number;
	private String nickname;



	public Client(String hostname, int port) throws IOException{
		this.hostname = hostname;
		this.port = port;

		System.out.println("Attempting to connect to " + this.hostname + ":" + this.port);
		socketClient = new Socket(hostname, port);
		System.out.println("Connection Established");

		System.out.println("Wait for all players do connect, thx man");
		in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
		out = new PrintWriter(socketClient.getOutputStream(), true);
	}



	@SuppressWarnings("unchecked")
	public void play() throws Exception {
		String[] splitted;
		String response;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


		try {
			while (true) {
				response = in.readLine();
				//System.out.println(response);  // Print de débug mega utile !
				splitted = response.split("\\s+");
				

				if (splitted[0].startsWith("ready")) { // tout les joueurs sont co et pret
					System.out.println("Choose a nickname bitch !");

					nickname = br.readLine();
					nickname = nickname.replace(' ', '_');
					System.out.println("hum " + nickname + " is really a pussy name ... but ok.");
					System.out.println("REMEMBER " + nickname + ", EVRYTHING STARTS AT 0 HERE, NOT 1, REMEMBER !");
					out.println("ready");
					System.out.println("Wait for all players to be ready, thx dude");
				}
				else if (splitted[0].startsWith("your_number"))  //on recupere son numero de joueur
					player_number = Integer.parseInt(splitted[1]);
				else if (splitted[0].startsWith("create_game")) { // on crée un jeu en local
					game = new Game(Integer.parseInt(splitted[1]));
					game.getPlayers().get(player_number).setNickname(nickname);
					out.println("game_created " + nickname);
				}
				else if (splitted[0].startsWith("nickname")) {
					int index = 0;
					for (int i = 2; i < splitted.length; i += 2) {
						index = Integer.parseInt(splitted[i]);
						game.getPlayers().get(index).setNickname(splitted[i + 1]);
					}
				}
				else if (splitted[0].startsWith("card_distribution")) { // distribution des mains

					for (int i = 2; i < splitted.length; ++i)
						game.getPlayers().get(Integer.parseInt(splitted[1])).getHand().add(game.getPlaying_cards().get(Integer.parseInt(splitted[i]) - 1));

					game.getPlayers().get(Integer.parseInt(splitted[1])).setOriginal_hand((Vector<PlayingCard>)(game.getPlayers().get(Integer.parseInt(splitted[1])).getHand().clone()));
					out.println("card_distributed");
				}
				else if (splitted[0].startsWith("printstate")) {
					for (int i = 0; i < game.getPlayers().size(); ++i) {
						if (i == player_number)
							game.getPlayers().get(i).print_state();
						else
							game.getPlayers().get(i).print_state_no_hand();
					}
				}
				else if (splitted[0].startsWith("give_hp")) {
					game.give_hp();
				}
				else if (splitted[0].startsWith("innondation")) {
					for (int i = 0; i < game.getInnondation_cards().size(); ++i){
						if (game.getInnondation_cards().get(i).getValue() == Integer.parseInt(splitted[1])) {
							game.setCurrent1(game.getInnondation_cards().get(i));
							game.getInnondation_cards().remove(i);
						}
					}
					for (int i = 0; i < game.getInnondation_cards().size(); ++i){
						if (game.getInnondation_cards().get(i).getValue() == Integer.parseInt(splitted[2])) {
							game.setCurrent2(game.getInnondation_cards().get(i));
							game.getInnondation_cards().remove(i);
						}	
					}

					System.out.println("Here are the two flooding cards :");
					game.getCurrent1().draw_card();
					game.getCurrent2().draw_card();
				}
				else if (splitted[0].startsWith("pick_card")) {
					PlayingCard picked = game.getPlayers().get(player_number).pick_card();
					out.println("card_picked " + picked.getValue());
					System.out.println("Wait evryone's pick now, dont be such a douchbag.");
				}
				else if (splitted[0].startsWith("card_picked")) {
					int index = 0;
					for (int i = 2; i < splitted.length; i += 2) {
						index = Integer.parseInt(splitted[i]);
						for (int j = 0; j < game.getPlayers().get(index).getHand().size(); ++j) {
							//		System.out.println("index player " + index + " " + "index card j " + j);
							if (game.getPlayers().get(index).getHand().get(j).getValue() == Integer.parseInt(splitted[i + 1])) {
								game.getPlayers().get(index).setCard_pickeddd(game.getPlayers().get(index).getHand().get(j));
								break;
							}
						}
					}
				}
				else if (splitted[0].startsWith("play_turn")) {
					game.play_turn();
					game.remove_hp();
					out.println("turn_played");
				}
				else if (splitted[0].startsWith("next_round")) {
					System.out.println("****END OF ROUND****");
					game.calc_score();
					game.switch_hand();
					game.reinitialization();
				}
				else if (splitted[0].startsWith("endgame")) {
					game.end_game();
				}
			}
			//out.println("QUIT");
		}
		finally {
			socketClient.close();
		}
	}
}

