package buttons.games.sounds.alertstest.Holders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import buttons.games.sounds.alertstest.R;

public class NotificationHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public CardView cardSelf;
    public TextView itemText, explainText;
    public CheckBox itemCheckbox;
    public EditText itemEditText;


    public NotificationHolder(View itemView) {
        super(itemView);
        cardSelf = itemView.findViewById(R.id.card);
        itemText = itemView.findViewById(R.id.item_textView);
        explainText = itemView.findViewById(R.id.explain_textView);
        itemCheckbox = itemView.findViewById(R.id.item_checkBox);
        itemEditText = itemView.findViewById(R.id.item_editText);

    }

    @Override
    public void onClick(View v) {

    }
}
