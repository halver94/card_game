package card_game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

public class Player {

	public Player(int player_number) {
		super();
		this.player_number = player_number;
		this.hand = new Vector<PlayingCard>();
		this.original_hand = new Vector<PlayingCard>();
		this.innondation_card = new InnondationCard(0);
		this.life = 0;
		this.score = 0;
		this.isalive = true;
	}


	public PlayingCard pick_card() throws NumberFormatException, IOException {
		System.out.println("Pick a card in your hand " + nickname + "(the position, not the value)");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		int value = 15;
		String str = null;


		while (value >= hand.size() || value < 0) {
			str = br.readLine();
			if (str.matches("^-?[0-9]+$")) {
				value = Integer.parseInt(str);
				if (value >= hand.size() || value < 0) {
					System.out.println("Not enough cards, plz pick a card, here is your hand : ");
					print_hand();
				}
				else
					break;
			}
			else {
				System.out.println("Enter a number you dumbass");
				System.out.println("Pick a card in your hand player " + player_number);
			}
		}

		PlayingCard card_picked = hand.get(value);
		//hand.remove(value);
		return card_picked;
	}

	public void print_hand() {

		System.out.print("*");
		for (int i = 0; i < hand.size(); ++i)
			System.out.print("***");
		System.out.println("");
		for (int i = 0; i < hand.size(); ++i)
			if (hand.get(i).getValue() < 10)
				System.out.print("*0" + hand.get(i).getValue());
			else
				System.out.print("*" + hand.get(i).getValue());
		System.out.println("");

		System.out.print("*");
		for (int i = 0; i < hand.size(); ++i)
			System.out.print("***");
		System.out.println("");
	}

	public void print_state() {
		System.out.println("*****************");
		System.out.println("* " + nickname);
		System.out.println("*Life : " + life);
		System.out.println("*Score : " + score);
		System.out.println("*Innondation : " + innondation_card.getValue());

		print_hand();

		System.out.println("");

	}
	
	public void print_state_no_hand() {
		System.out.println("*****************");
		System.out.println("* " + nickname);
		System.out.println("*Life : " + life);
		System.out.println("*Score : " + score);
		System.out.println("*Innondation : " + innondation_card.getValue());

		System.out.println("");

	}



	public Vector<PlayingCard> getOriginal_hand() {
		return original_hand;
	}
	public void setOriginal_hand(Vector<PlayingCard> original_hand) {
		this.original_hand = original_hand;
	}
	public boolean isIsalive() {
		return isalive;
	}
	public void setIsalive(boolean isalive) {
		this.isalive = isalive;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public Vector<PlayingCard> getHand() {
		return hand;
	}
	public void setHand(Vector<PlayingCard> hand) {
		this.hand = hand;
	}
	public int getLife() {
		return life;
	}
	public void setLife(int life) {
		this.life = life;
	}
	public int getPlayer_number() {
		return player_number;
	}
	public void setPlayer_number(int player_number) {
		this.player_number = player_number;
	}
	public InnondationCard getInnondation_card() {
		return innondation_card;
	}
	public void setInnondation_card(InnondationCard innondation_card) {
		this.innondation_card = innondation_card;
	}
	public PlayingCard getCard_pickeddd() {
		return card_pickeddd;
	}
	public void setCard_pickeddd(PlayingCard card_pickeddd) {
		this.card_pickeddd = card_pickeddd;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}





	private String nickname;
	private boolean isalive;
	private int score;
	private PlayingCard card_pickeddd;
	private InnondationCard innondation_card;
	private Vector<PlayingCard> hand;
	private Vector<PlayingCard> original_hand;
	private int life;
	private int player_number;
}
