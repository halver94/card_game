package card_game;

public class PlayingCard extends Card {

	public PlayingCard(int value, int life_given) {
		super(value);
		// TODO Auto-generated constructor stub
		
		this.life_given = life_given;
	}

	
	
	public int getLife_given() {
		return life_given;
	}
	public void setLife_given(int life_given) {
		this.life_given = life_given;
	}


	private int life_given;


	@Override
	public int compareTo(Card arg0) {
		// TODO Auto-generated method stub
		if (this.getValue() == arg0.getValue())
			return 0;
		else if (this.getValue() > arg0.getValue())
			return 1;
		else
			return -1;
	}
}
