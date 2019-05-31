/**
 * @authors : Audrey COMBE - Nina DE CASTRO
 * @date : 31 mai 2019
 * @file : Cateogry.java
 * @package : fr.upem.captcha.ui
 */

package fr.upem.captcha.images;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public abstract class Category implements Images{
	private List<URL> images;
	
	public Category() {
		this.images = getPhotos();
	}
	
	public List<URL> getImages() {
		return this.images;
	}
	
	public List<URL> getPhotos(){
		List<URL> images = new ArrayList<URL>();
		
		String packageName = "bin/"+this.getClass().getPackage().getName();
		String currentPath = packageName.replace('.', '/');
		
		Path currentRelativePath = Paths.get(currentPath);
		
		//Recup�ration des sous dossiers
		List <String> directories = null;
		try {
			directories = Files.walk(currentRelativePath, 1)
					.map(Path::getFileName)
					.map(Path::toString)
					.filter(n -> !n.contains("."))
					.collect(Collectors.toList());
			directories.remove(0); // on enl�ve le dossier courant (0)
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//R�cup�ration des images de chaque sous-dossiers
		for(String directorieItem : directories) {
			Path childPath = Paths.get(currentPath + "/" + directorieItem);
			
			
			List<String> imagesFromCategory = null;
			try {
				imagesFromCategory = Files.walk(childPath, 1)
						.map(Path::getFileName)
						.map(Path::toString)
						.filter(n -> n.contains(".jpg") || n.contains(".png") || n.contains(".jpeg"))
						.collect(Collectors.toList());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			for(String image : imagesFromCategory) {
				images.add(this.getClass().getResource(directorieItem+"/"+image));

			}
		}
		
		//si pas de sous-dossier (on r�cup�re les images du dossier courant)
		if(directories.isEmpty()) {
			List<String> imagesFromCategory = null;
			try {
				imagesFromCategory = Files.walk(currentRelativePath,  1)
						.map(Path::getFileName)
						.map(Path::toString)
						.filter(n -> n.contains(".jpg") || n.contains(".png") || n.contains(".jpeg"))
						.collect(Collectors.toList());
			}catch (IOException e) {
				e.printStackTrace();
			}
			for(String image : imagesFromCategory) {
				images.add(this.getClass().getResource(image));
			}
		}
		this.images = images;
		return images;
	}
	
	@Override
	public List<URL> getRandomPhotosURL(int max) {
		if(this.images.isEmpty()) {
			getPhotos();
		}
		
		List<URL> randomUrlList = new ArrayList<URL>();
		URL url;
		
		if(images.size() == 0) {
			throw new IllegalArgumentException("Pas de photo pour cette cat�gorie");
		}
		else if(max > images.size()) {
			throw new IllegalArgumentException("La valeur doit �tre inf�rieure au nombre d'images de cette cat�gorie : "+images.size());
			
		}
		
		for(int i = 0; i < max; i++) {
			do {
				url = getRandomPhotoURL();
			} while (randomUrlList.contains(url));
			randomUrlList.add(url);
		}
		
		return randomUrlList;
	}
	

	@Override
	public URL getRandomPhotoURL() {
		List<URL> urlList = this.images;
		Random random = new Random();
		int randomNumber = random.nextInt(this.images.size());
		URL url = urlList.get(randomNumber);
		return url;
	}

	
	public boolean isPhotoCorrect(URL url) {
		String packageName = this.getClass().getPackage().getName();
		return url.toString().replace('/', '.').contains(packageName);
	}
}
