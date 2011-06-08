
/* 
 *  Tweetfloweditor - a graphical editor to create Tweetflows
 *  
 *  Copyright (C) 2011  Matthias Neumayr
 *  Copyright (C) 2011  Martin Perebner
 *  
 *  Tweetfloweditor is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  Tweetfloweditor is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with Tweetfloweditor.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.tuwien.dsgproject.tfe.common;

import java.io.InputStream;
import java.net.URL;
import android.graphics.drawable.Drawable;

/**
 * UtilHelper
 * 
 * @author Matthias Neumayr
 * @author Martin Perebner
 * 
 * helpermethods that belong nowhere else
 */
public class UtilHelper {
	
	public static Drawable getImageFromUrl(URL url, String imageName) {
		try {
			InputStream inputStream = (InputStream) (url).getContent();
			Drawable drawable = Drawable.createFromStream(inputStream, imageName);
			return drawable;
		} catch (Exception e) {
			return null;
		}
	}
}
