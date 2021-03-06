package AppliContact;

import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/**
 * Cette classe repr�sente le panel contenant les boutons @BoutonSuivant. 
 * 
 * @author Audrey Viriot 
 * @author Valentine Cotter 
 */

public class BoutonDefilementListe extends JPanel 
{
	//Les boutons du panel 
	private BoutonSuivant btnPrecede = new BoutonSuivant("precede.png");
	private BoutonSuivant btnNext = new BoutonSuivant("next.png");
	//Acc�s aux diff�rentes pages de contact 
	private CardLayout cl2;
	private JPanel cardsListe;

	/**
	 * Constructeur de @BoutonDefilementListe. 
	 * 
	 * @param cl2, CardLayout de la liste contact 
	 * @param cardsList, toute les pages de liste de contact 
	 */
	public BoutonDefilementListe(CardLayout cl2, JPanel cardsList) 
	{

		btnPrecede.addMouseListener(new DefilerRight());
		btnNext.addMouseListener(new DefilerLeft());

		this.cl2 = cl2;
		this.cardsListe = cardsList;
		this.setLayout(new GridLayout(1, 2));
		this.add(btnPrecede);
		this.add(btnNext);

	}
	
	/**
	 * Affiche la liste de contact suivante au clic sur le bouton 
	 * 
	 * @author Audrey Viriot
	 * @author Valentine Cotter 
	 */
	class DefilerRight extends MouseAdapter 
	{
		@Override
		public void mouseClicked(MouseEvent arg0) 
		{

			cl2.next(cardsListe);
			
		}
	}

	/**
	 * Affiche le liste de contact pr�c�dente au clic sur le bouton 
	 * 
	 * 
	 * @author Audrey Viriot 
	 * @author Valentine Cotter 
	 */
	class DefilerLeft extends MouseAdapter 
	{
		@Override
		public void mouseClicked(MouseEvent arg0) 
		{

			cl2.previous(cardsListe);

		}

	}

}
