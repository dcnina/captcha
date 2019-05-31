/**
 * @authors : Audrey COMBE - Nina DE CASTRO
 * @date : 31 mai 2019
 * @file : MainUi.java
 * @package : fr.upem.captcha.ui
 */


package fr.upem.captcha.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;


import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import fr.upem.captcha.images.Images;


public class MainUi {
	private static ArrayList<URL> selectedImages = new ArrayList<URL>();
	//private static ArrayList<URL> correctedImages = new ArrayList<URL>();
	private static Init init = new Init();

	public static void main(String[] args) throws IOException, URISyntaxException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		JFrame frame = new JFrame("Captcha");
		initCaptcha(frame);
	}
	
	public static void initCaptcha(JFrame frame) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {

		GridLayout layout = createLayout();  // Création d'un layout de type Grille avec 4 lignes et 3 colonnes

		frame.setLayout(layout);  // affection du layout dans la fenêtre.
		frame.setSize(1024, 768); // définition de la taille
		frame.setResizable(false);  // On définit la fenêtre comme non redimentionnable

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Lorsque l'on ferme la fenêtre on quitte le programme.


		JButton okButton = createOkButton(frame);	
		
		for(int j = 0; j < 9; j++)
			frame.add(createLabelImage(init.getAllImages().get(j)));
		
		frame.add(new JLabel("Trouvez: \n" + init.getCorrectStringCategory()));
		frame.add(okButton);
		frame.setVisible(true);

	}

	private static GridLayout createLayout(){
		return new GridLayout(4,3);
	}

	private static JButton createOkButton(final JFrame frame){
		return new JButton(new AbstractAction("V�rifier") { //ajouter l'action du bouton

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(new Runnable() { // faire des choses dans l'interface donc appeler cela dans la queue des �v�nements

					@Override
					public void run() { // c'est un runnable
						if (checkPhotosIsCorrect(init.getCorrectImages(), init.getCorrectCategory())) {
							System.out.println("C'est OK");
							displaySuccessMessage("F�licitations vous avez r�ussi !", frame);
						}
						else {
								try {
									displayErrorMessage("Vous avez fait erreur !");
									restartCaptcha(frame);
								} catch (InstantiationException | IllegalAccessException | ClassNotFoundException
										| IOException e) {
									e.printStackTrace();
								}
							
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
		if(selectedImages.isEmpty()) {
			return false;
		}
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
	
	public static void displayErrorMessage(String message) {
		JOptionPane.showMessageDialog(null, message);
	}
	
	public static void displaySuccessMessage(String message, final JFrame frame) {
		JOptionPane.showMessageDialog(null, message);
		frame.dispose();
	}
	
	public static void restartCaptcha(JFrame frame) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
		selectedImages.clear();
		frame.setVisible(false);
		init.cleanInit();
		frame = new JFrame("Captcha");
		initCaptcha(frame);
	}
}
