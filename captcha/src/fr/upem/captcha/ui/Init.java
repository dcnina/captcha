package fr.upem.captcha.ui;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import fr.upem.captcha.images.Images;
import fr.upem.captcha.images.bancs.Bancs;
import fr.upem.captcha.images.panneaux.Panneaux;
import fr.upem.captcha.images.panneaux.ronds.PanneauRonds;
import fr.upem.captcha.images.ponts.Ponts;
import fr.upem.captcha.images.villes.Villes;

public class Init {
	private Images correctCategory;
	private String stringCategory;
	private ArrayList<URL> correctImages = new ArrayList<URL>();
	private ArrayList<URL> allImages = new ArrayList<URL>();
	private ArrayList<String> categorieNames = new ArrayList<String>();
	private int maxDifficulty = 1;
	
	public Init()  {
		//intialisation avec catégorie images
		this.categorieNames.add("images");
		this.initGrid();
	}
	
	public ArrayList<URL> getCorrectImages() {
		return correctImages;
	}

	public ArrayList<URL> getAllImages() {
		return allImages;
	}

	public ArrayList<String> getCategorieNames() {
		return categorieNames;
	}

	public Images getCorrectCategory() {
		return correctCategory;
	}
	
	public String getCorrectStringCategory() {
		return stringCategory;
	}
	
	
	public void initGrid()  {

		ArrayList<Images> categories = null;
		try {
			categories = getCategories(categorieNames);	// On récupére les différentes classes
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		System.out.println("categories init ligne 64 "+categories);
		
		//INIT ARRAY CATEGORIES
		try {
			correctCategory = getRandomCategory(categories, categorieNames);// On récupére une classe au hasard
			stringCategory = correctCategory.getClass().getSimpleName();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		
		//FILL CORRECT IMAGES
		Random random = new Random();
		
		int randomNumber = random.nextInt(4)+1;

		correctImages = (ArrayList<URL>) correctCategory.getRandomPhotosURL(randomNumber);
		
		//RANDOM OTHER IMAGES
		ArrayList<URL> otherImages = getRandomOtherImages(categories, correctImages.size());
		//FILL ALL ARRAY
		allImages.addAll(correctImages);
		allImages.addAll(otherImages);
		Collections.shuffle(allImages);
	}
	
	public static Images getRandomCategory(ArrayList<Images> categories, ArrayList<String> categorieNames) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		Random randomGenerator = new Random();

		Images category = categories.get(randomGenerator.nextInt(categories.size()));	// on choisit aléatoirement une classe dans la liste

		// On met la 1ere lettre en minuscule et on rajoute la categorie dans la liste des categories
		String categoryName = category.getClass().getSimpleName();
		categorieNames.add(categoryName);	// on rajoute la nouvelle categorie dans la liste

		return category;	// on renvoit la categorie
	}
	
	public ArrayList<URL> getRandomOtherImages(ArrayList<Images> categories, int numberImages){
		categories.remove(correctCategory);
		ArrayList<URL> tmpRandomUrl = new ArrayList<URL>();
		Random random = new Random();
		for(int i = numberImages; i < 9; i++) {
			URL url;
			do {
				
				int randomNumber = random.nextInt(categories.size());	
				url = categories.get(randomNumber).getRandomPhotoURL();
			} while (tmpRandomUrl.contains(url) || url == null);	

			tmpRandomUrl.add(url); 
			
		}
		return tmpRandomUrl;
	}
	
	private static String getCurrentPath(ArrayList<String> categorieNames) {
		StringBuilder fullPath = new StringBuilder("bin/fr/upem/captcha");	// Chemin pour exécuter en ligne de commandes depuis le dossier bin
		for(String categorie: categorieNames) {
			fullPath.append("/").append(categorie);
		}
		return fullPath.toString();
	}
	
	private static ArrayList<String> getClassPath(ArrayList<String> categorieNames, List<String> categories) {
		StringBuilder fullPath = new StringBuilder("fr.upem.captcha");
		for(String categorie: categorieNames) {
			fullPath.append(".").append(categorie);
		}
		ArrayList<String> classPath = new ArrayList<String>();
		for(String categorie: categories) {
			String className = categorie.substring(0, 1).toUpperCase() + categorie.substring(1);
			String fullName = fullPath+"."+categorie+"."+className;
			//String fullName = fullPath+"."+categorie;
			classPath.add(fullName);
		}
		
		return classPath;
	}
	
	public static Images instantiateImages(Class<?> category) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Class<?> cls = Class.forName(category.getTypeName());	// On récupére le type de la classe
		@SuppressWarnings("deprecation")
		Object clsInstance = cls.newInstance();	// On instancie un objet du type de la classe
		return (Images)clsInstance;	// On le cast en Images pour pouvoir utiliser les méthodes de l'interface
	}
	
	
	public static ArrayList<Images> getCategories(ArrayList<String> categorieNames) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		//Récupération du dossier courant
		String currentPath = getCurrentPath(categorieNames);
		Path currentRelativePath = Paths.get(currentPath);
		//récupération des sous dossiers (catégories)
		
		List<String> directories = null;
		
		try {
			directories = Files.walk(currentRelativePath, 1)
			        .map(Path::getFileName)
			        .map(Path::toString)
			        .filter(n -> !n.contains("."))
			        .collect(Collectors.toList());
			directories.remove(0);	// On enléve le 0 car c'est le nom du dossier courant
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		//récupération des noms des classes
		ArrayList<String> classPath = getClassPath(categorieNames, directories);
		
		for(String s : classPath) {
			classes.add(Class.forName(s));
		}
		
		//instanciation en objet image des classes
		ArrayList<Images> categories = new ArrayList<Images>();

		for(Class<?> c : classes) {
			categories.add(instantiateImages(c));
		}
		return categories;
	}
	
	public void cleanInit() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		categorieNames.clear();
		categorieNames.add("images");
		allImages.clear();
		correctImages.clear();
		this.initGrid();

	}
	
}
