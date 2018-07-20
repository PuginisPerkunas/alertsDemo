package buttons.games.sounds.alertstest.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import buttons.games.sounds.alertstest.AlertData.Notification;
import buttons.games.sounds.alertstest.Holders.NotificationHolder;
import buttons.games.sounds.alertstest.R;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationHolder> {

    private Context context;
    private List<Notification> itemsList;

    public NotificationAdapter(Context context, List<Notification> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        Object tempObj = itemsList.get(position).getInput();
        holder.itemText.setText(itemsList.get(position).getTitle());
        Object nullObj = "-";

        try{
            if(tempObj.equals(nullObj)){
                holder.itemEditText.setVisibility(View.GONE);
            } else {
                holder.itemEditText.setVisibility(View.VISIBLE);
            }
        } catch (NullPointerException ex){
            holder.itemEditText.setVisibility(View.GONE);
        }


        if(itemsList.get(position).getDescription() == null || itemsList.get(position).getDescription().equals("")){
            holder.explainText.setVisibility(View.GONE);
        } else {
            holder.explainText.setText(itemsList.get(position).getDescription());
        }

        holder.cardSelf.setOnClickListener(v -> {
            if(holder.itemCheckbox.isChecked())
            {
                holder.itemCheckbox.setChecked(false);
            }else
            {
                holder.itemCheckbox.setChecked(true);
            }
        });
    }
    @Override
    public int getItemCount() {
        return itemsList.size();
    }
}
