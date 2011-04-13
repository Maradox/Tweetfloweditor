
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

import java.io.Serializable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import at.tuwien.dsgproject.tfe.R;
import at.tuwien.dsgproject.tfe.entities.TweetFlow;
import at.tuwien.dsgproject.tfe.views.EditorView;
import at.tuwien.dsgproject.tfe.views.EditorView.EDITOR_STATE;
import at.tuwien.dsgproject.tfe.common.RasterGridHelper.SnapMode;

public class Editor extends ActionbarActivity {
	
	private EditorView mEditorView;
	private TweetFlow mTweetFlow;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        
        mTweetFlow = (savedInstanceState == null) ? null :
        		(TweetFlow) savedInstanceState.getSerializable(TweetFlow.TF_ID);
        if (mTweetFlow != null) {
        	mTweetFlow.setContext(this);
        } else {
        	mTweetFlow = new TweetFlow(this);
        }
        
        mEditorView = (EditorView) findViewById(R.id.editor_view);      
        registerForContextMenu(mEditorView);  
        mEditorView.setTweetFlow(mTweetFlow);
    }	
    
    
    
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(TweetFlow.TF_ID, mTweetFlow);
    }
    
    
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editor_menu, menu);

        return true;
    }
    
    public boolean onPrepareOptionsMenu(Menu menu) {   	
    	if(mTweetFlow.somethingSelected()) {
    		menu.findItem(R.id.deselect).setVisible(true);
    	} else {
    		menu.findItem(R.id.deselect).setVisible(false);
    	}
    	
    	if(mEditorView.isRasterOn()) {  
    		menu.findItem(R.id.raster_add).setVisible(false);
    		menu.findItem(R.id.raster_remove).setVisible(true);
        }     	
    	else {	
    		menu.findItem(R.id.raster_add).setVisible(true);
    		menu.findItem(R.id.raster_remove).setVisible(false);
        }
        
    	if(mEditorView.getSnapMode() == SnapMode.NOTHING)
    		menu.findItem(R.id.snapping_nothing).setChecked(true);
    	else if(mEditorView.getSnapMode() == SnapMode.RASTER) 
        	menu.findItem(R.id.snapping_raster).setChecked(true);
        else if(mEditorView.getSnapMode() == SnapMode.GRID) 
        	menu.findItem(R.id.snapping_grid).setChecked(true);
        
		return true;
    }
        
    public boolean onOptionsItemSelected(MenuItem menuItem) {
    	super.onOptionsItemSelected(menuItem);
    	
    	switch (menuItem.getItemId()) {
    		case R.id.deselect:
    			mTweetFlow.deselectAll();
    			mEditorView.redraw();
    			break;
    		case R.id.raster_add:
    			mEditorView.setRasterOn(true);
    			mEditorView.redraw();
    			break;
    		case R.id.raster_remove:	
    			mEditorView.setRasterOn(false);
    			mEditorView.redraw();
    			break;
    		case R.id.snapping:
    			break;
    		case R.id.snapping_nothing:
    			menuItem.setChecked(true);
    			mEditorView.setSnapMode(SnapMode.NOTHING);
    			mEditorView.redraw();
    			break;
    		case R.id.snapping_raster:
    			menuItem.setChecked(true);
    			mEditorView.setSnapMode(SnapMode.RASTER);
    			mEditorView.redraw();
    			break;
    		case R.id.snapping_grid:
    			menuItem.setChecked(true);
    			mEditorView.setSnapMode(SnapMode.GRID);
    			mEditorView.redraw();
    			break;
    		case R.id.undo:
    			mEditorView.undo();
    			break;	
    		case R.id.redo:
    			mEditorView.redo();
    			break;		
    		case R.id.add_open_sequence:
    			mTweetFlow.addOpenSequence();
    			mEditorView.redraw();
    			break;		
    		case R.id.create_loop:
    			mEditorView.setState(EDITOR_STATE.CREATE_LOOP);
    			break;		
    			
    		default:	
    			Toast.makeText(this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
    	  
    	}
    	return true;  
    }	 
}
