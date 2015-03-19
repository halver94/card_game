package card_game;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

public class Game {

	public final static int SERVER = 0;
	public final static int CLIENT = 1;
	
	public Game(int number_of_players) {
		super();
		this.number_of_players = number_of_players;
		this.round_number = 0;

		// initialiation des joueurs
		this.players = new Vector<Player>();
		for (int i = 0; i < number_of_players; ++i) {
			Player player = new Player(i);
			this.players.add(player);
		}
		System.out.println("added " + players.size() + " players.");

		// initialiation du tas d'innondation
		this.innondation_cards = new Vector<InnondationCard>();
		for (int i = 1; i <= 12; ++i) {
			InnondationCard card = new InnondationCard(i);
			InnondationCard card2 = new InnondationCard(i);
			this.innondation_cards.add(card);
			this.innondation_cards.add(card2);
		}
		System.out.println("added " + innondation_cards.size() + " flooding cards.");

		// initialiation du tas de playingcard
		this.playing_cards = new Vector<PlayingCard>();
		PlayingCard card = null;

		for (int i = 1; i <= 60; ++i) {
			if (i <= 12 || i >= 49)
				card = new PlayingCard(i, 0);
			else if (i < 25 && i > 12)
				card = new PlayingCard(i, 1);
			else if (i < 49 && i > 36)
				card = new PlayingCard(i, 1);
			else
				card = new PlayingCard(i, 2);
			this.playing_cards.add(card);
		}
		System.out.println("added " + playing_cards.size() + " playing cards.");

	}

	@SuppressWarnings("unchecked")
	public void card_distribution() {

		int random_number = 0;
		int round = 0;

		while (round < 12) {
			for (int i = 0; i < number_of_players; ++i) {
				do {
					random_number = randInt(0, playing_cards.size() - 1);
				} while (random_number > playing_cards.size());

				if (playing_cards.size() > 0) {
					players.get(i).getHand().add(playing_cards.get(random_number));
					playing_cards.remove(random_number);
				}

			}
			++round;
		}

		for (int i = 0; i < number_of_players; ++i) {
			Collections.sort(players.get(i).getHand());
			players.get(i).setOriginal_hand((Vector<PlayingCard>)(players.get(i).getHand().clone()));			
		}

		System.out.println("Cards Distributed");
	}

	public void pick_inondation() {
		int random_number = 0;

		random_number = randInt(0, innondation_cards.size() - 1);
		current1 = innondation_cards.get(random_number);
		innondation_cards.remove(random_number);

		random_number = randInt(0, innondation_cards.size() - 1);
		current2 = innondation_cards.get(random_number);
		innondation_cards.remove(random_number);

		System.out.println("Here are the two flooding cards :");
		current1.draw_card();
		current2.draw_card();
	}

	public void play_turn() throws NumberFormatException, IOException {

		Map<Integer, PlayingCard> card_played = new HashMap<Integer, PlayingCard>();
		int higher_player = 0;
		int second_higher_player = 1;

		round_number++;

		for (int i = 0; i < number_of_players; ++i){
			if (players.get(i).isIsalive()){
				PlayingCard card_picked = players.get(i).getCard_pickeddd();
				
				for (int j = 0; j < players.get(i).getHand().size(); ++j) {
					if (players.get(i).getHand().get(j).getValue() == card_picked.getValue())
						players.get(i).getHand().remove(j);
				}
				
				card_played.put(i, card_picked);
				System.out.print("Card picked by " + players.get(i).getNickname() + " : ");
				card_picked.draw_card();
			}
		}

		nested_loop:
		for (int i = 0; i < number_of_players; ++i){
			if (players.get(i).isIsalive()) {
				higher_player = players.get(i).getPlayer_number();
				for (int j = i + 1; j < number_of_players; ++j){
					if (players.get(j).isIsalive()) {
						second_higher_player = players.get(j).getPlayer_number();
						break nested_loop;
					}
				}
			}
		}

		for (int i = 0; i < card_played.size(); ++i) {
			if (players.get(i).isIsalive()) {
				if (card_played.get(i).getValue() > card_played.get(higher_player).getValue()) {
					second_higher_player = higher_player;
					higher_player = i;
				}
				else if (card_played.get(i).getValue() > card_played.get(second_higher_player).getValue() && i != higher_player) {
					second_higher_player = i;
				}
			}
		}
		
		
		players.get(higher_player).setInnondation_card(current1);
		players.get(second_higher_player).setInnondation_card(current2);

		System.out.println("----Flooding----");
		System.out.println("Player " + higher_player + " took flooding lvl " + current1.getValue());
		System.out.println("Player " + second_higher_player + " took flooding lvl " + current2.getValue());
	}

