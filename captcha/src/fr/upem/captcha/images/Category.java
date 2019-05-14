package fr.upem.captcha.images;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Category implements Images{
	private List<URL> images;
	
	public Category() {
		this.images = new ArrayList<URL>();
	}
	
	public List<URL> getImages() {
		return this.images;
	}
	
	public abstract List<URL> getPhotos();
	
	@Override
	public List<URL> getRandomPhotosURL(int max) {
		
		List<URL> randomUrlList = new ArrayList<URL>();
		URL url;
		
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
		List<URL> urlList = getPhotos();
		Random random = new Random();
		int randomNumber = random.nextInt(urlList.size());
		URL url = urlList.get(randomNumber);
		return url;
	}

	
	public abstract boolean isPhotoCorrect(URL url);
}
