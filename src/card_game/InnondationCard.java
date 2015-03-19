package card_game;

public class InnondationCard extends Card {

	public InnondationCard(int value) {
		super(value);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compareTo(Card o) {
		if (this.getValue() == o.getValue())
			return 0;
		else if (this.getValue() > o.getValue())
			return 1;
		else
			return -1;
	}

}
