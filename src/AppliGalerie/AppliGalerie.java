
package AppliGalerie;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * AppliGalerie contient tous les panels necessaires � la Galerie
 * 
 * @author Valentine
 * @author Audrey
 *
 */
public class AppliGalerie extends JPanel
{
	//Declaration du label qui contient le titre
	private JPanel labelTitre;
	private JLabel titre;
	//Dossier qui contient les images de la galerie
	private File folder = new File("./ImagesGalerie/");
	//Tableau qui contient les Panels du cardLayout
	String []listContent = {"Card1", "Card2", "Card3"};
	
	//CardLayout de la galerie
	private CardLayout clGalerie = new CardLayout();
	
	//Cr�ation du panel container du CardLayout
	private JPanel container = new JPanel();
	//D�claration de la variable qui sert � renommer les images temporairement
	private boolean temp = false;
	//Cr�ation de la variable grille centre
	private GrilleCentre gc;
	//Cr�ation de la variable scrollbar
	private JScrollPane scroll;
	//Cr�ation du tableau qui contient la liste des photos
	private File[] listPhotos;
	private JLabel[] imagesList;
	
	//fileChooser pour ajouteur une photo
	private JFileChooser fileChooser = new JFileChooser();
	
	private int nbrePhotos;
	private int id;
	
	//Ajout du bouton 
	private JButton addImg;

	/**
	 * Contructeur de la class AppliGalerie
	 */
	public AppliGalerie()
	{			
		//Ajout du Layout en BorderLayout
		this.setLayout(new BorderLayout());
		//Cr�ation du Jpanel qui contiendra le titre et le bouton ajout
		labelTitre = new JPanel();
		//JLabel qui contient le titre
		titre = new JLabel("Galerie");
		//Cr�ation du bouton
		addImg = new JButton("+");
		//Choix du layout = GridLayout
		labelTitre.setLayout(new GridLayout(1,3));
		//Ajout du bouton + titre au GridLayout au nord
		labelTitre.add(addImg);
		labelTitre.add(titre);
		//Ajout du JLabel vide
		labelTitre.add(new JLabel());
		//Ajout du labelTitre au nord
		this.add(labelTitre, BorderLayout.NORTH);
		//Choix de la police d'�criture
		Font policeNormal = new Font("Arial", 30, 30);
		//Application du Font au label titre Galerie + bouton add
		titre.setFont(policeNormal);
		addImg.setFont(new Font("Arial", 30, 30));
		//Alignement du titre de la galerie au centre
		titre.setHorizontalAlignment((int) CENTER_ALIGNMENT);
		
		//Ajouter action sur le bouton addImg
		addImg.addActionListener(new AjouterImage());
		
		//Choix du layout pour le jpanel container
		container.setLayout(clGalerie);
		//Instanciation de la GrilleCentre
		gc = new GrilleCentre();
		//Instanciation de la scrollbar
		scroll = new JScrollPane(gc);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		//Ajout de la scrollbar au panel
		container.add(scroll, listContent[0]);
		//Ajout du panel container au borderLayout centre
		this.add(container,BorderLayout.CENTER);
	}	
/**
 * AjouterImage permet de selectionner une image depuis l'ordinateur et de l'ajouter dans notre galerie
 * @author Audrey
 * @author Valentine
 *
 */
class AjouterImage implements ActionListener
{
/**
 * ActionPerformed - Action sur le bouton addImg
 */
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		//Choix des extensions accept�e
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "jpeg", "png");
		fileChooser.setFileFilter(filter);
		fileChooser.setMultiSelectionEnabled(true);
		
		//Ouverture de la fen�tre pour choisir la nouvelle photo
		int reponse = fileChooser.showOpenDialog(null);
		if (reponse == fileChooser.APPROVE_OPTION) 
		{

			//Reprend la photo selectionner
			File[] fs = fileChooser.getSelectedFiles();
			//Dossier de destination
			String location = "./ImagesGalerie/";
			
			for (int i = 0; i < fs.length; i++) 
			{
				//Path et Source
				String path = location + fs[i].getName();
				Path source = fs[i].toPath();
	
				//Si l'extension est valable
				if (checkExtension(fs[i]) == true) 
				{
					//R�cup�re le nom de la photo
					String choosedFile = fs[i].getName().substring(0, fs[i].getName().lastIndexOf("."));
					EnregistrerGalerie(fs[i],choosedFile);
				}
				
				else 
				{
					break;
				}
			}

		}
		
