package fr.upem.captcha.ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import fr.upem.captcha.images.Images;
import fr.upem.captcha.images.bancs.Bancs;
import fr.upem.captcha.images.panneaux.Panneau;
import fr.upem.captcha.images.panneaux.ronds.PanneauRonds;
import fr.upem.captcha.images.ponts.Ponts;
import fr.upem.captcha.images.villes.Villes;

public class Init {
	Random random = new Random();
	Images correctCategory;
	String stringCategory;
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
	
	public String getCorrectStringCategory() {
		return stringCategory;
	}
	
	public void addCategories() {
		categorieNames.add("Ponts");
		categorieNames.add("Villes");
		categorieNames.add("Panneau");
		categorieNames.add("PanneauRonds");
		categorieNames.add("Bancs");
	}
	
	public void initGrid() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		this.addCategories();
		int randomNumber = this.random.nextInt(4);
		ArrayList<Images> categories = new ArrayList<Images>();
		
		//INIT ARRAY CATEGORIES
		categories.add(new Ponts());
		categories.add(new Villes());
		categories.add(new Panneau());
		categories.add(new PanneauRonds());
		categories.add(new Bancs());
		//categories.add((Images) Class.forName("PanneauRonds").newInstance());
		
		//RANDOM CATEGORY
		this.stringCategory = categorieNames.get(randomNumber);
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
	
	public void cleanInit() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		allImages.clear();
		correctImages.clear();
		this.initGrid();
	}
	
}
