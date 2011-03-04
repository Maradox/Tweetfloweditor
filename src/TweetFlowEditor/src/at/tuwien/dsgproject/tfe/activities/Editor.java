
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

package at.tuwien.dsgproject.tfe.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import at.tuwien.dsgproject.tfe.R;
import at.tuwien.dsgproject.tfe.views.EditorView;

public class Editor extends ActionbarActivity {
	
	EditorView editorView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor); 
        
        editorView = (EditorView) findViewById(R.id.editor_view);
    }	
    
 
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
         
        MenuItem menuItem;
        
        menuItem = menu.add("Redo");
        menuItem.setIcon(R.drawable.editor_menue_undo);
          
        menuItem = menu.add("Undo");
        menuItem.setIcon(R.drawable.editor_menue_undo);
        
        menuItem = menu.add("Deselect");
        menuItem.setIcon(R.drawable.editor_menue_deselect);
        
        menuItem = menu.add("Save");
        menuItem.setIcon(R.drawable.editor_menue_save);
        
        menuItem = menu.add("Open");
        menuItem.setIcon(R.drawable.editor_menue_save);
        
		return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem menuItem) {
    	if (menuItem.hasSubMenu() == false) {
    		if(menuItem.getTitle().equals("Deselect")) {
    			editorView.delesectAll();
    			editorView.redraw();
    		}
    		
    		else
    			Toast.makeText(this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
    	}
    	return true;  
    }	
}
