package fr.upem.captcha.images.bancs;

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

public class Bancs extends Category {
	
	public Bancs() {
		super();
	}

	private final File directory = new File("./src/fr/upem/captcha/images/bancs");
	
	@Override
	public List<URL> getPhotos() {
		List<URL> urlList = new ArrayList<URL>();
		File[] f = directory.listFiles();
	    for (File file : f) {
	        if (file != null && (file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".jpeg"))) {
				try {
					URL url = file.toURL();
					urlList.add(url);
				} 
				catch (MalformedURLException e) {
				    
				}	
	        }
	    }
	    if (urlList.size() > 0)
            return urlList;
        else
            return null;
	}

	@Override
	public boolean isPhotoCorrect(URL url) {
		String filename = Paths.get(url.getPath()).getFileName().toString();
		if (Bancs.class.getResource(filename) != null)
			return true;
		return false;
	}
}