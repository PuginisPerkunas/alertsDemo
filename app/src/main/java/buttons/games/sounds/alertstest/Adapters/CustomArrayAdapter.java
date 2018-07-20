package buttons.games.sounds.alertstest.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter implements Filterable{

    public CustomArrayAdapter(@NonNull Context context,
                              int resource,
                              int textViewResourceId,
                              @NonNull List<Object> objects)
    {
        super(context, resource, textViewResourceId, objects);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        // return a filter that filters data based on a constraint

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                return null;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

            }
        };
    }
}
