package ed.george.addressbook;


import java.util.ArrayList;

import ed.george.addressbook.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class AddressFragment extends ListFragment {

	ArrayList<Contact> listcontacts;
	Activity a;
	String call;
	public AddressFragment() {

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		a = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//		dbHelper.getWritableDatabase();


		MainActivity ma = (MainActivity) getActivity();
		listcontacts = ma.getContactList();
		String[] x = new String[listcontacts.size()];
		for(int i = 0; i < listcontacts.size(); i++){
			Contact contact = listcontacts.get(i);
			x[i] =  contact.getName();

		}


		ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, x);
		setListAdapter(listAdapter);



	}




	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_frag, container, false);
	}

	@Override
	public void onListItemClick(ListView list, View v, int position, long id) {

		Toast.makeText(getActivity(), Integer.toString(position), Toast.LENGTH_LONG).show();


		Contact x = listcontacts.get(position);
		call = x.getNumber();
		AlertDialog.Builder builder = new AlertDialog.Builder(a);
		builder.setMessage("Call " + x.getName() + "?").setPositiveButton("Yes", dialogClickListener)
		.setNegativeButton("No", dialogClickListener).show();


	}



	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which){
			case DialogInterface.BUTTON_POSITIVE:
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:" + call ));
				startActivity(callIntent);
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				//No button clicked
				break;
			}
		}



	};




}
