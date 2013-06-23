package com.sellaspot;

import java.util.ArrayList;

import android.app.Dialog;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sellaspot.datamodel.Item;
import com.sellaspot.datamodel.RESTData;
import com.sellaspot.datamodel.UserRating;
import com.sellaspot.db.DBSession;

public class SpotsFragment extends ListFragment implements OnQueryTextListener {
 
	private ArrayList<Item> spots = new ArrayList<Item>();;
	private SpotsAdapter itemAdapter;

	private String userId;
	private String sessionId;
	private String itemId;
	
	private static final String TAG = "SellerSpotsActivity";
	private SpotsFragment fragment;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
    	// Inflate the layout for this fragment
        return inflater.inflate(R.layout.seller_spots_layout, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    
    	super.onActivityCreated(savedInstanceState);
    	
    	setHasOptionsMenu(true);
    	
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	StrictMode.setThreadPolicy(policy);
    	
		fragment = this;
		
		//new LoadData().execute();
		
		spots = new ArrayList<Item>();
		this.itemAdapter = new SpotsAdapter(this.getActivity().getApplicationContext(), R.layout.list_row, spots);
		setListAdapter(this.itemAdapter);
		
	    final Spinner spinner4 = (Spinner) getActivity().findViewById(R.id.categorySpinner);
	    ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(
	    		getActivity().getApplicationContext(), R.array.filter_category_array, android.R.layout.simple_spinner_dropdown_item);
	    adapter4.setDropDownViewResource(R.layout.spinner_item);
	    spinner4.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
	    
	    spinner4.setAdapter(adapter4);
	    spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	        	String category = spinner4.getSelectedItem().toString();
	        	
				if(category.equalsIgnoreCase("concerts")) {
					
					spinner4.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#751ccf")));
				
				} else if(category.equalsIgnoreCase("restaurant")) {
				
					spinner4.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffc600")));
				} else if(category.equalsIgnoreCase("parking")) {
					
					spinner4.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0ba216")));
				
				} else if(category.equalsIgnoreCase("sports")) {
				
					spinner4.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2665a0")));
				
				} else if(category.equalsIgnoreCase("movies")) {
				
					spinner4.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff4e00")));

				} else if(category.equalsIgnoreCase("theme park")) {
					
					spinner4.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#a1ae0e")));

				} else if(category.equalsIgnoreCase("general")) {
				
					spinner4.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6db4fd")));

				} else {
					
				    spinner4.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));

				}

				
	        	if(category.equalsIgnoreCase("All Categories")) {
	        		
	        		new LoadData().execute();
	        	} else {
	        		new FilterData().execute(category);
	        	}
	        	
	        }
	        public void onNothingSelected(AdapterView<?> parent) {
	        }
	    });
		
