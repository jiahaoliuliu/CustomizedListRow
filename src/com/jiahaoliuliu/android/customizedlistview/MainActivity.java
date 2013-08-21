package com.jiahaoliuliu.android.customizedlistview;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

public class MainActivity extends Activity {
    
	private static final String LOG_TAG = "CustomListView";
	private ListView listView;
	private List<Integer> checkedElements = new ArrayList<Integer>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.list_view_layout);
		
		final ArrayAdapter<String> myArrayAdapter
			= new ArrayAdapter<String>(this, R.layout.list_view_row, R.id.checkedTextView, GENRES);

		listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(myArrayAdapter);
        listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position,
					long id) {
				CheckedTextView checkedTextView =
						(CheckedTextView)view.findViewById(R.id.checkedTextView);
				// Save the actual selected row data
				boolean checked = checkedTextView.isChecked();
				int choiceMode = listView.getChoiceMode();
				switch (choiceMode) {
				// Not choosing anything
				case (ListView.CHOICE_MODE_NONE):
					// Clear all selected data
					clearSelection();
					//printCheckedElements();
					break;
				// Single choice
				case (ListView.CHOICE_MODE_SINGLE):
					// Clear all the selected data
					// Revert the actual row data
					clearSelection();
					toggle(checked, checkedTextView, position);
					//printCheckedElements();
					break;
				// Multiple choice
				case (ListView.CHOICE_MODE_MULTIPLE):
				case (ListView.CHOICE_MODE_MULTIPLE_MODAL):
					// Revert the actual selected row data
					toggle(checked, checkedTextView, position);
					//printCheckedElements();
					break;
				}
			}
		});
    }

    private void toggle(boolean previouslyChecked, CheckedTextView checkedTextView, int position) {
		if (previouslyChecked) {
			checkedTextView.setChecked(false);
			int listPosition = checkedElements.indexOf(position);
			checkedElements.remove(listPosition);
		} else {
			checkedTextView.setChecked(true);
			checkedElements.add(position);
		}
    }

	/**
	 * Uncheck all the items
	 */
	private void clearSelection() {
		for (Integer position: checkedElements) {
			int firstPosition = listView.getFirstVisiblePosition() - listView.getHeaderViewsCount(); // This is the same as child #0
			int wantedChild = position - firstPosition;
			// Say, first visible position is 8, you want position 10, wantedChild will now be 2
			// So that means your view is child #2 in the ViewGroup:
			if (wantedChild < 0 || wantedChild >= listView.getChildCount()) {
			  Log.w(LOG_TAG, "Unable to get view for desired position, because it's not being displayed on screen.");
			  return;
			}
			// Could also check if wantedPosition is between listView.getFirstVisiblePosition() and listView.getLastVisiblePosition() instead.
			View wantedView = listView.getChildAt(wantedChild);
        	CheckedTextView checkedTextView = (CheckedTextView)wantedView.findViewById(R.id.checkedTextView);
        	if (checkedTextView.isChecked()) {
    			checkedTextView.setChecked(false);
    			int listPosition = checkedElements.indexOf(position);
    			checkedElements.remove(listPosition);
        	}
        }
	}

	private void printCheckedElements() {
		for (Integer position: checkedElements) {
			Log.v(LOG_TAG, String.valueOf(position));
		}
		Log.v(LOG_TAG, "\n");
	}

    private static final String[] GENRES = new String[] {
        "Action", "Adventure", "Animation", "Children", "Comedy", "Documentary", "Drama",
        "Foreign", "History", "Independent", "Romance", "Sci-Fi", "Television", "Thriller"
    };
}
