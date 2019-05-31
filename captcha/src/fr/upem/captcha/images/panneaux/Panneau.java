package fr.upem.captcha.images.panneaux;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.upem.captcha.images.Category;
import fr.upem.captcha.images.Images;
import fr.upem.captcha.images.panneaux.ronds.PanneauRonds;

public class Panneau extends Category {
	private final File directory = new File("src/fr/upem/captcha/images/panneaux");
	
	public Panneau() {
		super();
	}

//	@Override
//	public List<URL> getPhotos() {
//		List<URL> urlList = new ArrayList<URL>();
//		File[] f = directory.listFiles();
//	    for (File file : f) {
//	        if (file != null && (file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".jpeg"))) {
//				try {
//					URL url = file.toURL();
//					urlList.add(url);
//				} 
//				catch (MalformedURLException e) {
//				    
//				}	
//	        }
//	    }
//	    if (urlList.size() > 0)
//            return urlList;
//        else
//            return null;
//	}

	@Override
	public List<URL> getRandomPhotosURL(int max) {
		
		List<URL> randomUrlList = new ArrayList<URL>();
		for(int i = 0; i < max; i++) {
			randomUrlList.add(getRandomPhotoURL());
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

	@Override
	public boolean isPhotoCorrect(URL url) {
		String filename = Paths.get(url.getPath()).getFileName().toString();
		if (Panneau.class.getResource(filename) != null)
			return true;
		return false;
	}
}