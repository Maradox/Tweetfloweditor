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

import java.io.File;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import at.tuwien.dsgproject.tfe.R;
import at.tuwien.dsgproject.tfe.common.StorageHandler;
import at.tuwien.dsgproject.tfe.common.UserManagement;


/**
 * Home
 * 
 * @author Matthias Neumayr
 * @author Martin Perebner
 * 
 * the main activity launched at program start; i provides a 
 * listview to open/delete saved Tweetflows
 */
public class Home extends ActionbarActivity {
    /** Called when the activity is first created. */
	
	protected StorageHandler mStorage;
	private ArrayList<File> mFileList;
	protected FileListAdapter mFileListAdapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        SharedPreferences settings = getSharedPreferences("TFE", MODE_PRIVATE);
        String requestToken = settings.getString("requestToken", "");
        String secretToken = settings.getString("secretToken", "");
        
        if((requestToken.length() > 0) && (secretToken.length() > 0)) {
        	UserManagement.getInstance().loginAuto(requestToken,secretToken);
        }
 
        mStorage = new StorageHandler(this);
        mFileList = new ArrayList<File>();  
        mFileListAdapter = new FileListAdapter(this, mFileList);
        updateFileList();
        prepareListView();
    }	
    
	@Override
	protected void onResume() {
		updateFileList();
		super.onResume();
	}

	public void openEditorCurrent(View view) {
		final Context c = view.getContext();
		Intent i = new Intent(c, Editor.class );	
		i.putExtra(Editor.OPEN_CURRENT_FILE, true);
		c.startActivity(i); 
	}
	
	protected void openEditorFile(File file) {
    	Intent i = new Intent(this, Editor.class );	
    	i.putExtra(Editor.OPEN_FILE, file.getName());
    	Home.this.startActivity(i); 
	}

    private void updateFileList() {
    	mFileList.clear();
    	mStorage.checkStorageState();
    	if(mStorage.isReadable()) {
	    	for(File f : mStorage.listFiles()) {
				if(!f.isHidden() && !f.isDirectory()) {
					mFileList.add(f);
				}	
			}
    	} else {
    		Toast.makeText(this, "Cannot read from external Storage!", Toast.LENGTH_LONG);
    	}
    	mFileListAdapter.notifyDataSetChanged();
    }
	
    private void prepareListView() {
        ListView lv = (ListView) findViewById(R.id.home_file_list);
        
		// Set the view to be shown if the list is empty
		LayoutInflater inflator = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View emptyView = inflator.inflate(R.layout.filelist_empty, null);
		((ViewGroup)lv.getParent()).addView(emptyView);
		lv.setEmptyView(emptyView);
	
        lv.setAdapter(mFileListAdapter);
        
        lv.setOnItemClickListener(new OnItemClickListener() {
        
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	final File file = (File)parent.getItemAtPosition(position);
            	openEditorFile(file);
            }
         });
        
        lv.setOnItemLongClickListener(new OnItemLongClickListener() {
        	
        	 public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        		 final File file = (File)parent.getItemAtPosition(position);
        		 final CharSequence[] items = {"Open","Delete"};

        		 AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        		 builder.setTitle("Edit "+file.getName()+":");
        		 builder.setItems(items, new DialogInterface.OnClickListener() {
        		     public void onClick(DialogInterface dialog, int item) {
        		         switch(item) {
        		         case 0:
        		        	 openEditorFile(file);
        		        	 break;
        		         case 1:
        		        	 file.delete();
        		        	 updateFileList();
        		        	 break;
        		         default:
        		        	 Log.w("TFE", "invalid file dialog item");
        		         }
        		     }
        		 });
        		 builder.show();
        		 return true;
        	 }
        		
		});
    }
    
    
    
	private class FileListAdapter extends ArrayAdapter<File> {
		
		private ArrayList<File> mObjects;
		
		public FileListAdapter(Context context, ArrayList<File> list) {
			super(context, R.layout.filelist_item, R.id.filelist_item_text, list);
			mObjects = list;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View row = null;
			
			if(convertView == null) { 
				LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.filelist_item, parent, false);
			} else {
				row = convertView;
			}

			File file = mObjects.get(position);
			TextView textView = (TextView)row.findViewById(R.id.filelist_item_text);
			if(file != null)
				textView.setText(file.getName());
			
			return row;
		}

	}
}