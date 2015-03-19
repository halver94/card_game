package card_game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	public static void print_rules() {
		
		System.out.println("*/*/*/*/ THE RULES \\*\\*\\*\\*");
		System.out.println("------SERVER------\n If you're the server, dont do anything, just wait \n for all client connexions.");
		System.out.println("You can play by launching an other window");

		System.out.println("------CLIENT------");
		System.out.println(" 1) Two flooding card are picked randomly.");
		System.out.println(" 2) all players choose a card in their hand.");
		System.out.println(" 3) the greater player card value pick the grater flooding card value");
		System.out.println(" 3b) the second greater player card value pick the second flooding card");
		System.out.println(" 4) Once flooding card are distributed, the player with the grater flooding card loose a life");
		System.out.println(" 4b) no life = dead");
		System.out.println(" 5) repeat steps 1-4 until only 2 players are alive or no more card in hand");
		System.out.println(" 6) all players score is calculated (score = life remaining)");
		System.out.println(" 7) evryone gives his hand to the player at his left");
		System.out.println(" 8) repeat 1-7 until you got your first hand");
		System.out.println("\n The WINNER is the one with the highest score");		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//GameGUI gui = new GameGUI();
		//gui.display();
		int number_of_player = 0;
		int port_numb = 6666;
		String str = null;

		BufferedReader br = null;
		int answer = 0;

		try {
			print_rules();
			System.out.print("\n\nserver ou client (0/1) ? ");
			br = new BufferedReader(new InputStreamReader(System.in));

			str = br.readLine();

		
			while (!str.matches("^[0-1]+$")) {
				System.out.println("Enter 0 or 1 you sicktard");
				str = br.readLine();
			}
			answer = Integer.parseInt(str);

			if (answer == 0){
				System.out.print("Combien de joueurs ? ");

				while (number_of_player < 3 || number_of_player > 5) {
					str = br.readLine();
					if (str.matches("^-?[0-9]+$")) {
						number_of_player = Integer.parseInt(str);
						if (number_of_player < 3 || number_of_player > 5)
							System.out.print("3-5 joueurs, Combien de joueurs ? ");
						else
							break;
					}
					else {
						System.out.println("Enter a number you dumbass");
						System.out.print("Combien de joueurs ? ");
					}
				}
				card_game.Server server = new Server(port_numb, number_of_player);
				server.run();

			}
			else {
				String IPADDRESS_PATTERN = 
						"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
								"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
								"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
								"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

				Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);

				System.out.println("Please type the adresse you want to connect!");
				str = br.readLine();
				Matcher matcher = pattern.matcher(str);

				while(!matcher.matches()) {
					System.out.println("Invalid ip format, retry please");
					str = br.readLine();
					matcher = pattern.matcher(str);
				}
				Client client = new Client(str, port_numb);
				client.play();

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


}