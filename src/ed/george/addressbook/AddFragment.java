package ed.george.addressbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class AddFragment extends Fragment {

	View v;
	Activity a;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment



		v = inflater.inflate(R.layout.add, container, false);

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

		final EditText fn = (EditText) v.findViewById(R.id.first_name);
		final EditText nu = (EditText) v.findViewById(R.id.number);
		final EditText em = (EditText) v.findViewById(R.id.email);

		final ImageButton addBut = (ImageButton) v.findViewById(R.id.add_con);
		final ImageButton delBut = (ImageButton) v.findViewById(R.id.del_con);
		delBut.setEnabled(false); //default

		fn.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {
				if (fn.getText().toString().length() + nu.getText().toString().length() + em.getText().toString().length() > 0){
					delBut.setEnabled(true);
				}else{
					delBut.setEnabled(false);
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after){}
			public void onTextChanged(CharSequence s, int start, int before, int count){}
		}); 
		
		nu.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {
				if (fn.getText().toString().length() + nu.getText().toString().length() + em.getText().toString().length() > 0){
					delBut.setEnabled(true);
				}else{
					delBut.setEnabled(false);
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after){}
			public void onTextChanged(CharSequence s, int start, int before, int count){}
		}); 
		
		em.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {
				if (fn.getText().toString().length() + nu.getText().toString().length() + em.getText().toString().length() > 0){
					delBut.setEnabled(true);
				}else{
					delBut.setEnabled(false);
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after){}
			public void onTextChanged(CharSequence s, int start, int before, int count){}
		}); 


		addBut.setOnClickListener(new View.OnClickListener() {                       
			@Override
			public void onClick(View v) {

				if (fn.getText().toString().equals("") || nu.getText().toString().equals("")){
					AlertDialog.Builder builder = new AlertDialog.Builder(a);
					builder.setMessage("Required fields not complete!").setPositiveButton("OK", dialogClickListener).show();
					Log.d("BUTTON", "ONE OR MORE EMPTY");

				}else{

					String email;
					if (em.getText().toString().equals("")){
						email = null;
					}else{
						email = em.getText().toString();
						email = email.replaceAll(";", "").replaceAll("'", "");
					
						//Prevent SQL injection!?
					}

					//Surprise Muddatrucker. It's Databasing time!
					
					String n = fn.getText().toString();
					n = n.replaceAll(";", "").replaceAll("'", "").replaceAll("-", "");
					//again, protect sql injection
					
					DBHelper dbhelper = new DBHelper(a, "addressBook", null, 1);
					SQLiteDatabase db = dbhelper.getWritableDatabase();
					db.execSQL("INSERT INTO addressBook (name,number,email) " +
							"VALUES " +
							"('" + n  +  "', '" + nu.getText().toString() + 
							"','"+ email +"');");
					MainActivity x = (MainActivity) getActivity();
					x.genContactList(); //refresh arraylist
					Toast.makeText(getActivity(), "Contact Added!", Toast.LENGTH_SHORT).show();
					newPage();
					
					//Shut the door on your way out! KTHX
					db.close();
					dbhelper.close();

				}


				Log.d("BUTTON", "ADD PRESSED");
			}

		});



		delBut.setOnClickListener(new View.OnClickListener() {                       
			@Override
			public void onClick(View v) {
				//This works
				AlertDialog.Builder builder = new AlertDialog.Builder(a);
				builder.setMessage("Delete Changes?").setPositiveButton("Cancel", dialogClickListener).setNegativeButton("OK", dialogClickListener).show();
				Log.d("BUTTON", "DEL PRESSED");
			}

		});


	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which){
			case DialogInterface.BUTTON_POSITIVE:

				break;
			case DialogInterface.BUTTON_NEGATIVE:
				newPage();
				break;

			}
		}



	};

	void newPage(){
		// Create new fragment and transaction
		AddFragment newfrag = new AddFragment();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();

		transaction.replace(R.id.container, newfrag);
		//Could allow you to go back?
		//transaction.addToBackStack(null);
		transaction.commit();
	}
	
}
