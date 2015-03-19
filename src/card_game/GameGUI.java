package card_game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GameGUI extends JPanel
{
JPanel topPanel = new JPanel();
JPanel dcardPanel = new JPanel();
JPanel pcardPanel = new JPanel();
JTextPane winlosebox = new JTextPane();
/*JButton hitbutton = new JButton();
JButton dealbutton = new JButton();
JButton staybutton = new JButton();
JButton playagainbutton = new JButton();
*/
JLabel dealerlabel = new JLabel();
JLabel playerlabel = new JLabel();

/*
Hand dealer = new Hand();    //to hold the dealer's cards
Hand player = new Hand();    //to hold the player's cards
Blackjack game = new Blackjack(dealer,player); 
*/
/*************************************************************
  the labels to represent the cards for the game
*************************************************************/
JLabel playercard1;
JLabel playercard2;
JLabel playercardhit;
JLabel dealercard0;
JLabel dealercard2;
JLabel dealercard1;
JLabel dealercardhit;

/*************************************************************
   Constructs the screen
*************************************************************/
public GameGUI () 
{
  
  topPanel.setBackground(new Color(0, 122, 0));
  dcardPanel.setBackground(new Color(0, 122, 0));
  pcardPanel.setBackground(new Color(0, 122, 0));
  
  topPanel.setLayout(new FlowLayout());
  winlosebox.setText(" ");
  winlosebox.setFont(new java.awt.Font("Helvetica Bold", 1, 20));
 /* dealbutton.setText("  Deal");
  dealbutton.addActionListener(new dealbutton());
  hitbutton.setText("  Hit");
  hitbutton.addActionListener(new hitbutton());
  hitbutton.setEnabled(false);
  staybutton.setText("  Stay");
  staybutton.addActionListener(new staybutton());   
  staybutton.setEnabled(false);
  playagainbutton.setText("  Play Again");
  playagainbutton.addActionListener(new playagainbutton());
  playagainbutton.setEnabled(false); 
 */ 
  dealerlabel.setText("  Dealer:  ");
  playerlabel.setText("  Player2:  ");

  topPanel.add(winlosebox);
  /*topPanel.add(dealbutton);
  topPanel.add(hitbutton);
  topPanel.add(staybutton);
  topPanel.add(playagainbutton);
 */ pcardPanel.add(playerlabel);
  dcardPanel.add(dealerlabel);
  
  setLayout(new BorderLayout());
  add(topPanel,BorderLayout.NORTH);
  add(dcardPanel,BorderLayout.CENTER);
  add(pcardPanel,BorderLayout.SOUTH);
  
}//end BlackjackGUI 

/*************************************************************
   Shows the screen
*************************************************************/
public void display() 
{
  JFrame myFrame = new JFrame("Inondation");
  myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  myFrame.setContentPane(this);
  myFrame.setPreferredSize(new Dimension(700,550));

  //Display the window.
  myFrame.pack();
  myFrame.setVisible(true);
  
}//end display

/*************************************************************
 DealButton
 @param e Deal button pressed
*************************************************************/
/*
class dealbutton implements ActionListener { 
public void actionPerformed(ActionEvent e) {

  dcardPanel.add(dealerlabel);
  pcardPanel.add(playerlabel);

 /*
    Get's dealer and player cards from Hand
     and the image associated with that random
     card and puts them on the screen.
 */
  /*
  dealercard0 = new JLabel(new ImageIcon("back.jpg"));

  game.dealInitialCards();

  //to iterate set and get current dealer cards
  Card dcard=null;
  Iterator<Card> dscan = (dealer.inHand).iterator();
  int count = 0;
  while (dscan.hasNext())
  {
      dcard = dscan.next();
      if(count==0)
        dealercard1 = new JLabel(dcard.getimage());
      else 
        dealercard2 = new JLabel(dcard.getimage());
        
      count++;
  }

  //to iterate set and get current player cards
  Iterator<Card> pscan = (player.inHand).iterator();
  count = 0;
  while (pscan.hasNext())
  {
     Card pcard = pscan.next();
     if(count==0)
       playercard1 = new JLabel(pcard.getimage());
     else
       playercard2 = new JLabel(pcard.getimage());
 
     count++;
  }

  dcardPanel.add(dealercard0);
  dcardPanel.add(dealercard2);

  pcardPanel.add(playercard1);
  pcardPanel.add(playercard2);
  
  dealerlabel.setText("  Dealer:  "+ dcard.getvalue());
  playerlabel.setText("  Player:  " + game.handValue(player));
  
  hitbutton.setEnabled(true);
  staybutton.setEnabled(true);
  dealbutton.setEnabled(false);

  if(game.blackj())     
   {
     hitbutton.setEnabled(false);
     staybutton.setEnabled(false);
     dealbutton.setEnabled(false);
     playagainbutton.setEnabled(true);
     winlosebox.setText("BlackJack");
   }

  add(dcardPanel,BorderLayout.CENTER);
  add(pcardPanel,BorderLayout.SOUTH);

}
}//end dealbutton
*/
/*************************************************************
 HitButton
  every time the player wants another card
  until hand value is over 21.
 @param e Hit button pressed
*************************************************************/
/*
class hitbutton implements ActionListener { 
public void actionPerformed(ActionEvent e) {

  
  Card hitcard = game.hit(player);
  playercardhit = new JLabel(hitcard.getimage());
  pcardPanel.add(playercardhit);
  pcardPanel.repaint();

  if(game.bust(player))     
  {
    winlosebox.setText("Bust");
    hitbutton.setEnabled(false);
    dealbutton.setEnabled(false);
    staybutton.setEnabled(false);
    playagainbutton.setEnabled(true);
  }

  playerlabel.setText("  Player:   " + game.handValue(player));

}
}//end hitbutton
*/
/*************************************************************
 StayButton
  dealer must hit on 16 or lower. determines the winner, 
  player wins if under 21 and above dealer. 
  Tie goes to dealer.
 @param e Stay button pressed
*************************************************************/ /*
class staybutton implements ActionListener { 
public void actionPerformed(ActionEvent e) {
  
  dcardPanel.remove(dealercard0);
  dcardPanel.add(dealercard1);

  dealer = game.dealerPlays();
  dcardPanel.removeAll();
  dcardPanel.add(dealerlabel);
  dealerlabel.setText(" " + dealerlabel.getText());  
  
  //iterate through cards and re-display
  Card dhitcard = null;
  Iterator<Card> scan = (dealer.inHand).iterator();
  while (scan.hasNext())
  {
      dhitcard = scan.next();
      dealercardhit = new JLabel(dhitcard.getimage());
      dcardPanel.add(dealercardhit);
  }
  
  dealerlabel.setText("Dealer: " + game.handValue(dealer));
  playerlabel.setText("Player: " + game.handValue(player));

  winlosebox.setText(game.winner());
  hitbutton.setEnabled(false);
  staybutton.setEnabled(false);
  
  playagainbutton.setEnabled(true);

}
}//end staybutton
*/
/*************************************************************
 PlayAgainButton
  resets screen
 @param e Play Again button pressed
*************************************************************//*
class playagainbutton implements ActionListener { 
public void actionPerformed(ActionEvent e) {

  dealerlabel.setText("Dealer: ");
  playerlabel.setText("Player: ");
  winlosebox.setText(""); 
  dealer = new Hand();
  player = new Hand();
  game=new Blackjack(dealer, player);

  dcardPanel.removeAll();
  pcardPanel.removeAll();

  hitbutton.setEnabled(false);
  staybutton.setEnabled(false);
  playagainbutton.setEnabled(false);
  dealbutton.setEnabled(true);

}
}//end playagainbutton
*/
}//end BlackjackGUI
