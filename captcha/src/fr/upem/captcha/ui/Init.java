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
import fr.upem.captcha.images.panneaux.Panneau;
import fr.upem.captcha.images.panneaux.ronds.PanneauRonds;
import fr.upem.captcha.images.ponts.Ponts;
import fr.upem.captcha.images.villes.Villes;

public class Init {
	Random random = new Random();
	Images correctCategory;
	//String stringCategory;
	ArrayList<URL> correctImages = new ArrayList<URL>();
	ArrayList<URL> allImages = new ArrayList<URL>();
	ArrayList<String> categorieNames = new ArrayList<String>();
	int maxDifficulty = 1;
	
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
	
//	public String getCorrectStringCategory() {
//		return stringCategory;
//	}
	
//	public void addCategories() {
//		categorieNames.add("Ponts");
//		categorieNames.add("Villes");
//		categorieNames.add("Panneau");
//		categorieNames.add("PanneauRonds");
//		categorieNames.add("Bancs");
//	}
	public void Init() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		//intialisation avec catégorie images
		this.categorieNames.add("images");
		this.initGrid();
	}
	
	public void initGrid() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		//this.addCategories();
		
		int randomNumber = this.random.nextInt(4);
		ArrayList<Images> categories = getCategories(categorieNames);
		
		//INIT ARRAY CATEGORIES
//		categories.add(new Ponts());
//		categories.add(new Villes());
//		categories.add(new Panneau());
//		categories.add(new PanneauRonds());
//		categories.add(new Bancs());
		//categories.add((Images) Class.forName("PanneauRonds").newInstance());
		
		//RANDOM CATEGORY
		//this.stringCategory = categorieNames.get(randomNumber);
		this.correctCategory = categories.get(randomNumber);
		
		//FILL CORRECT IMAGES
		randomNumber = this.random.nextInt(4)+1;
		correctImages = (ArrayList<URL>) correctCategory.getRandomPhotosURL(randomNumber);
		
		//RANDOM OTHER IMAGES
		ArrayList<URL> otherImages = getRandomOtherImages(categories, correctImages.size());
		System.out.println("TAB OTHER IMAGES: " + otherImages.toString());
		//FILL ALL ARRAY
		allImages.addAll(this.correctImages);
		allImages.addAll(otherImages);
		Collections.shuffle(allImages);
	}
	
	public ArrayList<URL> getRandomOtherImages(ArrayList<Images> categories, int numberImages){
		categories.remove(correctCategory);
		ArrayList<URL> tmpRandomUrl = new ArrayList<URL>();
		int randomNumber;
		
		for(int i = numberImages; i < 9; i++) {
			URL url;
			do {
				randomNumber = this.random.nextInt(categories.size());	
				url = categories.get(randomNumber).getRandomPhotoURL();
			} while (tmpRandomUrl.contains(url));	

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
			String fullName = fullPath+"."+categorie;
			classPath.add(fullName);
		}
		return classPath;
	}
	
	public static Images instantiateImages(Class<?> category) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Class<?> cls = Class.forName(category.getTypeName());	// On récupére le type de la classe
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//récupération des noms des classes
		ArrayList<String> classPath = getClassPath(categorieNames, directories);
		for(String s : classPath) {
			classes.add(Class.forName(s));
		}
		
		//instanciation en objet image des classes
		ArrayList<Images> categories = new ArrayList<Images>();
		for(Class c : classes) {
			categories.add(instantiateImages(c));
		}
		
		return categories;
	}
	
	public void cleanInit() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		allImages.clear();
		correctImages.clear();
		this.initGrid();
	}
	
}
