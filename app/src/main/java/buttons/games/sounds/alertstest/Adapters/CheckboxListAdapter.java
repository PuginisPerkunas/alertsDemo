package buttons.games.sounds.alertstest.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import buttons.games.sounds.alertstest.CheckableItemDTO;
import buttons.games.sounds.alertstest.Holders.ListViewItemViewHolder;
import buttons.games.sounds.alertstest.MainActivity;
import buttons.games.sounds.alertstest.R;

public class CheckboxListAdapter extends BaseAdapter implements Filterable{
    private List<CheckableItemDTO> checkableItemDTOList = null;
    private Context context = null;
    public static CheckboxListAdapter adapterFinal;

    public CheckboxListAdapter(Context context, List<CheckableItemDTO> checkableItemDTOList) {
        this.context = context;
        this.checkableItemDTOList = checkableItemDTOList;
    }

    @Override
    public int getCount() {
        int size = 0;
        if(checkableItemDTOList!=null) {
            size = checkableItemDTOList.size();
        }
        return size;
    }

    @Override
    public Object getItem(int position) {
        Object item = null;
        if(checkableItemDTOList!=null) {
            item = checkableItemDTOList.get(position);
        }
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewItemViewHolder viewHolder = null;
        if(convertView!=null)
        {
            viewHolder = (ListViewItemViewHolder) convertView.getTag();
        }else
        {
            convertView = View.inflate(context, R.layout.item_device_list_checkbox, null);
            CheckBox listItemCheckbox = (CheckBox) convertView.findViewById(R.id.list_view_item_checkbox);
            TextView listItemText = (TextView) convertView.findViewById(R.id.list_view_item_text);
            Typeface typeface = ResourcesCompat.getFont(context, R.font.open_sans_regular);
            listItemText.setTypeface(typeface);
            viewHolder = new ListViewItemViewHolder(convertView);
            viewHolder.setItemCheckbox(listItemCheckbox);
            viewHolder.setItemTextView(listItemText);
            convertView.setTag(viewHolder);
        }
        CheckableItemDTO listViewItemDto = checkableItemDTOList.get(position);
        viewHolder.getItemCheckbox().setChecked(listViewItemDto.isChecked());
        viewHolder.getItemTextView().setText(listViewItemDto.getItemText());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = null;

        if(filter == null)
            filter = new CustomFilter();
        return filter;

    }

    public class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults newFilterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<CheckableItemDTO> auxData = new ArrayList<CheckableItemDTO>();
                for (int i = 0; i < checkableItemDTOList.size(); i++) {
                    if (checkableItemDTOList.get(i).getItemText().toLowerCase().contains(constraint))
                        auxData.add(checkableItemDTOList.get(i));
                }
                newFilterResults.count = auxData.size();
                newFilterResults.values = auxData;
            } else {
                newFilterResults.count = checkableItemDTOList.size();
                newFilterResults.values = checkableItemDTOList;
            }
            return newFilterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<CheckableItemDTO> resultData = new ArrayList<CheckableItemDTO>();
            resultData = (ArrayList<CheckableItemDTO>) results.values;
            MainActivity.mListView = resultData;
            adapterFinal = new CheckboxListAdapter(context, MainActivity.mListView);
            MainActivity.mListViewView.setAdapter(adapterFinal);
        }
    }
}