		//Si on annule 
		if (reponse == fileChooser.CANCEL_OPTION)
		{
			fileChooser.cancelSelection();
			return;
		}
		
		//Renomme les fichiers apr�s ajout de l'image
		RenommerFichier rf = new RenommerFichier();		
	}
	
	/**
	 * EnregistrerGalerie copie l'image selection� dans le dossier de destination
	 * @param img
	 * @param choosedFile
	 */
	private void EnregistrerGalerie(File img, String choosedFile)
	{			
		try
		{			
			BufferedImage bi = ImageIO.read(img) ;
			File outputfile = new File("./ImagesGalerie/" + choosedFile);
			ImageIO.write(bi, getExtension(img), outputfile);
		}
		
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}

/**
 * getFileExtension prend l'extension de l'image selectionn�e
 * @param file
 * @return
 */
private String getFileExtension(File file) 
{
	String fileName = file.getName();
	
	if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	else
		return "";
}

/**
 * checkExtension contr�le si lextension est accept�e ou pas
 * @param fichier
 * @return
 */
private boolean checkExtension(File fichier) 
{
	String ext = getFileExtension(fichier);
	
	if (ext.toLowerCase().equals("jpeg") || ext.toLowerCase().equals("jpg") || ext.toLowerCase().equals("png"))
		return true;
	else
		return false;
}
	
/**
 * GrilleCentre remplit la grille qui contient toutes les photos du dossier
 * @author Valentine
 * @author Audrey
 *
 */
class GrilleCentre extends JPanel
{
	//D�claration de l'array list qui contient le path des images
	private ArrayList path = new ArrayList();
	//Dossier qui contient le path du dossier des images
	private File folder = new File("./ImagesGalerie/");
	
	/**
	 * Constructeur de la classe GrilleCentre
	 */
	public GrilleCentre()
	{	
		//Compte le nbre de photos qui se trouve dans le dossier galerie
		listPhotos = folder.listFiles();
		nbrePhotos = listPhotos.length;
		
		//Renommer les fichiers au d�part
		RenommerFichier rd = new RenommerFichier();

		//Cr�ation GridLayout qui contiendra les photos
		this.setLayout(new GridLayout(0,3,5,5));
		
		//Remplissage tableau d�faut
		for(int i = 0; i < nbrePhotos; i++)
		{
			id = i+1;
			path.add("ImagesGalerie/" + id + ".jpg");
		}
		
		//Ajoute tableau au gridLayout
		for(int i = 0; i != path.size(); i++)
		{
			int id = i+1;
			String path = "ImagesGalerie/" + id + ".jpg";
			ImageBouton temp = new ImageBouton(path);
			this.add(temp);
			temp.addActionListener(new ClickImage(id, nbrePhotos));
		}
	}
	
	public void test() {
		for(int i = 0; i != path.size(); i++)
		{
			int id = i+1;
			String path = "ImagesGalerie/" + id + ".jpg";
			ImageBouton temp = new ImageBouton(path);
			this.add(temp);
			temp.addActionListener(new ClickImage(id, nbrePhotos));
		}
	}
}

/**
 * AfficheImage permet d'afficher une image de la grille en grand
 * @author Valentine
 * @author Audrey
 *
 */
class AfficheImage extends JPanel
{
	//Variable qui d�finit l'id de la photo
	private int id = 0;
	//Panel qui contient la photo en agrandis
	private JPanel photoPane = new JPanel();
	//Variable qui d�finit le nbre de photos dans la grille
	private int nbrePhotos = 0;
	
	//D�claration des variables servant �afficher l'image
	private ImageIcon icon;
	private JLabel labelImage;
	private BufferedImage img = null;
	private Image dimg;
	
