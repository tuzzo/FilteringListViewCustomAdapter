package tzz.customlistview_example;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tzz on 8/18/17.
 *
 */

/*for the filtering, the adapter must implements Filterable
* you have to override the getFilter method and you have to add an internal class that works as filter*/

class CustomAdapter extends ArrayAdapter<Person> implements Filterable {

    private Context context;
    private int layoutResourceId;
    private ArrayList<Person> list;
    private CustomFilter customFilter;
    private ArrayList<Person> originalList;

    CustomAdapter(Context context, int layoutResourceId, ArrayList<Person> data){
        super(context, layoutResourceId, data);
        this.context=context;
        this.layoutResourceId=layoutResourceId;
        this.list=data;
        this.originalList=data; //a copy for when you delete the filters and you want to reset the data
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row;
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //if you have the adapter as the internal class of the acitivty you can use getActivity().getLAyoutInflater()
            row = inflater.inflate(layoutResourceId,parent,false);
        }
        else
            row = convertView;

        //find views
        TextView name = (TextView)row.findViewById(R.id.name);
        TextView surname = (TextView)row.findViewById(R.id.surname);

        //set the text
        name.setText(list.get(position).getName());
        surname.setText(list.get(position).getSurname());

        //the next code block is for the text size. It's a workaround for ask the system the medium and the small sizes so I don't
        //have to define different dimensions

        TextView tempView = new TextView(context);

        if(Build.VERSION.SDK_INT >= 23) //if check because current api is 19 but the second setTextAppearance is deprecated
            tempView.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Medium);
        else
            tempView.setTextAppearance(context,android.R.style.TextAppearance_DeviceDefault_Medium);

        Float medium = tempView.getTextSize();

        if(Build.VERSION.SDK_INT >= 23) //if check because current api is 19 but the second setTextAppearance is deprecated
            tempView.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Small);
        else
            tempView.setTextAppearance(context,android.R.style.TextAppearance_DeviceDefault_Small);

        Float small = tempView.getTextSize();

        name.setTextSize(medium);
        surname.setTextSize(small);

        return row;

    }

    //the app may launch IndexOutOfBoundsException so you have to override also getCount and getItem because
    //getView will use the size of the items that the superclass has, which is the originally list passed with superclass constructor

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public Person getItem (int pos){
        return list.get(pos);
    }

    void resetData() {

        list = originalList;
    }

    @Override
    @NonNull
    public Filter getFilter(){
        if(customFilter == null){
            customFilter = new CustomFilter();
        }
        return customFilter;
    }


    //the class that creates the filter
    private class CustomFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = originalList;
                results.count = originalList.size();
            }
            else {
                // We perform filtering operation
                List<Person> newList = new ArrayList<>();

                for (Person p : list) {
                    if (p.getName().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                        newList.add(p);
                }

                results.values = newList;
                results.count = newList.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            // Now we have to inform the adapter about the new list filtered
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                list = (ArrayList<Person>) results.values;
                notifyDataSetChanged();
            }

        }

    }
}