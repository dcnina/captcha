package fr.upem.captcha.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import fr.upem.captcha.images.Category;
import fr.upem.captcha.images.Images;
import fr.upem.captcha.images.panneaux.Panneau;
import fr.upem.captcha.images.panneaux.ronds.PanneauRonds;
import fr.upem.captcha.images.ponts.Ponts;
import fr.upem.captcha.images.villes.Villes;

public class MainUi {
	final static int VILLES = 0;
	final static int PONTS = 1;
	final static int PANNEAUX = 2;
	final static int PANNEAUXRONDS = 3;
	private static ArrayList<URL> selectedImages = new ArrayList<URL>();
	private static int type;
	private static int numberImages = 0;
	private static String typeName;
	private static Init init = new Init();

	public static void main(String[] args) throws IOException, URISyntaxException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		JFrame frame = new JFrame("Captcha"); // Création de la fenêtre principale

		GridLayout layout = createLayout();  // Création d'un layout de type Grille avec 4 lignes et 3 colonnes

		frame.setLayout(layout);  // affection du layout dans la fenêtre.
		frame.setSize(1024, 768); // définition de la taille
		frame.setResizable(false);  // On définit la fenêtre comme non redimentionnable

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Lorsque l'on ferme la fenêtre on quitte le programme.


		JButton okButton = createOkButton();


		/// TYPE ALÉATOIRE
		
		/*switch(randomNumber) {
		case VILLES:
			type = VILLES; typeName = "Villes";
			typeRandomUrl = (ArrayList<URL>)villes.getRandomPhotosURL(random.nextInt(4) + 1); break;
		case PONTS:
			type = PONTS; typeName = "Ponts";
			typeRandomUrl = (ArrayList<URL>)ponts.getRandomPhotosURL(random.nextInt(4) + 1); break;
		case PANNEAUX:
			type = PANNEAUX; typeName = "Panneaux";
			typeRandomUrl = (ArrayList<URL>)panneaux.getRandomPhotosURL(random.nextInt(4) + 1); break;
		case PANNEAUXRONDS:
			type = PANNEAUXRONDS; typeName = "Panneaux ronds";
			typeRandomUrl = (ArrayList<URL>)panneauRonds.getRandomPhotosURL(random.nextInt(4) + 1); break;
		}

		numberImages += typeRandomUrl.size();
		System.out.println("NOUS AVONS UN TYPE : " + type + " et une taille : " + numberImages);
		
		//REMPLISSAGE DE TYPERANDOMURL
		ArrayList<URL> tmpRandomUrl = new ArrayList<URL>();
		for(int i = numberImages; i < 9; i++) {
			do{
				randomNumber = random.nextInt(4);
			}while(randomNumber == type); 
			
			switch(randomNumber){
			case VILLES: 
				tmpRandomUrl = (ArrayList<URL>)villes.getRandomPhotosURL(1); break;
			case PONTS:
				tmpRandomUrl = (ArrayList<URL>)ponts.getRandomPhotosURL(1); break;
			case PANNEAUX:
				tmpRandomUrl = (ArrayList<URL>)panneaux.getRandomPhotosURL(1); break;
			case PANNEAUXRONDS:
				tmpRandomUrl = (ArrayList<URL>)panneauRonds.getRandomPhotosURL(1); break;
			}
			numberImages += tmpRandomUrl.size();
			typeRandomUrl.addAll(tmpRandomUrl);
		}

		Collections.shuffle(typeRandomUrl);	//mélange de la liste
		*/
		// Ajout des images dans la frame
		
		init.initGrid();
		
		System.out.println("Catégories: " + init.getCategorieNames().toString());
		System.out.println("Bonne CAT: " + init.getCorrectCategory().toString());
		System.out.println("Tableaux d'images: " + init.getCorrectImages().toString());
		System.out.println();
		
		for(int j = 0; j < 9; j++)
			frame.add(createLabelImage(init.getAllImages().get(j)));
		

		frame.add(new JLabel("Trouvez toutes les images qui sont des : \n" + typeName));
		frame.add(okButton);
		frame.setVisible(true);
	}


	private static GridLayout createLayout(){
		return new GridLayout(4,3);
	}

	private static JButton createOkButton(){
		return new JButton(new AbstractAction("Vérifier") { //ajouter l'action du bouton

			@Override
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(new Runnable() { // faire des choses dans l'interface donc appeler cela dans la queue des évènements

					@Override
					public void run() { // c'est un runnable
						if (checkPhotosIsCorrect(init.getCorrectImages(), init.getCorrectCategory())) {
							System.out.println("C'est OK");
						}
						else {
							System.out.println("PAS OK");
						}
					}
				});
			}
		});
	}

	private static JLabel createLabelImage(URL imageLocation) throws IOException{

		final URL url = imageLocation; //Aller chercher les images !! IMPORTANT 

		BufferedImage img = ImageIO.read(url); //lire l'image
		Image sImage = img.getScaledInstance(1024/3,768/4, Image.SCALE_SMOOTH); //redimentionner l'image

		final JLabel label = new JLabel(new ImageIcon(sImage)); // créer le composant pour ajouter l'image dans la fenêtre

		label.addMouseListener(new MouseListener() { //Ajouter le listener d'évenement de souris
			private boolean isSelected = false;


			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {

			}

			@Override
			public void mouseExited(MouseEvent arg0) {

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {

			}

			@Override
			public void mouseClicked(MouseEvent arg0) { //ce qui nous intéresse c'est lorsqu'on clique sur une image, il y a donc des choses à faire ici
				EventQueue.invokeLater(new Runnable() { 

					@Override
					public void run() {
						if(!isSelected){
							label.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
							isSelected = true;
							selectedImages.add(url);
						}
						else {
							label.setBorder(BorderFactory.createEmptyBorder());
							isSelected = false;
							selectedImages.remove(url);
						}

					}
				});

			}
		});

		return label;
	}
	
	private static boolean checkPhotosIsCorrect(ArrayList<URL> correctImages, Images category) {
		if (selectedImages.containsAll(correctImages)) {
			for (URL url: selectedImages) {
				if (!category.isPhotoCorrect(url))
					return false;
			}
			return true;
		}
		else{
			return false;
		}
	}
}
