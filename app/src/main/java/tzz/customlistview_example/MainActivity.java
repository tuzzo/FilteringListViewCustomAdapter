package tzz.customlistview_example;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText edit;
    ListView listV;
    ArrayList<Person> persons=new ArrayList<>();
    CustomAdapter mAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();        //find views of widget in layout
        populateList();     //populate the list with invented object

        mAdapter = new CustomAdapter(this,R.layout.row,persons); //set the adapter
        listV.setAdapter(mAdapter);           //assign adapter to ListView

        listV.setTextFilterEnabled(true);   //IMPORTANT for filtering

        //filtering with the EditText
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i2 < i1) {

                    // We're deleting char so we need to reset the adapter data
                    mAdapter.resetData();
                }

                mAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void findViews(){
        edit=(EditText)findViewById(R.id.edit);
        listV=(ListView)findViewById(R.id.list);
    }

    private void populateList(){
        persons.add(new Person("Galileo", "Galilei"));
        persons.add(new Person("Federico", "Faggin"));
        persons.add(new Person("Cristoforo", "Colombo"));
        persons.add(new Person("Antonio", "Meucci"));
        persons.add(new Person("Giuseppe", "Garibaldi"));
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.search){

            //Filtering with a SearchManager
            SearchManager searchManager = (SearchManager)this.getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) item.getActionView();

            searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
            searchView.setSubmitButtonEnabled(true);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    mAdapter.getFilter().filter(query);
                    return false;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    if(TextUtils.isEmpty(newText)){
                        listV.clearTextFilter();
                    }
                    mAdapter.getFilter().filter(newText);
                    listV.setFilterText(newText);
                    return true;
                }
            });
            searchView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            }
            );
        }
        return super.onOptionsItemSelected(item);
    }
}