	//Cr�ation des diff�rents boutons pour le panel qui affiche les images
	private ImageBouton next = new ImageBouton("next.png");
	private ImageBouton previous = new ImageBouton("precede.png");
	private ImageBouton listBack = new ImageBouton("listblack.png");
	private ImageBouton corbeille = new ImageBouton("poubelle.png");
	
	/**
	 * Contructeur de la class AfficheImage
	 * @param id
	 * @param nbrePhotos
	 */
	public AfficheImage(int id, int nbrePhotos)
	{
		this.id = id;
		this.nbrePhotos = nbrePhotos;
	}
	/**
	 * display est une fonction qui 
	 */
	void display()
	{
		//Met le layout � null pour placer les �l�ments comme on le souhaite
		photoPane.setLayout(null);
		//Ajout du panel
		container.add(photoPane, listContent[1]);
		
		//Path
		String path = "ImagesGalerie/" + id + ".jpg";
		
		//Try-catch qui trouve l'image avec son path
		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//D�finit la taille de l'image
		dimg = img.getScaledInstance(350,350,Image.SCALE_SMOOTH);
		//prend l'image en tant qu'ic�ne
		icon = new ImageIcon(dimg);
		//Ajoute l'icon au label image
		labelImage = new JLabel(icon);
		
		//placement des composants dans la fen�tre
		labelImage.setBounds(50, 50, 310,370);
		previous.setBounds(20,450, 40,40);
		next.setBounds(340,450, 40,40);
		listBack.setBounds(175,443, 50,50);
		corbeille.setBounds(330,0,40,40);
		
		//Ajout des composants dans la fen�tre
		photoPane.add(labelImage);
		photoPane.add(next);
		photoPane.add(previous);
		photoPane.add(listBack);
		photoPane.add(corbeille);

		//Ajout des actions lorsque l'on clique dessus
		previous.addActionListener(new LectureBouton(id, 0, nbrePhotos));
		next.addActionListener(new LectureBouton(id, 1, nbrePhotos));
		listBack.addActionListener(new LectureBouton(id, 2, nbrePhotos));
		corbeille.addActionListener(new LectureBouton(id, 3, nbrePhotos));
		
		//Quand on est sur la premi�re photo, on enl�ve le bouton previous
		if(id == 1)
		{
			photoPane.remove(previous);
		}
		
		//Quand on est sur la derni�re photo, on enl�ve le bouton next
		if(id == nbrePhotos)
		{
			photoPane.remove(next);
		}

		//On montre le panel qui affiche l'image
		clGalerie.show(container, listContent[1]);
	}
}

/**
 * ClickImage est une action lorsque l'on clique sur une image pour l'agrandir
 * @author Audrey
 * @author Valentine
 *
 */
class ClickImage implements ActionListener
{	
	
	int id = 0;
	int nbrePhotos = 0;
	
	/**
	 * Constructeur de ClickImage
	 * @param id
	 * @param nbrePhotos
	 */
	public ClickImage(int id, int nbrePhotos)
	{
		this.id = id;
		this.nbrePhotos = nbrePhotos;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		//Instanciation de la variable
		AfficheImage ai = new AfficheImage(id, nbrePhotos);
		//Appel de la m�thode AfficheImage
		ai.display();
	}	
}

/**
 * getExtension r�cup�re l'extension d'un fichier
 * @param fichier
 * @return
 */
private String getExtension(File fichier)
{
	String imgExtension = fichier.getName();

	//prend le nom du fichier
	String fileName = fichier.getName();
	
	//Prend l'extension
	if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
		return imgExtension.substring(imgExtension.lastIndexOf(".") + 1);
	else
		return "";
}

class LectureBouton implements ActionListener
{	
	int id = 0;
	int i = 0;
	int nbrePhotos = 0;
	
