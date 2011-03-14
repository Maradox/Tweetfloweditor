
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import at.tuwien.dsgproject.tfe.R;
import at.tuwien.dsgproject.tfe.views.EditorView;
import at.tuwien.dsgproject.tfe.views.EditorView.SnapMode;
import at.tuwien.dsgproject.tfe.views.EditorView.TouchMode;

public class Editor extends ActionbarActivity {
	
	private EditorView editorView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor); 
        
        editorView = (EditorView) findViewById(R.id.editor_view);
    }	
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editor_menu, menu);

        return true;
    }
    
    public boolean onPrepareOptionsMenu(Menu menu) {   	
    	if(editorView.somethingSelected()) {
    		menu.findItem(R.id.deselect).setVisible(true);
    	} else {
    		menu.findItem(R.id.deselect).setVisible(false);
    	}
    	
    	if(editorView.isRasterOn()) {  
    		menu.findItem(R.id.raster_add).setVisible(false);
    		menu.findItem(R.id.raster_remove).setVisible(true);
        }     	
    	else {	
    		menu.findItem(R.id.raster_add).setVisible(true);
    		menu.findItem(R.id.raster_remove).setVisible(false);
        }
        
    	if(editorView.getSnapMode() == SnapMode.NOTHING)
    		menu.findItem(R.id.snapping_nothing).setChecked(true);
    	else if(editorView.getSnapMode() == SnapMode.RASTER) 
        	menu.findItem(R.id.snapping_raster).setChecked(true);
        else if(editorView.getSnapMode() == SnapMode.GRID) 
        	menu.findItem(R.id.snapping_grid).setChecked(true);
        
		return true;
    }
        
    public boolean onOptionsItemSelected(MenuItem menuItem) {
    	super.onOptionsItemSelected(menuItem);
    	
    	switch (menuItem.getItemId()) {
    		case R.id.deselect:
    			editorView.delesectAll();
    			editorView.redraw();
    			break;
    		case R.id.raster_add:
    			editorView.setRasterOn(true);
    			editorView.redraw();
    			break;
    		case R.id.raster_remove:	
    			editorView.setRasterOn(false);
    			editorView.redraw();
    			break;
    		case R.id.snapping:
    			break;
    		case R.id.snapping_nothing:
    			menuItem.setChecked(true);
    			editorView.setSnapMode(SnapMode.NOTHING);
    			editorView.redraw();
    			break;
    		case R.id.snapping_raster:
    			menuItem.setChecked(true);
    			editorView.setSnapMode(SnapMode.RASTER);
    			editorView.redraw();
    			break;
    		case R.id.snapping_grid:
    			menuItem.setChecked(true);
    			editorView.setSnapMode(SnapMode.GRID);
    			editorView.redraw();
    			break;
    		case R.id.undo:
    			editorView.undo();
    			break;	
    		case R.id.redo:
    			editorView.redo();
    			break;		
    		case R.id.container:
    			editorView.setmCurrMode(TouchMode.CONTAINER_DOWN);
    			break;		
    			
    		default:	
    			Toast.makeText(this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
    	  
    	}
    	return true;  
    }	
}
