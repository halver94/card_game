package card_game;

public abstract class Card implements Comparable<Card> {

	public Card(int value) {
		super();
		this.value = value;
	}

	public void draw_card() {
		System.out.println("*" + value + "*\n");
	}
	
	
	
	public int getValue() {
		return value;
	}

	private int value;

}