	public void remove_hp() {

		int higher_innondation = 0;

		for (int i = 0; i < players.size(); ++i) {
			if (players.get(i).isIsalive()) {
				if (players.get(i).getInnondation_card().getValue() > higher_innondation)
					higher_innondation = players.get(i).getInnondation_card().getValue();
			}
		}

		for (int i = 0; i < players.size(); ++i) {
			if (players.get(i).isIsalive()) {
				if (players.get(i).getInnondation_card().getValue() == higher_innondation)
					if (players.get(i).getLife() == 0) {
						players.get(i).setIsalive(false);
						System.out.println("Player dead: " + players.get(i).getNickname());
					}
					else
						players.get(i).setLife(players.get(i).getLife() - 1);
			}
		}
	}

	public void give_hp() {

		int hp = 0;
		for (int i = 0; i < players.size(); ++i) {
			players.get(i).setLife(0);
			for (int j = 0; j < players.get(i).getHand().size(); ++j) {
				hp += players.get(i).getHand().get(j).getLife_given();
			}
			players.get(i).setLife(hp / 2);
			hp = 0;
		}
	}

	public void end_game() {
		
		System.out.println("***Here are the score you bitches***");
		Player player = players.get(0);
		for (int i = 0; i < players.size(); ++i) {
			if (players.get(i).getScore() > player.getScore())
				player = players.get(i);
			System.out.println(players.get(i).getNickname() + " got " + players.get(i).getScore() + " points.");
		}
		
		System.out.println("====GAME OVER====");
		System.out.println(player.getNickname() + " WON THE GAME !");
	}
	
	public boolean check_end_round() {

		int number_of_player_alive = 0;

		if (round_number < 12) {
			for (int i = 0; i < players.size(); ++i) {
				if (players.get(i).getLife() > 0)
					number_of_player_alive++;
				if (number_of_player_alive > 2)
					return false;
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public void switch_hand() {

		System.out.println("SWITCHING HAND, dude give your hand to the player at your left !");
		Vector<PlayingCard> tmp_hand = new Vector<PlayingCard>();

		tmp_hand = (Vector<PlayingCard>)players.get(0).getOriginal_hand().clone();

		for (int i = 0; i < players.size() - 1; ++i) {
			players.get(i).getHand().clear();
			players.get(i).setHand((Vector<PlayingCard>)players.get(i + 1).getOriginal_hand().clone());
			players.get(i).setOriginal_hand((Vector<PlayingCard>)players.get(i + 1).getOriginal_hand().clone());
			Collections.sort(players.get(i).getHand());
		}
		players.get(players.size() - 1).setHand((Vector<PlayingCard>)tmp_hand.clone());
		players.get(players.size() - 1).setOriginal_hand((Vector<PlayingCard>)tmp_hand.clone());
		Collections.sort(players.get(players.size() - 1).getHand());
	}

	public void calc_score() {
		for (int i = 0; i < players.size(); ++i) {
			players.get(i).setScore(players.get(i).getScore() + players.get(i).getLife());
		}
	}

	public void reinitialization() {
		
		//System.out.println("Reinitialisation inondation heap");
		
		this.innondation_cards = new Vector<InnondationCard>();
		for (int i = 1; i <= 12; ++i) {
			InnondationCard card = new InnondationCard(i);
			InnondationCard card2 = new InnondationCard(i);
			this.innondation_cards.add(card);
			this.innondation_cards.add(card2);
		}
		
		System.out.println("Jesus, all players are now alive !");
		for (int i = 0; i < players.size(); ++i) {
			players.get(i).setIsalive(true);
		players.get(i).setInnondation_card(new InnondationCard(0));
		}
		
		round_number = 0;
	}
	
	
	public void printstate() {
		for (int i = 0; i < players.size(); ++i)
			players.get(i).print_state();
	}

	public int randInt(int min, int max) {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	public int getNumber_of_players() {
		return number_of_players;
	}
	public Vector<Player> getPlayers() {
		return players;
	}
	public void setPlayers(Vector<Player> players) {
		this.players = players;
	}
	public Vector<InnondationCard> getInnondation_cards() {
		return innondation_cards;
	}
	public void setInnondation_cards(Vector<InnondationCard> innondation_cards) {
		this.innondation_cards = innondation_cards;
	}
	public Vector<PlayingCard> getPlaying_cards() {
		return playing_cards;
	}
	public void setPlaying_cards(Vector<PlayingCard> playing_cards) {
		this.playing_cards = playing_cards;
	}
	public InnondationCard getCurrent1() {
		return current1;
	}
	public void setCurrent1(InnondationCard current1) {
		this.current1 = current1;
	}
	public InnondationCard getCurrent2() {
		return current2;
	}
	public void setCurrent2(InnondationCard current2) {
		this.current2 = current2;
	}


	private int round_number;
	private InnondationCard current1;
	private InnondationCard current2;
	private int number_of_players;
	private Vector<Player> players;
	private Vector<InnondationCard> innondation_cards;
	private Vector<PlayingCard> playing_cards;


}

