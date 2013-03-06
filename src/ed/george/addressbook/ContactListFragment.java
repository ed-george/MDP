package ed.george.addressbook;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ContactListFragment extends Fragment {

	View v;
	Activity a;
	private int personindex = 0;
	private boolean safetycatch = false;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		setHasOptionsMenu(true);


		v = inflater.inflate(R.layout.cont_list, container, false);

		return v;

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		a = activity;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		MainActivity ma = (MainActivity) getActivity();

		final ArrayList<Contact> contacts = ma.getContactList();
		final ImageButton leftBut = (ImageButton) v.findViewById(R.id.but_left);
		final ImageButton rightBut = (ImageButton) v.findViewById(R.id.but_right);

		fillContent(contacts);



		leftBut.setEnabled(false);
		if (contacts.size() == 1){
			rightBut.setEnabled(false);
		}

		leftBut.setOnClickListener(new View.OnClickListener() {                       
			@Override
			public void onClick(View v) {

				Log.d("BUTTON", "LEFT PRESSED");
				Log.d("personidex", Integer.toString(personindex));
				if(personindex == 1){
					personindex--;
					fillContent(contacts);
					leftBut.setEnabled(false);
					rightBut.setEnabled(true);
				}else{
					personindex--;
					fillContent(contacts);
					rightBut.setEnabled(true);
				}

			}



		});


		rightBut.setOnClickListener(new View.OnClickListener() {                       
			@Override
			public void onClick(View v) {
				Log.d("BUTTON", "RIGHT PRESSED");
				Log.d("personidex", Integer.toString(personindex));
				personindex++;
				if(personindex == (contacts.size() - 1)){

					if(contacts.size() > 1){
						leftBut.setEnabled(true);
					}else{
						leftBut.setEnabled(false);
					}

					rightBut.setEnabled(false);
					fillContent(contacts);

				}else{
					fillContent(contacts);
					leftBut.setEnabled(true);
					rightBut.setEnabled(true);
				}

			}

		});


	}

	private void fillContent(ArrayList<Contact> contacts) {
		TextView name = (TextView) v.findViewById(R.id.list_first_name);
		TextView number = (TextView) v.findViewById(R.id.list_number);
		TextView email = (TextView) v.findViewById(R.id.list_email);
		TextView emailTitle = (TextView) v.findViewById(R.id.email_bold);
		if(contacts.size() > 0){
			Contact contact = contacts.get(personindex);

			name.setText(contact.getName());
			number.setText(contact.getNumber());
			if(contact.getEmail().equals("null")){
				emailTitle.setEnabled(false);
				email.setEnabled(false);
				email.setText("No e-mail for this contact.");
			}else{
				emailTitle.setEnabled(true);
				email.setEnabled(true);
				email.setText(contact.getEmail());
			}
		}else{
			AlertDialog.Builder builder = new AlertDialog.Builder(a);
			safetycatch = true;
			
			builder.setMessage("You have no Contacts!").setPositiveButton("OK", dialogClickListenerTwo).show();
			
		}
	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which){
			case DialogInterface.BUTTON_POSITIVE:
				if(safetycatch){
					Toast.makeText(getActivity(), "Couldn't delete!", Toast.LENGTH_SHORT);
					break;
				}else{
				//Delete user and refresh page?!
				MainActivity ma = (MainActivity) getActivity();
				final ArrayList<Contact> contacts = ma.getContactList();
				String name = contacts.get(personindex).getName();
				DBHelper dbHelper = new DBHelper(a, "addressBook", null, 1);
				SQLiteDatabase db = dbHelper.getReadableDatabase();
				db.execSQL("DELETE FROM addressBook WHERE name='" + name +"';");
				ma.genContactList();
				Toast.makeText(getActivity(), name+ " was deleted!", Toast.LENGTH_LONG).show();
				dbHelper.close();
				db.close();
				newPage();
				}
				//dont forget to refrsh?!

				break;
			case DialogInterface.BUTTON_NEGATIVE:
				//Do nothing!
				break;

			}
		}

	};

	DialogInterface.OnClickListener dialogClickListenerTwo = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which){
			case DialogInterface.BUTTON_POSITIVE:
				AddFragment newfrag = new AddFragment();
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.container, newfrag);
				//Go back?
				//transaction.addToBackStack(null);
				transaction.commit();
		

				//dont forget to refrsh?!

				break;
			case DialogInterface.BUTTON_NEGATIVE:
				//Do nothing!
				break;

			}
		}

	};
	
	
	DialogInterface.OnClickListener callClickListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			MainActivity ma = (MainActivity) getActivity();
			final ArrayList<Contact> contacts = ma.getContactList();
			String number = contacts.get(personindex).getNumber();
			
			switch (which){
			case DialogInterface.BUTTON_POSITIVE:
				//call
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:" + number ));
				startActivity(callIntent);
				break;
				
			case DialogInterface.BUTTON_NEUTRAL:
				//message
				Intent msgIntent = new Intent(Intent.ACTION_VIEW);
				msgIntent.setData(Uri.parse("sms:" + number ));
				startActivity(msgIntent);
				break;
				
			case DialogInterface.BUTTON_NEGATIVE:
				//Do nothing!
				break;

			}
		}

	};

	

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.contactment, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(safetycatch){
			return false; //Not great practice but running out of patience!
		}
		
		switch (item.getItemId())
		{


		case R.id.delbutopt:
			AlertDialog.Builder builder = new AlertDialog.Builder(a);
			builder.setMessage("Delete this contact?").setPositiveButton("Yes", dialogClickListener)
			.setNegativeButton("No", dialogClickListener).show();
			return true;

		case R.id.callbutt:	
			AlertDialog.Builder builderII = new AlertDialog.Builder(a);
			builderII.setMessage("Call or Message Contact?").setPositiveButton("Call", callClickListener).setNeutralButton("Message", callClickListener)
			.setNegativeButton("Cancel", callClickListener).show();
			
			return true;
			
		default:
			//shouldnt matter but #YOLO
			return super.onOptionsItemSelected(item);

		}
	}


	void newPage(){
		// Create new fragment and transaction
		ContactListFragment newfrag = new ContactListFragment();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();

		transaction.replace(R.id.container, newfrag);
		//Go back?
		//transaction.addToBackStack(null);
		transaction.commit();
	}

}
