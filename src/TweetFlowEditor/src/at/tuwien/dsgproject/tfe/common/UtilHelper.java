package at.tuwien.dsgproject.tfe.common;

import java.io.InputStream;
import java.net.URL;
import android.graphics.drawable.Drawable;

public class UtilHelper {
	
	public static Drawable getImageFromUrl(URL url, String imageName) {
		try {
			InputStream inputStream = (InputStream) (url).getContent();
			Drawable drawable = Drawable.createFromStream(inputStream, imageName);
			return drawable;
		}catch (Exception e) {
			return null;
		}
	}
}
