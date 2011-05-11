
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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import at.tuwien.dsgproject.tfe.R;
import at.tuwien.dsgproject.tfe.common.RasterGridHelper.SnapMode;
import at.tuwien.dsgproject.tfe.common.ActionbarHelper;
import at.tuwien.dsgproject.tfe.common.StorageHandler;
import at.tuwien.dsgproject.tfe.common.TweeterParser;
import at.tuwien.dsgproject.tfe.dialogs.SaveTweetflowDialog;
import at.tuwien.dsgproject.tfe.entities.TweetFlow;
import at.tuwien.dsgproject.tfe.views.EditorView;
import at.tuwien.dsgproject.tfe.views.EditorView.EDITOR_STATE;

public class Editor extends ActionbarActivity {
	
	public final static String OPEN_FILE = "open_file_path";
	public final static String OPEN_NEW = "open_new";
	
	public final static String OPEN_CURRENT_FILE = ".current";
	
	private EditorView mEditorView;
	private TweetFlow mTweetFlow;
	private StorageHandler mStorage;
	
	public String mFileName = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        
        mStorage = new StorageHandler(this);
        mFileName = null;
        
        mEditorView = (EditorView) findViewById(R.id.editor_view);      
        registerForContextMenu(mEditorView);  
        
        //check if the activity is being restarted and reopen the current Tweetflow
        if(savedInstanceState != null) {
        	if (savedInstanceState.getBoolean(OPEN_CURRENT_FILE, false)) {
        		Log.d("TFE-Editor","recover .current TF");
        		mFileName = OPEN_CURRENT_FILE;	  
        	}
        } else {
        	Intent i = getIntent();
        	getFilenameFromIntent(i);
        }
        //openTweetFlow();    
    }	
    
    private void getFilenameFromIntent(Intent i) {
    	if(i.getBooleanExtra(OPEN_CURRENT_FILE, false)) {
			mFileName = OPEN_CURRENT_FILE;
    	} else if(i.hasExtra(OPEN_FILE)) {
    		mFileName = i.getStringExtra(OPEN_FILE); 
        } else if(i.getBooleanExtra(OPEN_NEW, false)){
        	mFileName = null;
        } else {
        	Log.w("TFE-Editor", "invalid intent content!");
        }
    }
    
    private void openTweetFlow() {
     	if(mFileName != null) {
     		try {
				mTweetFlow = mStorage.openTweetflowFile(mFileName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(this, "Error opening Tweetflow, creating new Tweetflow", Toast.LENGTH_SHORT).show();
				mTweetFlow = new TweetFlow();
			}	
     	} else {
        	mTweetFlow = new TweetFlow();
        	Toast.makeText(this, "New Tweetflow", Toast.LENGTH_SHORT).show();
     	}
     	mTweetFlow.setContext(this);
     	mEditorView.setTweetFlow(mTweetFlow);
    	
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState); 
    	//saving Tweetflow to .current
    	mStorage.write(OPEN_CURRENT_FILE, mTweetFlow);
    	outState.putBoolean(OPEN_CURRENT_FILE, true);     
    }
    
	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		getFilenameFromIntent(intent);
	}
	
    protected void onResume() {
    	super.onResume();
    	openTweetFlow();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mStorage.write(OPEN_CURRENT_FILE, mTweetFlow);
    } 


    
    //TODO maybe add both dialogs to the activity
    public void saveTweetFlow(View v) {
    	final SaveTweetflowDialog dialog = new SaveTweetflowDialog(this, mTweetFlow);
    	dialog.show();
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
	    	case R.id.send_tweetflow:
				Toast.makeText(this, TweeterParser.parseTweetFlow(mTweetFlow), Toast.LENGTH_SHORT).show();
				break;
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
    		case R.id.add_open_sequence:
    			mTweetFlow.addOpenSequence();
    			mEditorView.redraw();
    			break;		
    		case R.id.create_bigloop:
    			mEditorView.setState(EDITOR_STATE.CREATE_LOOP);
    			break;
    		case R.id.save:
    			saveTweetFlow(mEditorView);
    			break;
    			
    		default:	
    			Toast.makeText(this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
    	  
    	}
    	return true;  
    }
    
}