//	    Button searchButton = (Button)getActivity().findViewById(R.id.searchButton);
//	    searchButton.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				EditText searchEditText = (EditText)getActivity().findViewById(R.id.searchEditText);
//				
//				if(searchEditText.getText().toString().isEmpty()) {
//					Toast.makeText(fragment.getActivity(), "Please enter search text", Toast.LENGTH_LONG).show();
//				} else {
//					System.out.println(searchEditText.getText().toString());
//					new SearchSpots().execute(searchEditText.getText().toString());
//				}
//				
//			}
//		});
	}

	private class SpotsAdapter extends ArrayAdapter<Item> {

		private ArrayList<Item> items;

		public SpotsAdapter(Context context, int textViewResourceId,
				ArrayList<Item> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View v = convertView;
			
			if (v == null) {
			
				LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.list_row, null);
			}
			
			Item item = items.get(position);
			
			if (item != null) {

				TextView tt = (TextView) v.findViewById(R.id.toptext);
				TextView bt = (TextView) v.findViewById(R.id.bottomtext);
				TextView lt = (TextView) v.findViewById(R.id.locationText);
				TextView bidsText = (TextView) v.findViewById(R.id.bidsText);
				TextView priceText = (TextView) v.findViewById(R.id.priceText);
				ImageView imgView = (ImageView)v.findViewById(R.id.icon);

				//Display icon based on category
				String category = item.getCategory();
				
				if(category.equalsIgnoreCase("concerts")) {
				
					imgView.setBackgroundResource(R.drawable.concerts);
				
				} else if(category.equalsIgnoreCase("restaurant")) {
				
					imgView.setBackgroundResource(R.drawable.restaurant);
				} else if(category.equalsIgnoreCase("parking")) {
				
					imgView.setBackgroundResource(R.drawable.parking);
				
				} else if(category.equalsIgnoreCase("sports")) {
				
					imgView.setBackgroundResource(R.drawable.sports);
				
				} else if(category.equalsIgnoreCase("movies")) {
				
					imgView.setBackgroundResource(R.drawable.movies);
				
				} else if(category.equalsIgnoreCase("theme park")) {
				
					imgView.setBackgroundResource(R.drawable.themepark);
				
				} else if(category.equalsIgnoreCase("general")) {
				
					imgView.setBackgroundResource(R.drawable.general);
				} else {
					imgView.setBackgroundResource(R.drawable.general);
				}
				
				if (tt != null) {
					
					if(item.getItemdescription().length() > 100) {
						tt.setText(item.getItemdescription().substring(0, 99) + "...");
					} else {
						tt.setText(item.getItemdescription());
					}
				}
				if (bt != null) {
					bt.setText(item.getVenuename());
				}
				if (lt != null) {
					lt.setText(item.getCity() + "," + item.getState());
				}
				if (bidsText != null) {
					bidsText.setText(item.getNumofbids() + " bids");
				}
				if(priceText!=null) {
					priceText.setText("$" + item.getPrice());
				}
			}
			return v;
		}
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {

    	final Dialog dialog = new Dialog(this.getActivity());
		dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
		
		dialog.setContentView(R.layout.spot_detail_dialog);
		
		dialog.setTitle("Bid For Spot!");
		
		//final TextView txtView = (TextView)dialog.findViewById(R.id.textView1);
		
		String itemId = String.valueOf(spots.get(position).getItemid());
		
		DBSession session = new DBSession(this.getActivity());
		
		this.itemId = itemId;
		this.userId = session.getUserId();
		this.sessionId = session.getSessionId();
		
		final Item item = RESTData.getSpotById(this.itemId, this.userId, this.sessionId);
		UserRating userRating = RESTData.getRating(this.userId, this.sessionId, item.getUserid());
		RatingBar ratingBar = (RatingBar)dialog.findViewById(R.id.userRatingBar);
		
		TextView getDirectionsTextView = (TextView)dialog.findViewById(R.id.getDirectionsTextView);
		
		getDirectionsTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
				Uri.parse("http://maps.google.com/maps?saddr=" + CurrentLocation.latitude +"," + CurrentLocation.longitude + 
						"&daddr=" + item.getLatitude()+"," + item.getLongitude() + "&f=d"));
				startActivity(intent);
			}
		});
		
		if(userRating != null) {
		
			ratingBar.setRating(userRating.getTotalrating());
		}
		
		//String itemText = item.getItemdescription() + " \nCategory: " + item.getCategory() + "\nLocation: " + item.getCity() + "," + item.getState() + "\nPrice: $" + item.getPrice() + "\nQuantity: " +item.getQuantity(); 
		//txtView.setText(itemText);
		
		Button spotItButton = (Button) dialog.findViewById(R.id.bidButton);
		// if button is clicked, close the custom dialog
		spotItButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
	      		TextView bidPriceTextView = (TextView)dialog.findViewById(R.id.bid_price);
	     		TextView userDescriptionTextView = (TextView)dialog.findViewById(R.id.user_description);
	     		TextView messageTextView = (TextView)dialog.findViewById(R.id.messageTextView);
	     		
	     		if(!(bidPriceTextView.getText().toString().isEmpty() || 
	     				userDescriptionTextView.getText().toString().isEmpty())) {
		     		if(RESTData.bidOnSpot(fragment.userId, fragment.sessionId, fragment.itemId, bidPriceTextView.getText().toString(), userDescriptionTextView.getText().toString(), "buyer")) {
		         		Toast.makeText(fragment.getActivity(), "Bid for the spot.Seller will be sent notification", Toast.LENGTH_LONG).show();
		     		} else {
		         		Toast.makeText(fragment.getActivity(), "Bid FAILED! Try again later", Toast.LENGTH_LONG).show();
		     		}
		     		
		     		dialog.dismiss();
	     		} else {
	     			messageTextView.setText("Please enter the bid price and your description");
	     		}
				
				
			}
		});

		ImageView categoryImageView = (ImageView)dialog.findViewById(R.id.categoryImageView);
		TextView itemDescriptionTextView = (TextView)dialog.findViewById(R.id.itemDescriptionTextView);
		TextView locationTextView = (TextView)dialog.findViewById(R.id.locationTextView);
		TextView priceTextView = (TextView)dialog.findViewById(R.id.priceTextView);
		
		//Display icon based on category
		String category = item.getCategory();
		
		if(category.equalsIgnoreCase("concerts")) {
		
			categoryImageView.setBackgroundResource(R.drawable.concerts);
		
		} else if(category.equalsIgnoreCase("restaurant")) {
		
			categoryImageView.setBackgroundResource(R.drawable.restaurant);
		} else if(category.equalsIgnoreCase("parking")) {
		
			categoryImageView.setBackgroundResource(R.drawable.parking);
		
		} else if(category.equalsIgnoreCase("sports")) {
		
			categoryImageView.setBackgroundResource(R.drawable.sports);
		
		} else if(category.equalsIgnoreCase("movies")) {
		
			categoryImageView.setBackgroundResource(R.drawable.movies);
		
		} else if(category.equalsIgnoreCase("theme park")) {
		
			categoryImageView.setBackgroundResource(R.drawable.themepark);
		
		} else if(category.equalsIgnoreCase("general")) {
		
			categoryImageView.setBackgroundResource(R.drawable.general);
		} else {
			categoryImageView.setBackgroundResource(R.drawable.general);
		}

		
		itemDescriptionTextView.setText(item.getItemdescription());
		locationTextView.setText(item.getAddress() + "\n" + item.getCity() + "," + item.getState());
		priceTextView.setText(item.getQuantity() + " spots available for $" + item.getPrice());
		
		dialog.show();
		dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.banner);

	}

	@Override
	public void onResume() {
		super.onResume();
		updateUI();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
	
	private void updateUI() {
		

	}
	
	public class FilterData extends AsyncTask<String, Void, Void> {
	    
		ProgressDialog progressDialog;
	    
	    //declare other objects as per your need
	    @Override
	    protected void onPreExecute()
	    {
	        progressDialog = ProgressDialog.show(fragment.getActivity(), "Loading", "Filtering spots by category", true);

	        //do initialization of required objects objects here                
	    };   

	    
	    @Override
	    protected Void doInBackground(String... params)
	    {   
	    	String category = params[0];
	    	
			DBSession session = new DBSession(fragment.getActivity().getApplicationContext());

			spots = (ArrayList<Item>) RESTData.filterByCategory(session.getUserId(), session.getSessionId(), category);
			
			//Log.d(TAG, spots.toString());

	         //do loading operation here  
	        return null;
	    }       
	    @Override
	    protected void onPostExecute(Void result)
	    {
	        super.onPostExecute(result);
			if (spots != null && spots.size() > 0) {
				
				itemAdapter.clear();
				itemAdapter.notifyDataSetChanged();
				
				for (int i = 0; i < spots.size(); i++)
					itemAdapter.add(spots.get(i));
			}
			// progressDialog.dismiss();
			itemAdapter.notifyDataSetChanged();    
	        
			progressDialog.dismiss();
	    };
	 }

	
	public class LoadData extends AsyncTask<Void, Void, Void> {
	    
		ProgressDialog progressDialog;
	    
	    //declare other objects as per your need
	    @Override
	    protected void onPreExecute()
	    {
	        progressDialog = ProgressDialog.show(fragment.getActivity(), "Loading", "Getting spots of your interest", true);

	        //do initialization of required objects objects here                
	    };   

	    
	    @Override
	    protected Void doInBackground(Void... params)
	    {   
			DBSession session = new DBSession(fragment.getActivity().getApplicationContext());

			spots = (ArrayList<Item>) RESTData.getSpots(session.getUserId(), session.getSessionId(), "seller");
			
			//Log.d(TAG, spots.toString());

	         //do loading operation here  
	        return null;
	    }       
	    @Override
	    protected void onPostExecute(Void result)
	    {
	        super.onPostExecute(result);
			if (spots != null && spots.size() > 0) {
				
				itemAdapter.clear();
				itemAdapter.notifyDataSetChanged();
				
				for (int i = 0; i < spots.size(); i++)
					itemAdapter.add(spots.get(i));
			}
			// progressDialog.dismiss();
			itemAdapter.notifyDataSetChanged();    
	        
			progressDialog.dismiss();
	    };
	 }
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.main, menu);
		
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(this);
        
