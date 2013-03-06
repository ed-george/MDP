package ed.george.addressbook;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private DBHelper dbHelper;
	private ArrayList <Contact> conts = new ArrayList<Contact>();


	public ArrayList<Contact> getContactList(){
		return conts;

	}

	public void setContactList(ArrayList<Contact> conts){
		this.conts = conts; 

	}


	public void genContactList(){
		ArrayList <Contact> newcont = new ArrayList<Contact>();
		dbHelper = new DBHelper(this, "addressBook", null, 1);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.query("addressBook", new String[] { "id", "name", "number", "email" },null, null, null, null,"name");

		if(c.moveToFirst()){
			do{
				Contact con = new Contact(c.getString(1),c.getString(2),c.getString(3));	
				newcont.add(con);
			}
			while(c.moveToNext());
		}
		setContactList(newcont);
		dbHelper.close();
		db.close();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		genContactList();

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// For each of the sections in the app, add a tab to the action bar.
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section1).setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section2).setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section3).setTabListener(this));

	}


	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
				getActionBar().getSelectedNavigationIndex());
	}





	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

		/**
		 * On first tab we will show our list
		 */
		if (tab.getPosition() == 0) {
			AddressFragment simpleListFragment = new AddressFragment();
			getSupportFragmentManager().beginTransaction().replace(R.id.container, simpleListFragment).commit();
		}
		else if (tab.getPosition() == 1) {
			AddFragment test = new AddFragment();
			getSupportFragmentManager().beginTransaction().replace(R.id.container, test).commit();
		} 
		else if (tab.getPosition() == 2) {
			ContactListFragment clf = new ContactListFragment();
			getSupportFragmentManager().beginTransaction().replace(R.id.container, clf).commit();
		} 
		
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}


}