	public LectureBouton(int id, int i, int nbrePhotos)
	{
		this.i = i;
		this.id = id;
		this.nbrePhotos = nbrePhotos;
	}
	
	
	public void actionPerformed(ActionEvent e) 
	{
		if(i == 0)	//Bouton previous
		{	
			id = id-1;
			AfficheImage previous = new AfficheImage(id, nbrePhotos);
			previous.display();
		}
		
		if(i == 1)	//Bouton next
		{
			id++;
			AfficheImage next = new AfficheImage(id, nbrePhotos);
			next.display();
		}
		
		if(i == 2)	//Bouton retour sur la grille de photo
		{
			
			clGalerie.show(container, listContent[0]);

		}
		
		if(i == 3)	//Corbeille
		{
			//delete le fichier et les renomment
			DeleteFile df = new DeleteFile(id);
			RenommerFichier rf = new RenommerFichier();
			
			//refresh
			gc.removeAll();
			gc.repaint();
			gc.revalidate();
			gc.updateUI();
			
			ChargementGrille();
			clGalerie.show(container, listContent[2]);
			
			revalidate();
			repaint();
			
		}
	}	
}
/**
 * ChargementGrille recr�e la grille avec le refresh des photos
 */
private void ChargementGrille()
{
	gc = new GrilleCentre();
	scroll = new JScrollPane(gc);
	scroll.getVerticalScrollBar().setUnitIncrement(16);
	container.add(scroll, listContent[2]);
	add(container, BorderLayout.CENTER);
}

/**
 * DeleteFile supprime une photo de la galerie + du dossier
 * @author Valentine
 * @author Audrey
 */
class DeleteFile
{
	int id = 0;	
	
	public DeleteFile(int id)
	{
		this.id = id;
		//Path de la photo a supprimer
		String path = "ImagesGalerie/" + id + ".jpg";
		
	    try
	    {
	         File file = new File(path);

	         if(file.delete())
	         {
	        	 //Message qui indique que la photo est supprim�e
	        	 System.out.println(file.getName() + " est supprim�.");
	         }
	         
	         else
	         {
	        	 //Message qui indique que la suppression a �chou�e
	        	 System.out.println("Op�ration de suppression echou�e");
	         }
	    }
	    
	    catch(Exception e)
	    {
	         e.printStackTrace();
	    }
	}
}
/**
 * RenommerFichier renomme les fichiers apr�s supression ou ajout de photo
 * @author Valentine
 * @author Audrey
 */
class RenommerFichier
{
	public RenommerFichier()
	{
		
		File dossier = new File("ImagesGalerie/");
		File[] tousLesFichiers = dossier.listFiles();
		
		
		if(temp == false)
		{
			int y = 0;
			
			//Renomme les fichiers de tous le dossier temporairement pour �viter la conclusion
			for(int i = 1; i <= tousLesFichiers.length; i++)
			{
				File ancien = new File(tousLesFichiers[y].getPath());
				File tempName = new File("ImagesGalerie/" + i + "_.jpg");
				ancien.renameTo(tempName);
				y++;
			}
			
			//Met la variable a true
			temp = true;
			RenommerFichier rf = new RenommerFichier();	
		}
		
		
		//Renommer les fichiers d�finitivement et correctement
		else
		{
			int y = 0;
			
			for(int i = 1; i <= tousLesFichiers.length; i++)
			{
				File ancien = new File(tousLesFichiers[y].getPath());
				File nouveau = new File("ImagesGalerie/" + i + ".jpg");
				ancien.renameTo(nouveau);
				y++;
			}
			
			temp = false;
		}
	}
}
/**
 * ImageBouton est une fonction qui cr�e les boutons et ensuite les peinds avec nos images
 * @author Valentine
 * @author Audrey
 */

class ImageBouton extends JButton
{
	String path = "";
	Border bordureVide = BorderFactory.createEmptyBorder();
	
	public ImageBouton(String path)		//param�tre
	{	
		this.setOpaque(false);
		this.path = path;
		this.setPreferredSize(new Dimension(120,120));
		this.setBorder(bordureVide);
	}
	
	/**
	 * paintComponent peinds les boutons avec les images
	 */
	public void paintComponent(Graphics g)
	{
		try 
	    {
		      Image img = ImageIO.read(new File(this.path));
		      g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	    } catch (IOException e)
		
		{
	    	System.out.println("ERROR");
	    }
	}
}

}