//		 MenuItem searchItem = menu.findItem(R.id.menu_item_search);
//	        SearchView searchView = (SearchView) searchItem.getActionView();
//
//
//	      SearchManager searchManager = (SearchManager) 
//	    		  this.getActivity().getApplicationContext().getSystemService(Context.SEARCH_SERVICE);
//	       if(null!=searchManager ) {   
//	         searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getActivity().getComponentName()));
//	        }
//	    searchView.setIconifiedByDefault(false);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.refresh:
	        new LoadData().execute();
	        return true;
	    case R.id.about:
	    	final Dialog dialog = new Dialog(this.getActivity());
			dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);

			dialog.setContentView(R.layout.about_dialog);
			
			dialog.setTitle("About SpotMob");
			dialog.show();
			dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.banner);
	        return true;
	    case R.id.myProfile:
			Intent intent = new Intent(this.getActivity(), MyProfileActivity.class);
			this.startActivity(intent);
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	
	
	public class SearchSpots extends AsyncTask<String, Void, Void> {
	    
		ProgressDialog progressDialog;
	    
	    //declare other objects as per your need
	    @Override
	    protected void onPreExecute()
	    {
	        progressDialog = ProgressDialog.show(fragment.getActivity(), "Searching", "Searching for spots", true);

	        //do initialization of required objects objects here                
	    };   

	    
	    @Override
	    protected Void doInBackground(String... params)
	    {   
	    	String searchString = params[0];
	    	
			DBSession session = new DBSession(fragment.getActivity().getApplicationContext());

			spots = (ArrayList<Item>) RESTData.searchSpots(session.getUserId(), session.getSessionId(), searchString);
			
			//Log.d(TAG, spots.toString());

	         //do loading operation here  
	        return null;
	    }       
	    @Override
	    protected void onPostExecute(Void result)
	    {
	        super.onPostExecute(result);
			if (spots != null && spots.size() > 0) {
				
				itemAdapter.clear();
				itemAdapter.notifyDataSetChanged();
				
				for (int i = 0; i < spots.size(); i++)
					itemAdapter.add(spots.get(i));
			} else {
				itemAdapter.clear();
			}
			// progressDialog.dismiss();
			itemAdapter.notifyDataSetChanged();    
	        
			progressDialog.dismiss();
	    };
	 }

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		new SearchSpots().execute(query);
		return false;
	}

}