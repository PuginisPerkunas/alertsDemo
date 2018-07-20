package buttons.games.sounds.alertstest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import buttons.games.sounds.alertstest.Adapters.CheckboxListAdapter;
import buttons.games.sounds.alertstest.Adapters.NotificationAdapter;
import buttons.games.sounds.alertstest.AlertData.AlertsDataItem;
import buttons.games.sounds.alertstest.AlertData.Type;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.test_device_layout)
    RelativeLayout mainView;
    @BindView(R.id.navigation)
    BottomNavigationView mBottomNavigation;

    @SuppressLint("StaticFieldLeak")
    public static ListView mListViewView;

    private View viewInflated;
    public static CheckboxListAdapter listViewDataAdapter;
    public static List<CheckableItemDTO> mListView;
    public AlertsDataItem mainDataList;

    boolean isZoomInClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mainDataList = getMainData();
        //Handle bottom navigation item
        mBottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            clearView();
            switch (item.getItemId()) {
                //Devices Tab
                case R.id.devices:
                    //Get layout
                    viewInflated = View.inflate(MainActivity.this,R.layout.devices_layout,null);
                    //Add View To container in MainActivity
                    mainView.addView(viewInflated);
                    //Next-step handler
                    mainHandler(AlertsConstants.DEVICE);
                    return true;
                case R.id.type:
                    //Get layout
                    viewInflated = View.inflate(MainActivity.this,R.layout.type_layout,null);
                    //Add View To container in MainActivity
                    mainView.addView(viewInflated);
                    //Next-step handler
                    mainHandler(AlertsConstants.TYPE);
                    return true;
                case R.id.geofencing:
                    //Get layout
                    viewInflated = View.inflate(MainActivity.this,R.layout.geofencing_layout,null);
                    //Add View To container in MainActivity
                    mainView.addView(viewInflated);
                    //Next-step handler
                    mainHandler(AlertsConstants.GEOFENCING);
                    return true;
                case R.id.schedule:

                    return true;
                case R.id.notifications:
                    //Get layout
                    viewInflated = View.inflate(MainActivity.this,R.layout.notifications_layout,null);
                    //Add View To container in MainActivity
                    mainView.addView(viewInflated);
                    //Next-step handler
                    mainHandler(AlertsConstants.NOTIFICATIONS);
                    return true;
            }
            return false;
        }
    };

    //Need to clean view container before use
    private void clearView(){
        if (null != mainView && mainView.getChildCount() > 0) {
            try {
                mainView.removeViews (0, mainView.getChildCount());
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(getBaseContext().toString(), e.getMessage());
            }
        }
    }

    private void mainHandler(String tag){
        // tag - bottom navigation selection
        switch (tag) {
            case AlertsConstants.DEVICE:
                //Init items
                EditText nameEditText = viewInflated.findViewById(R.id.name_et);
                EditText searchEditText = viewInflated.findViewById(R.id.search_et);
                Button selectAllDevice = viewInflated.findViewById(R.id.selectAll_btn);
                Button deselectAllDevice = viewInflated.findViewById(R.id.deselectAll_btn);

                mListViewView = viewInflated.findViewById(R.id.devices_lv);
                // Initiate listview data.
                mListView = this.getCheckableItemList(AlertsConstants.DEVICE, 0);
                // Create a custom list view adapter with checkbox control
                listViewDataAdapter =
                        new CheckboxListAdapter(getApplicationContext(), mListView);

                listViewDataAdapter.notifyDataSetChanged();
                // Set data adapter to list view.
                mListViewView.setAdapter(listViewDataAdapter);
                mListViewView.setTextFilterEnabled(true);
                searchEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        listViewDataAdapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                setViewListItemClickListener(mListViewView);
                setSelectListeners(selectAllDevice, deselectAllDevice);
                break;
                //TODO TYPE SECTION
            case AlertsConstants.TYPE:
                Spinner typeAutoCompleteText = viewInflated.findViewById(R.id.type_auto_complete_text);
                RelativeLayout dataHolder = viewInflated.findViewById(R.id.type_items_layout);
                final ArrayAdapter<Type> deviceAdapter
                        = new ArrayAdapter<>(getBaseContext(), R.layout.item_device, R.id.type_item, mainDataList.getTypes());
                typeAutoCompleteText.setAdapter(deviceAdapter);
                typeAutoCompleteText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //Get selected type
                    dataHolder.removeAllViews();
                    Object item = parent.getItemAtPosition(position);
                    if (item instanceof Type){
                        Type selectedType= (Type) item;
                        Log.d("SelectedType: ",selectedType.getType());

                        switch (selectedType.getAttributes().get(0).getType()){
                            case AlertsConstants.INTEGER_TYPE:
                                View tempViewInteger = View.inflate(MainActivity.this,
                                        R.layout.item_type_integer,
                                        null);
                                TextView viewTitle = tempViewInteger.findViewById(R.id.integer_title);
                                viewTitle.setText(selectedType.getAttributes().get(0).getTitle());
                                dataHolder.addView(tempViewInteger);
                            break;

                            case AlertsConstants.MULTISELECT_TYPE:

                                View mMultiselectView = View.inflate(MainActivity.this,
                                        R.layout.item_type_multiselect,
                                        null);

                                TextView listTitle = mMultiselectView.findViewById(R.id.listTitle);
                                listTitle.setText(selectedType.getAttributes().get(0).getTitle());
                                Button selectAll = mMultiselectView.findViewById(R.id.selectAll_btn);
                                Button deselectAll = mMultiselectView.findViewById(R.id.deselectAll_btn);
//                                ListView listView = mMultiselectView.findViewById(R.id.multiselect_lv);
                                EditText mSearch = mMultiselectView.findViewById(R.id.multiselect_search_et);
                                mListViewView = mMultiselectView.findViewById(R.id.multiselect_lv);
                                // Initiate listview data.
                                mListView = getCheckableItemList(AlertsConstants.MULTISELECT_TYPE, position);
                                // Create a custom list view adapter with checkbox control
                                listViewDataAdapter =
                                        new CheckboxListAdapter(getApplicationContext(), mListView);

                                listViewDataAdapter.notifyDataSetChanged();
                                // Set data adapter to list view.
                                mListViewView.setAdapter(listViewDataAdapter);
                                mListViewView.setTextFilterEnabled(true);

                                mSearch.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        listViewDataAdapter.getFilter().filter(s);
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });

                                setViewListItemClickListener(mListViewView);
                                setSelectListeners(selectAll, deselectAll);
                                dataHolder.addView(mMultiselectView);
                                //TODO IMPLEMENT ON SELECT AND DESELECT BUTTONS
                            break;
                        }

//                        if(((Type) item).getAttributes().get(0).getType().equals("integer")){
//                            View tempView = View.inflate(MainActivity.this,R.layout.item_type_integer,null);
//                            TextView tempViewTitle = tempView.findViewById(R.id.integer_title);
//                            tempViewTitle.setText(selectedType.getAttributes().get(0).getTitle());
//                            dataHolder.addView(tempView);
//                        } else{
//
//                        }
                    }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                break;
            //TODO TYPE SECTION
            case AlertsConstants.GEOFENCING:
                //init items
                EditText searchEditTextGeo = viewInflated.findViewById(R.id.search_et);
                Button zoomInBtn = viewInflated.findViewById(R.id.zoomIn_btn);
                Button zoomOutBtn = viewInflated.findViewById(R.id.zoomOut_btn);
                Button selectAllGeo = viewInflated.findViewById(R.id.selectAll_btn);
                Button deselectAllGeo = viewInflated.findViewById(R.id.deselectAll_btn);
                CardView zoomInCard = viewInflated.findViewById(R.id.zoomIn_card);
                CardView zoomOutCard = viewInflated.findViewById(R.id.zoomOut_card);

                zoomInBtn.setOnClickListener(v ->{
                    zoomInBtn.setTextColor(getResources().getColor(R.color.cardBackgroundColor));
                    zoomInCard.setBackgroundColor(getResources().getColor(R.color.btnTextColor));
                    zoomOutBtn.setTextColor(getResources().getColor(R.color.btnTextColor));
                    zoomOutCard.setBackgroundColor(getResources().getColor(R.color.cardBackgroundColor));
                });
                zoomOutBtn.setOnClickListener(v -> {
                    zoomOutBtn.setTextColor(getResources().getColor(R.color.cardBackgroundColor));
                    zoomOutCard.setBackgroundColor(getResources().getColor(R.color.btnTextColor));
                    zoomInBtn.setTextColor(getResources().getColor(R.color.btnTextColor));
                    zoomInCard.setBackgroundColor(getResources().getColor(R.color.cardBackgroundColor));
                });

                mListViewView = viewInflated.findViewById(R.id.devices_lv);
                // Initiate listview data.
                mListView = this.getCheckableItemList(AlertsConstants.GEOFENCING, 0);
                // Create a custom list view adapter with checkbox control
                listViewDataAdapter =
                        new CheckboxListAdapter(getApplicationContext(), mListView);

                listViewDataAdapter.notifyDataSetChanged();
                // Set data adapter to list view.
                mListViewView.setAdapter(listViewDataAdapter);
                mListViewView.setTextFilterEnabled(true);

                searchEditTextGeo.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        listViewDataAdapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                setViewListItemClickListener(mListViewView);
                setSelectListeners(selectAllGeo, deselectAllGeo);
                break;
            case AlertsConstants.SCHEDULE:

                break;
            case AlertsConstants.NOTIFICATIONS:

                RecyclerView notificationsRecyclerView;
                LinearLayoutManager linearLayoutManager;
                NotificationAdapter adapter;

                notificationsRecyclerView = viewInflated.findViewById(R.id.notification_rv);
                linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                notificationsRecyclerView.setLayoutManager(linearLayoutManager);

                adapter = new NotificationAdapter(MainActivity.this,getMainData().getNotifications());
                notificationsRecyclerView.setAdapter(adapter);

                break;
        }
    }

    private void setSelectListeners(Button selectButton, Button deselectButton){
        // Click this button to select all listview items with checkbox checked.
        selectButton.setOnClickListener(view -> {
            int size = mListView.size();
            for(int i=0;i<size;i++)
            {
                CheckableItemDTO dto = mListView.get(i);
                dto.setChecked(true);
            }
            if(CheckboxListAdapter.adapterFinal !=null){
                CheckboxListAdapter.adapterFinal.notifyDataSetChanged();
            }
            listViewDataAdapter.notifyDataSetChanged();
        });
        // Click this button to disselect all listview items with checkbox unchecked.
        deselectButton.setOnClickListener(view -> {
            int size = mListView.size();
            for(int i=0;i<size;i++)
            {
                CheckableItemDTO dto = mListView.get(i);
                dto.setChecked(false);
            }
            if(CheckboxListAdapter.adapterFinal !=null){
                CheckboxListAdapter.adapterFinal.notifyDataSetChanged();
            }
            listViewDataAdapter.notifyDataSetChanged();
        });
    }

    private void setViewListItemClickListener(ListView mListViewView){
        // When list view item is clicked.
        mListViewView.setOnItemClickListener((adapterView, view, itemIndex, l) -> {
            // Get user selected item.
            Object itemObject = adapterView.getAdapter().getItem(itemIndex);
            // Translate the selected item to DTO object.
            CheckableItemDTO itemDto = (CheckableItemDTO) itemObject;
            // Get the checkbox.
            CheckBox itemCheckbox = (CheckBox) view.findViewById(R.id.list_view_item_checkbox);
            // Reverse the checkbox and clicked item check state.
            if(itemDto.isChecked())
            {
                itemCheckbox.setChecked(false);
                itemDto.setChecked(false);
            }else
            {
                itemCheckbox.setChecked(true);
                itemDto.setChecked(true);
            }
        });
    }

    //Temp solution
    private List<CheckableItemDTO> getCheckableItemList(String type , int position)
    {
        List<CheckableItemDTO> ret = new ArrayList<CheckableItemDTO>();
        int length;

        switch (type){
            case AlertsConstants.DEVICE:
                length = mainDataList.getDevices().size();
                for(int i=0;i<length;i++)
                {
                    String itemText = mainDataList.getDevices().get(i).getValue();
                    CheckableItemDTO dto = new CheckableItemDTO();
                    dto.setChecked(false);
                    dto.setItemText(itemText);
                    ret.add(dto);
                }
                return ret;
            case AlertsConstants.GEOFENCING:
                length = mainDataList.getGeofences().size();
                for(int i=0;i<length;i++)
                {
                    String itemText = mainDataList.getGeofences().get(i).getValue();
                    CheckableItemDTO dto = new CheckableItemDTO();
                    dto.setChecked(false);
                    dto.setItemText(itemText);
                    ret.add(dto);
                }
                return ret;
            case AlertsConstants.MULTISELECT_TYPE:
                length = mainDataList.getTypes().get(position).getAttributes().get(0).getOptions().size();
                for(int i=0;i<length;i++)
                {
                    String itemText = mainDataList.getTypes().get(position).getAttributes().get(0).getOptions().get(i).getName();
                    CheckableItemDTO dto = new CheckableItemDTO();
                    dto.setChecked(false);
                    dto.setItemText(itemText);
                    ret.add(dto);
                }
                return ret;
        }
        return ret;
    }

    private AlertsDataItem getMainData(){
        AlertsDataItem tempData;
        String rawJson = "{\"devices\":[{\"id\":138,\"value\":\"A3\",\"title\":\"A3\"},{\"id\":956,\"value\":\"asd\",\"title\":\"asd\"},{\"id\":905,\"value\":\"Belekas\",\"title\":\"Belekas\"},{\"id\":121,\"value\":\"Birla\",\"title\":\"Birla\"},{\"id\":122,\"value\":\"cron\",\"title\":\"cron\"},{\"id\":124,\"value\":\"Demo 1\",\"title\":\"Demo 1\"},{\"id\":133,\"value\":\"Demo 10\",\"title\":\"Demo 10\"},{\"id\":134,\"value\":\"Demo 11\",\"title\":\"Demo 11\"},{\"id\":135,\"value\":\"Demo 12\",\"title\":\"Demo 12\"},{\"id\":136,\"value\":\"Demo 13\",\"title\":\"Demo 13\"},{\"id\":125,\"value\":\"Demo 2\",\"title\":\"Demo 2\"},{\"id\":126,\"value\":\"Demo 3 Demo 3 Demo 3\",\"title\":\"Demo 3 Demo 3 Demo 3\"},{\"id\":127,\"value\":\"Demo 4\",\"title\":\"Demo 4\"},{\"id\":128,\"value\":\"Demo 5\",\"title\":\"Demo 5\"},{\"id\":129,\"value\":\"Demo 6\",\"title\":\"Demo 6\"},{\"id\":130,\"value\":\"Demo 7\",\"title\":\"Demo 7\"},{\"id\":131,\"value\":\"Demo 8\",\"title\":\"Demo 8\"},{\"id\":132,\"value\":\"Demo 9\",\"title\":\"Demo 9\"},{\"id\":935,\"value\":\"Device name 1\",\"title\":\"Device name 1\"},{\"id\":958,\"value\":\"emulator\",\"title\":\"emulator\"},{\"id\":140,\"value\":\"Fake 1\",\"title\":\"Fake 1\"},{\"id\":149,\"value\":\"Fake 10\",\"title\":\"Fake 10\"},{\"id\":239,\"value\":\"Fake 100\",\"title\":\"Fake 100\"},{\"id\":240,\"value\":\"Fake 101\",\"title\":\"Fake 101\"},{\"id\":241,\"value\":\"Fake 102\",\"title\":\"Fake 102\"},{\"id\":242,\"value\":\"Fake 103\",\"title\":\"Fake 103\"},{\"id\":243,\"value\":\"Fake 104\",\"title\":\"Fake 104\"},{\"id\":244,\"value\":\"Fake 105\",\"title\":\"Fake 105\"},{\"id\":245,\"value\":\"Fake 106\",\"title\":\"Fake 106\"},{\"id\":246,\"value\":\"Fake 107\",\"title\":\"Fake 107\"},{\"id\":247,\"value\":\"Fake 108\",\"title\":\"Fake 108\"},{\"id\":248,\"value\":\"Fake 109\",\"title\":\"Fake 109\"},{\"id\":150,\"value\":\"Fake 11\",\"title\":\"Fake 11\"},{\"id\":249,\"value\":\"Fake 110\",\"title\":\"Fake 110\"},{\"id\":250,\"value\":\"Fake 111\",\"title\":\"Fake 111\"},{\"id\":251,\"value\":\"Fake 112\",\"title\":\"Fake 112\"},{\"id\":252,\"value\":\"Fake 113\",\"title\":\"Fake 113\"},{\"id\":253,\"value\":\"Fake 114\",\"title\":\"Fake 114\"},{\"id\":254,\"value\":\"Fake 115\",\"title\":\"Fake 115\"},{\"id\":255,\"value\":\"Fake 116\",\"title\":\"Fake 116\"},{\"id\":256,\"value\":\"Fake 117\",\"title\":\"Fake 117\"},{\"id\":257,\"value\":\"Fake 118\",\"title\":\"Fake 118\"},{\"id\":258,\"value\":\"Fake 119\",\"title\":\"Fake 119\"},{\"id\":151,\"value\":\"Fake 12\",\"title\":\"Fake 12\"},{\"id\":259,\"value\":\"Fake 120\",\"title\":\"Fake 120\"},{\"id\":260,\"value\":\"Fake 121\",\"title\":\"Fake 121\"},{\"id\":261,\"value\":\"Fake 122\",\"title\":\"Fake 122\"},{\"id\":262,\"value\":\"Fake 123\",\"title\":\"Fake 123\"},{\"id\":263,\"value\":\"Fake 124\",\"title\":\"Fake 124\"},{\"id\":264,\"value\":\"Fake 125\",\"title\":\"Fake 125\"},{\"id\":265,\"value\":\"Fake 126\",\"title\":\"Fake 126\"},{\"id\":899,\"value\":\"Fake 897\",\"title\":\"Fake 897\"},{\"id\":900,\"value\":\"Fake 898\",\"title\":\"Fake 898\"},{\"id\":901,\"value\":\"Fake 899\",\"title\":\"Fake 899\"},{\"id\":148,\"value\":\"Fake 9\",\"title\":\"Fake 9\"},{\"id\":229,\"value\":\"Fake 90\",\"title\":\"Fake 90\"},{\"id\":902,\"value\":\"Fake 900\",\"title\":\"Fake 900\"},{\"id\":903,\"value\":\"Fake 901\",\"title\":\"Fake 901\"},{\"id\":230,\"value\":\"Fake 91\",\"title\":\"Fake 91\"},{\"id\":231,\"value\":\"Fake 92\",\"title\":\"Fake 92\"},{\"id\":232,\"value\":\"Fake 93\",\"title\":\"Fake 93\"},{\"id\":233,\"value\":\"Fake 94\",\"title\":\"Fake 94\"},{\"id\":234,\"value\":\"Fake 95\",\"title\":\"Fake 95\"},{\"id\":235,\"value\":\"Fake 96\",\"title\":\"Fake 96\"},{\"id\":236,\"value\":\"Fake 97\",\"title\":\"Fake 97\"},{\"id\":237,\"value\":\"Fake 98\",\"title\":\"Fake 98\"},{\"id\":238,\"value\":\"Fake 99\",\"title\":\"Fake 99\"},{\"id\":942,\"value\":\"gpswox OBDII Tracker for test\",\"title\":\"gpswox OBDII Tracker for test\"},{\"id\":943,\"value\":\"gpswox Vehicle Tracker for test\",\"title\":\"gpswox Vehicle Tracker for test\"},{\"id\":955,\"value\":\"GSPDATA\",\"title\":\"GSPDATA\"},{\"id\":945,\"value\":\"huawey honor 4c\",\"title\":\"huawey honor 4c\"},{\"id\":910,\"value\":\"ignas\",\"title\":\"ignas\"},{\"id\":949,\"value\":\"iiiooii\",\"title\":\"iiiooii\"},{\"id\":957,\"value\":\"iPhone test\",\"title\":\"iPhone test\"},{\"id\":962,\"value\":\"iPhone Tracker Test\",\"title\":\"iPhone Tracker Test\"},{\"id\":959,\"value\":\"nexus 6p\",\"title\":\"nexus 6p\"},{\"id\":963,\"value\":\"Osmand test\",\"title\":\"Osmand test\"},{\"id\":965,\"value\":\"sockettest\",\"title\":\"sockettest\"},{\"id\":948,\"value\":\"Taxi meter with photos\",\"title\":\"Taxi meter with photos\"},{\"id\":964,\"value\":\"Teltonika FM3612 + LVCAN200\",\"title\":\"Teltonika FM3612 + LVCAN200\"},{\"id\":137,\"value\":\"Testing\",\"title\":\"Testing\"},{\"id\":918,\"value\":\"testing\",\"title\":\"testing\"},{\"id\":904,\"value\":\"testint\",\"title\":\"testint\"},{\"id\":960,\"value\":\"tracker nexus 6p\",\"title\":\"tracker nexus 6p\"},{\"id\":944,\"value\":\"ulbotech\",\"title\":\"ulbotech\"},{\"id\":961,\"value\":\"Watch 3G\",\"title\":\"Watch 3G\"}],\"geofences\":[{\"id\":9,\"value\":\"Geo\",\"title\":\"Geo\"},{\"id\":11,\"value\":\"Test\",\"title\":\"Test\"},{\"id\":12,\"value\":\"Testinh\",\"title\":\"Testinh\"},{\"id\":13,\"value\":\"Geo2\",\"title\":\"Geo2\"},{\"id\":19,\"value\":\"labas\",\"title\":\"labas\"},{\"id\":20,\"value\":\"LIETUVA !\",\"title\":\"LIETUVA !\"},{\"id\":21,\"value\":\"White test\",\"title\":\"White test\"},{\"id\":22,\"value\":\"Ofisas\",\"title\":\"Ofisas\"},{\"id\":23,\"value\":\"Home\",\"title\":\"Home\"},{\"id\":24,\"value\":\"Cross\",\"title\":\"Cross\"}],\"types\":[{\"type\":\"overspeed\",\"title\":\"Overspeed\",\"attributes\":[{\"name\":\"overspeed\",\"title\":\"Overspeed(kph)\",\"type\":\"integer\",\"default\":[]}]},{\"type\":\"stop_duration\",\"title\":\"Stop duration\",\"attributes\":[{\"name\":\"stop_duration\",\"title\":\"Stop duration longer than(minutes)\",\"type\":\"integer\",\"default\":[]}]},{\"type\":\"driver\",\"title\":\"Driver change\",\"attributes\":[{\"name\":\"drivers\",\"title\":\"Drivers:\",\"type\":\"multiselect\",\"options\":[{\"id\":\"3\",\"user_id\":\"1\",\"device_id\":\"954\",\"device_port\":null,\"name\":\"Testing\",\"rfid\":\"ABC451132\",\"phone\":\"4\",\"email\":\"\",\"description\":\"\",\"created_at\":\"2017-06-20 23:00:43\",\"updated_at\":\"2018-06-28 13:12:49\",\"title\":\"Testing\"},{\"id\":\"4\",\"user_id\":\"1\",\"device_id\":\"124\",\"device_port\":null,\"name\":\"Testing 2\",\"rfid\":\"ABC11132456\",\"phone\":\"\",\"email\":\"\",\"description\":\"\",\"created_at\":\"2017-06-20 23:01:57\",\"updated_at\":\"2018-01-30 16:13:31\",\"title\":\"Testing 2\"},{\"id\":\"6\",\"user_id\":\"1\",\"device_id\":\"133\",\"device_port\":null,\"name\":\"Testing 33\",\"rfid\":\"23\",\"phone\":\"\",\"email\":\"\",\"description\":\"\",\"created_at\":\"2017-06-20 23:12:04\",\"updated_at\":\"2018-01-30 14:07:44\",\"title\":\"Testing 33\"},{\"id\":\"7\",\"user_id\":\"1\",\"device_id\":\"138\",\"device_port\":null,\"name\":\"Ignas\",\"rfid\":\"\",\"phone\":\"123456789\",\"email\":\"\",\"description\":\"\",\"created_at\":\"2017-12-22 07:59:55\",\"updated_at\":\"2018-01-30 14:45:00\",\"title\":\"Ignas\"},{\"id\":\"8\",\"user_id\":\"1\",\"device_id\":\"124\",\"device_port\":null,\"name\":\"Ignukas\",\"rfid\":\"\",\"phone\":\"\",\"email\":\"\",\"description\":\"jkjj\",\"created_at\":\"2017-12-22 13:42:00\",\"updated_at\":\"2018-01-01 21:59:35\",\"title\":\"Ignukas\"},{\"id\":\"15\",\"user_id\":\"1\",\"device_id\":\"941\",\"device_port\":null,\"name\":\"Aha\",\"rfid\":\"ABC111\",\"phone\":\"62222408\",\"email\":\"a@at.lt\",\"description\":\"Testuojam\",\"created_at\":\"2017-12-29 13:27:46\",\"updated_at\":\"2018-02-23 11:38:15\",\"title\":\"Aha\"},{\"id\":\"16\",\"user_id\":\"1\",\"device_id\":\"910\",\"device_port\":null,\"name\":\"Tomas \",\"rfid\":\"\",\"phone\":\"\",\"email\":\"a@a.lt\",\"description\":\"\",\"created_at\":\"2017-12-30 14:14:03\",\"updated_at\":\"2017-12-30 14:14:03\",\"title\":\"Tomas \"},{\"id\":\"17\",\"user_id\":\"1\",\"device_id\":\"954\",\"device_port\":null,\"name\":\"Linas\",\"rfid\":\"\",\"phone\":\"\",\"email\":\"\",\"description\":\"testing\",\"created_at\":\"2018-04-06 23:14:32\",\"updated_at\":\"2018-04-06 23:14:32\",\"title\":\"Linas\"}],\"default\":[]}]},{\"type\":\"geofence_in\",\"title\":\"Geofence In\",\"attributes\":[{\"name\":\"geofences\",\"title\":\"Geofences\",\"type\":\"multiselect\",\"options\":[{\"id\":\"9\",\"user_id\":\"1\",\"group_id\":0,\"active\":\"1\",\"name\":\"Geo\",\"coordinates\":\"[{\\\"lat\\\":55.670614828967,\\\"lng\\\":12.628784179688},{\\\"lat\\\":55.672260536717,\\\"lng\\\":12.610416412354},{\\\"lat\\\":55.665193184436,\\\"lng\\\":12.60046005249},{\\\"lat\\\":55.664805895369,\\\"lng\\\":12.615222930908},{\\\"lat\\\":55.659480282028,\\\"lng\\\":12.627410888672}]\",\"polygon_color\":\"#d000df\",\"created_at\":\"2017-07-18 06:16:03\",\"updated_at\":\"2018-05-23 21:28:30\",\"title\":\"Geo\"},{\"id\":\"11\",\"user_id\":\"1\",\"group_id\":0,\"active\":\"1\",\"name\":\"Test\",\"coordinates\":\"[{\\\"lat\\\":55.689197622339,\\\"lng\\\":12.431030273438},{\\\"lat\\\":55.690358753803,\\\"lng\\\":12.420558929443},{\\\"lat\\\":55.692874420351,\\\"lng\\\":12.398071289062},{\\\"lat\\\":55.697131333455,\\\"lng\\\":12.383480072021},{\\\"lat\\\":55.700033508435,\\\"lng\\\":12.369403839111},{\\\"lat\\\":55.683197893777,\\\"lng\\\":12.335243225098},{\\\"lat\\\":55.664031305736,\\\"lng\\\":12.31481552124},{\\\"lat\\\":55.642917836815,\\\"lng\\\":12.340393066406},{\\\"lat\\\":55.643692844267,\\\"lng\\\":12.382278442383},{\\\"lat\\\":55.642917836815,\\\"lng\\\":12.400131225586},{\\\"lat\\\":55.642724082556,\\\"lng\\\":12.417812347412},{\\\"lat\\\":55.647083321567,\\\"lng\\\":12.421760559082},{\\\"lat\\\":55.657930876627,\\\"lng\\\":12.421245574951},{\\\"lat\\\":55.669356299863,\\\"lng\\\":12.420644760132},{\\\"lat\\\":55.674777367624,\\\"lng\\\":12.421073913574},{\\\"lat\\\":55.678165153556,\\\"lng\\\":12.423820495605},{\\\"lat\\\":55.682907560981,\\\"lng\\\":12.42639541626}]\",\"polygon_color\":\"#65c999\",\"created_at\":\"2017-09-11 11:43:14\",\"updated_at\":\"2018-05-23 21:28:12\",\"title\":\"Test\"},{\"id\":\"12\",\"user_id\":\"1\",\"group_id\":0,\"active\":\"1\",\"name\":\"Testinh\",\"coordinates\":\"[{\\\"lat\\\":49.213865232086,\\\"lng\\\":18.735176324844},{\\\"lat\\\":49.21379865151,\\\"lng\\\":18.738502264023},{\\\"lat\\\":49.212782410539,\\\"lng\\\":18.735128045082}]\",\"polygon_color\":\"#a660ab\",\"created_at\":\"2017-09-18 16:19:38\",\"updated_at\":\"2017-09-18 16:19:38\",\"title\":\"Testinh\"},{\"id\":\"13\",\"user_id\":\"1\",\"group_id\":0,\"active\":\"1\",\"name\":\"Geo2\",\"coordinates\":\"[{\\\"lat\\\":51.488544993181,\\\"lng\\\":3.9162826538086},{\\\"lat\\\":51.495064730144,\\\"lng\\\":3.8553428649902},{\\\"lat\\\":51.474219674077,\\\"lng\\\":3.8503646850586},{\\\"lat\\\":51.468766318141,\\\"lng\\\":3.9114761352539}]\",\"polygon_color\":\"#d000df\",\"created_at\":\"2017-09-19 11:50:01\",\"updated_at\":\"2017-09-19 11:50:01\",\"title\":\"Geo2\"},{\"id\":\"19\",\"user_id\":\"1\",\"group_id\":0,\"active\":\"1\",\"name\":\"labas\",\"coordinates\":\"[{\\\"lat\\\":47.871670554416,\\\"lng\\\":3.1394200399518},{\\\"lat\\\":53.748294056322,\\\"lng\\\":2.8318025171757},{\\\"lat\\\":53.903922960948,\\\"lng\\\":10.478288568556},{\\\"lat\\\":48.6904942835,\\\"lng\\\":11.313246823847}]\",\"polygon_color\":\"#ff6790\",\"created_at\":\"2017-11-14 17:23:44\",\"updated_at\":\"2017-11-14 17:23:44\",\"title\":\"labas\"},{\"id\":\"20\",\"user_id\":\"1\",\"group_id\":0,\"active\":\"0\",\"name\":\"LIETUVA !\",\"coordinates\":\"[{\\\"lat\\\":55.053202585371,\\\"lng\\\":22.582397460938},{\\\"lat\\\":54.961848039842,\\\"lng\\\":22.758178710938},{\\\"lat\\\":54.826007999095,\\\"lng\\\":22.922973632813},{\\\"lat\\\":54.724620194924,\\\"lng\\\":22.780151367188},{\\\"lat\\\":54.549765673898,\\\"lng\\\":22.659301757812},{\\\"lat\\\":54.399748155638,\\\"lng\\\":22.780151367188},{\\\"lat\\\":54.338946534621,\\\"lng\\\":23.04931640625},{\\\"lat\\\":54.25559837127,\\\"lng\\\":23.3349609375},{\\\"lat\\\":54.130259578795,\\\"lng\\\":23.516235351563},{\\\"lat\\\":53.985165238685,\\\"lng\\\":23.48876953125},{\\\"lat\\\":53.933453949447,\\\"lng\\\":23.7744140625},{\\\"lat\\\":53.952853199669,\\\"lng\\\":24.08203125},{\\\"lat\\\":53.910810087254,\\\"lng\\\":24.41162109375},{\\\"lat\\\":54.001311864648,\\\"lng\\\":24.570922851563},{\\\"lat\\\":53.998083040359,\\\"lng\\\":24.78515625},{\\\"lat\\\":54.149567212541,\\\"lng\\\":24.845581054688},{\\\"lat\\\":54.14634989866,\\\"lng\\\":25.0927734375},{\\\"lat\\\":54.262015759179,\\\"lng\\\":25.328979492188},{\\\"lat\\\":54.294087719276,\\\"lng\\\":25.499267578125},{\\\"lat\\\":54.139914520834,\\\"lng\\\":25.576171875},{\\\"lat\\\":54.159217654167,\\\"lng\\\":25.762939453125},{\\\"lat\\\":54.29088164657,\\\"lng\\\":25.713500976562},{\\\"lat\\\":54.310114339162,\\\"lng\\\":25.614624023438},{\\\"lat\\\":54.40614309032,\\\"lng\\\":25.609130859375},{\\\"lat\\\":54.597527852114,\\\"lng\\\":25.740966796875},{\\\"lat\\\":54.816513684853,\\\"lng\\\":25.762939453125},{\\\"lat\\\":54.955540054617,\\\"lng\\\":25.933227539063},{\\\"lat\\\":54.987070078949,\\\"lng\\\":26.202392578125},{\\\"lat\\\":55.141209644495,\\\"lng\\\":26.328735351562},{\\\"lat\\\":55.144349170977,\\\"lng\\\":26.614379882813},{\\\"lat\\\":55.288500557924,\\\"lng\\\":26.834106445313},{\\\"lat\\\":55.335393612016,\\\"lng\\\":26.6748046875},{\\\"lat\\\":55.329144408405,\\\"lng\\\":26.444091796875},{\\\"lat\\\":55.410307210052,\\\"lng\\\":26.531982421875},{\\\"lat\\\":55.50686080246,\\\"lng\\\":26.52099609375},{\\\"lat\\\":55.575239380091,\\\"lng\\\":26.641845703125},{\\\"lat\\\":55.67758441109,\\\"lng\\\":26.641845703125},{\\\"lat\\\":55.71164005362,\\\"lng\\\":26.356201171875},{\\\"lat\\\":55.862982311976,\\\"lng\\\":26.174926757812},{\\\"lat\\\":56.087362474951,\\\"lng\\\":25.724487304688},{\\\"lat\\\":56.16084725409,\\\"lng\\\":25.504760742188},{\\\"lat\\\":56.185310993255,\\\"lng\\\":25.087280273438},{\\\"lat\\\":56.301301022161,\\\"lng\\\":24.993896484375},{\\\"lat\\\":56.441240391404,\\\"lng\\\":24.889526367188},{\\\"lat\\\":56.380460326029,\\\"lng\\\":24.664306640625},{\\\"lat\\\":56.283010159802,\\\"lng\\\":24.554443359375},{\\\"lat\\\":56.270811388415,\\\"lng\\\":24.19189453125},{\\\"lat\\\":56.313490068258,\\\"lng\\\":24.021606445312},{\\\"lat\\\":56.337856494411,\\\"lng\\\":23.812866210938},{\\\"lat\\\":56.368292660394,\\\"lng\\\":23.675537109375},{\\\"lat\\\":56.334811541652,\\\"lng\\\":23.527221679688},{\\\"lat\\\":56.377418773876,\\\"lng\\\":23.367919921875},{\\\"lat\\\":56.362207370444,\\\"lng\\\":23.175659179688},{\\\"lat\\\":56.295205040556,\\\"lng\\\":23.082275390625},{\\\"lat\\\":56.398704540115,\\\"lng\\\":22.955932617188},{\\\"lat\\\":56.359164361149,\\\"lng\\\":22.703247070313},{\\\"lat\\\":56.401744392759,\\\"lng\\\":22.439575195313},{\\\"lat\\\":56.42605447605,\\\"lng\\\":22.033081054688},{\\\"lat\\\":56.32872090718,\\\"lng\\\":21.796875},{\\\"lat\\\":56.289108086465,\\\"lng\\\":21.478271484375},{\\\"lat\\\":56.191424492755,\\\"lng\\\":21.264038085938},{\\\"lat\\\":56.096555750568,\\\"lng\\\":21.192626953125},{\\\"lat\\\":56.072035471801,\\\"lng\\\":21.044311523438},{\\\"lat\\\":55.80128097118,\\\"lng\\\":20.994873046875},{\\\"lat\\\":55.297883605106,\\\"lng\\\":20.8740234375},{\\\"lat\\\":55.244683663497,\\\"lng\\\":21.170654296875},{\\\"lat\\\":55.29475616922,\\\"lng\\\":21.37939453125},{\\\"lat\\\":55.203953257859,\\\"lng\\\":21.522216796875},{\\\"lat\\\":55.100373319786,\\\"lng\\\":21.901245117188}]\",\"polygon_color\":\"#fa1039\",\"created_at\":\"2017-11-15 06:26:52\",\"updated_at\":\"2018-03-12 15:27:37\",\"title\":\"LIETUVA !\"},{\"id\":\"21\",\"user_id\":\"1\",\"group_id\":0,\"active\":\"1\",\"name\":\"White test\",\"coordinates\":\"[{\\\"lat\\\":54.007768761935,\\\"lng\\\":20.2587890625},{\\\"lat\\\":53.80065082633,\\\"lng\\\":16.0400390625},{\\\"lat\\\":52.616390233045,\\\"lng\\\":15.2490234375},{\\\"lat\\\":52.456009392641,\\\"lng\\\":20.8740234375},{\\\"lat\\\":53.670680193473,\\\"lng\\\":21.3134765625}]\",\"polygon_color\":\"#ffffff\",\"created_at\":\"2018-05-09 16:06:36\",\"updated_at\":\"2018-05-09 16:06:36\",\"title\":\"White test\"},{\"id\":\"22\",\"user_id\":\"1\",\"group_id\":0,\"active\":\"1\",\"name\":\"Ofisas\",\"coordinates\":\"[{\\\"lat\\\":54.698148539024,\\\"lng\\\":25.267063379288},{\\\"lat\\\":54.698278739291,\\\"lng\\\":25.266097784042},{\\\"lat\\\":54.69833143928,\\\"lng\\\":25.265550613403},{\\\"lat\\\":54.698319039289,\\\"lng\\\":25.265030264854},{\\\"lat\\\":54.697928437622,\\\"lng\\\":25.264493823051},{\\\"lat\\\":54.697463430735,\\\"lng\\\":25.264370441437},{\\\"lat\\\":54.697193724298,\\\"lng\\\":25.264370441437},{\\\"lat\\\":54.696914715753,\\\"lng\\\":25.264568924904},{\\\"lat\\\":54.696635705289,\\\"lng\\\":25.265089273453},{\\\"lat\\\":54.696381494084,\\\"lng\\\":25.265486240387},{\\\"lat\\\":54.69634739246,\\\"lng\\\":25.266414284706},{\\\"lat\\\":54.696480698646,\\\"lng\\\":25.267803668976},{\\\"lat\\\":54.697249525777,\\\"lng\\\":25.26779294014},{\\\"lat\\\":54.697621533675,\\\"lng\\\":25.267733931541},{\\\"lat\\\":54.6980772387,\\\"lng\\\":25.267615914345}]\",\"polygon_color\":\"#d000df\",\"created_at\":\"2018-05-29 14:46:26\",\"updated_at\":\"2018-05-31 19:13:42\",\"title\":\"Ofisas\"},{\"id\":\"23\",\"user_id\":\"1\",\"group_id\":0,\"active\":\"1\",\"name\":\"Home\",\"coordinates\":\"[{\\\"lat\\\":54.716806315371,\\\"lng\\\":25.214910507202},{\\\"lat\\\":54.716930258603,\\\"lng\\\":25.216423273087},{\\\"lat\\\":54.717698698179,\\\"lng\\\":25.216509103775},{\\\"lat\\\":54.718268821355,\\\"lng\\\":25.2152967453},{\\\"lat\\\":54.71821304879,\\\"lng\\\":25.213730335236},{\\\"lat\\\":54.716855892709,\\\"lng\\\":25.214052200317}]\",\"polygon_color\":\"#b741bf\",\"created_at\":\"2018-05-29 14:47:16\",\"updated_at\":\"2018-05-29 14:47:16\",\"title\":\"Home\"},{\"id\":\"24\",\"user_id\":\"1\",\"group_id\":\"3\",\"active\":\"1\",\"name\":\"Cross\",\"coordinates\":\"[{\\\"lat\\\":54.709579769991,\\\"lng\\\":25.240960121155},{\\\"lat\\\":54.709492994039,\\\"lng\\\":25.23893237114},{\\\"lat\\\":54.708228523397,\\\"lng\\\":25.239146947861},{\\\"lat\\\":54.708439271242,\\\"lng\\\":25.242161750793},{\\\"lat\\\":54.709623157897,\\\"lng\\\":25.241560935974}]\",\"polygon_color\":\"#546cab\",\"created_at\":\"2018-05-29 14:49:29\",\"updated_at\":\"2018-06-15 14:35:31\",\"title\":\"Cross\"}],\"default\":[]}]},{\"type\":\"geofence_out\",\"title\":\"Geofence Out\",\"attributes\":[{\"name\":\"geofences\",\"title\":\"Geofences\",\"type\":\"multiselect\",\"options\":[{\"id\":\"9\",\"user_id\":\"1\",\"group_id\":0,\"active\":\"1\",\"name\":\"Geo\",\"coordinates\":\"[{\\\"lat\\\":55.670614828967,\\\"lng\\\":12.628784179688},{\\\"lat\\\":55.672260536717,\\\"lng\\\":12.610416412354},{\\\"lat\\\":55.665193184436,\\\"lng\\\":12.60046005249},{\\\"lat\\\":55.664805895369,\\\"lng\\\":12.615222930908},{\\\"lat\\\":55.659480282028,\\\"lng\\\":12.627410888672}]\",\"polygon_color\":\"#d000df\",\"created_at\":\"2017-07-18 06:16:03\",\"updated_at\":\"2018-05-23 21:28:30\",\"title\":\"Geo\"},{\"id\":\"11\",\"user_id\":\"1\",\"group_id\":0,\"active\":\"1\",\"name\":\"Test\",\"coordinates\":\"[{\\\"lat\\\":55.689197622339,\\\"lng\\\":12.431030273438},{\\\"lat\\\":55.690358753803,\\\"lng\\\":12.420558929443},{\\\"lat\\\":55.692874420351,\\\"lng\\\":12.398071289062},{\\\"lat\\\":55.697131333455,\\\"lng\\\":12.383480072021},{\\\"lat\\\":55.700033508435,\\\"lng\\\":12.369403839111},{\\\"lat\\\":55.683197893777,\\\"lng\\\":12.335243225098},{\\\"lat\\\":55.664031305736,\\\"lng\\\":12.31481552124},{\\\"lat\\\":55.642917836815,\\\"lng\\\":12.340393066406},{\\\"lat\\\":55.643692844267,\\\"lng\\\":12.382278442383},{\\\"lat\\\":55.642917836815,\\\"lng\\\":12.400131225586},{\\\"lat\\\":55.642724082556,\\\"lng\\\":12.417812347412},{\\\"lat\\\":55.647083321567,\\\"lng\\\":12.421760559082},{\\\"lat\\\":55.657930876627,\\\"lng\\\":12.421245574951},{\\\"lat\\\":55.669356299863,\\\"lng\\\":12.420644760132},{\\\"lat\\\":55.674777367624,\\\"lng\\\":12.421073913574},{\\\"lat\\\":55.678165153556,\\\"lng\\\":12.423820495605},{\\\"lat\\\":55.682907560981,\\\"lng\\\":12.42639541626}]\",\"polygon_color\":\"#65c999\",\"created_at\":\"2017-09-11 11:43:14\",\"updated_at\":\"2018-05-23 21:28:12\",\"title\":\"Test\"},{\"id\":\"12\",\"user_id\":\"1\",\"group_id\":0,\"active\":\"1\",\"name\":\"Testinh\",\"coordinates\":\"[{\\\"lat\\\":49.213865232086,\\\"lng\\\":18.735176324844},{\\\"lat\\\":49.21379865151,\\\"lng\\\":18.738502264023},{\\\"lat\\\":49.212782410539,\\\"lng\\\":18.735128045082}]\",\"polygon_color\":\"#a660ab\",\"created_at\":\"2017-09-18 16:19:38\",\"updated_at\":\"2017-09-18 16:19:38\",\"title\":\"Testinh\"},{\"id\":\"13\",\"user_id\":\"1\",\"group_id\":0,\"active\":\"1\",\"name\":\"Geo2\",\"coordinates\":\"[{\\\"lat\\\":51.488544993181,\\\"lng\\\":3.9162826538086},{\\\"lat\\\":51.495064730144,\\\"lng\\\":3.8553428649902},{\\\"lat\\\":51.474219674077,\\\"lng\\\":3.8503646850586},{\\\"lat\\\":51.468766318141,\\\"lng\\\":3.9114761352539}]\",\"polygon_color\":\"#d000df\",\"created_at\":\"2017-09-19 11:50:01\",\"updated_at\":\"2017-09-19 11:50:01\",\"title\":\"Geo2\"},{\"id\":\"19\",\"user_id\":\"1\",\"group_id\":0,\"active\":\"1\",\"name\":\"labas\",\"coordinates\":\"[{\\\"lat\\\":47.871670554416,\\\"lng\\\":3.1394200399518},{\\\"lat\\\":53.748294056322,\\\"lng\\\":2.8318025171757},{\\\"lat\\\":53.903922960948,\\\"lng\\\":10.478288568556},{\\\"lat\\\":48.6904942835,\\\"lng\\\":11.313246823847}]\",\"polygon_color\":\"#ff6790\",\"created_at\":\"2017-11-14 17:23:44\",\"updated_at\":\"2017-11-14 17:23:44\",\"title\":\"labas\"},{\"id\":\"20\",\"user_id\":\"1\",\"group_id\":0,\"active\":\"0\",\"name\":\"LIETUVA !\",\"coordinates\":\"[{\\\"lat\\\":55.053202585371,\\\"lng\\\":22.582397460938},{\\\"lat\\\":54.961848039842,\\\"lng\\\":22.758178710938},{\\\"lat\\\":54.826007999095,\\\"lng\\\":22.922973632813},{\\\"lat\\\":54.724620194924,\\\"lng\\\":22.780151367188},{\\\"lat\\\":54.549765673898,\\\"lng\\\":22.659301757812},{\\\"lat\\\":54.399748155638,\\\"lng\\\":22.780151367188},{\\\"lat\\\":54.338946534621,\\\"lng\\\":23.04931640625},{\\\"lat\\\":54.25559837127,\\\"lng\\\":23.3349609375},{\\\"lat\\\":54.130259578795,\\\"lng\\\":23.516235351563},{\\\"lat\\\":53.985165238685,\\\"lng\\\":23.48876953125},{\\\"lat\\\":53.933453949447,\\\"lng\\\":23.7744140625},{\\\"lat\\\":53.952853199669,\\\"lng\\\":24.08203125},{\\\"lat\\\":53.910810087254,\\\"lng\\\":24.41162109375},{\\\"lat\\\":54.001311864648,\\\"lng\\\":24.570922851563},{\\\"lat\\\":53.998083040359,\\\"lng\\\":24.78515625},{\\\"lat\\\":54.149567212541,\\\"lng\\\":24.845581054688},{\\\"lat\\\":54.14634989866,\\\"lng\\\":25.0927734375},{\\\"lat\\\":54.262015759179,\\\"lng\\\":25.328979492188},{\\\"lat\\\":54.294087719276,\\\"lng\\\":25.499267578125},{\\\"lat\\\":54.139914520834,\\\"lng\\\":25.576171875},{\\\"lat\\\":54.159217654167,\\\"lng\\\":25.762939453125},{\\\"lat\\\":54.29088164657,\\\"lng\\\":25.713500976562},{\\\"lat\\\":54.310114339162,\\\"lng\\\":25.614624023438},{\\\"lat\\\":54.40614309032,\\\"lng\\\":25.609130859375},{\\\"lat\\\":54.597527852114,\\\"lng\\\":25.740966796875},{\\\"lat\\\":54.816513684853,\\\"lng\\\":25.762939453125},{\\\"lat\\\":54.955540054617,\\\"lng\\\":25.933227539063},{\\\"lat\\\":54.987070078949,\\\"lng\\\":26.202392578125},{\\\"lat\\\":55.141209644495,\\\"lng\\\":26.328735351562},{\\\"lat\\\":55.144349170977,\\\"lng\\\":26.614379882813},{\\\"lat\\\":55.288500557924,\\\"lng\\\":26.834106445313},{\\\"lat\\\":55.335393612016,\\\"lng\\\":26.6748046875},{\\\"lat\\\":55.329144408405,\\\"lng\\\":26.444091796875},{\\\"lat\\\":55.410307210052,\\\"lng\\\":26.531982421875},{\\\"lat\\\":55.50686080246,\\\"lng\\\":26.52099609375},{\\\"lat\\\":55.575239380091,\\\"lng\\\":26.641845703125},{\\\"lat\\\":55.67758441109,\\\"lng\\\":26.641845703125},{\\\"lat\\\":55.71164005362,\\\"lng\\\":26.356201171875},{\\\"lat\\\":55.862982311976,\\\"lng\\\":26.174926757812},{\\\"lat\\\":56.087362474951,\\\"lng\\\":25.724487304688},{\\\"lat\\\":56.16084725409,\\\"lng\\\":25.504760742188},{\\\"lat\\\":56.185310993255,\\\"lng\\\":25.087280273438},{\\\"lat\\\":56.301301022161,\\\"lng\\\":24.993896484375},{\\\"lat\\\":56.441240391404,\\\"lng\\\":24.889526367188},{\\\"lat\\\":56.380460326029,\\\"lng\\\":24.664306640625},{\\\"lat\\\":56.283010159802,\\\"lng\\\":24.554443359375},{\\\"lat\\\":56.270811388415,\\\"lng\\\":24.19189453125},{\\\"lat\\\":56.313490068258,\\\"lng\\\":24.021606445312},{\\\"lat\\\":56.337856494411,\\\"lng\\\":23.812866210938},{\\\"lat\\\":56.368292660394,\\\"lng\\\":23.675537109375},{\\\"lat\\\":56.334811541652,\\\"lng\\\":23.527221679688},{\\\"lat\\\":56.377418773876,\\\"lng\\\":23.367919921875},{\\\"lat\\\":56.362207370444,\\\"lng\\\":23.175659179688},{\\\"lat\\\":56.295205040556,\\\"lng\\\":23.082275390625},{\\\"lat\\\":56.398704540115,\\\"lng\\\":22.955932617188},{\\\"lat\\\":56.359164361149,\\\"lng\\\":22.703247070313},{\\\"lat\\\":56.401744392759,\\\"lng\\\":22.439575195313},{\\\"lat\\\":56.42605447605,\\\"lng\\\":22.033081054688},{\\\"lat\\\":56.32872090718,\\\"lng\\\":21.796875},{\\\"lat\\\":56.289108086465,\\\"lng\\\":21.478271484375},{\\\"lat\\\":56.191424492755,\\\"lng\\\":21.264038085938},{\\\"lat\\\":56.096555750568,\\\"lng\\\":21.192626953125},{\\\"lat\\\":56.072035471801,\\\"lng\\\":21.044311523438},{\\\"lat\\\":55.80128097118,\\\"lng\\\":20.994873046875},{\\\"lat\\\":55.297883605106,\\\"lng\\\":20.8740234375},{\\\"lat\\\":55.244683663497,\\\"lng\\\":21.170654296875},{\\\"lat\\\":55.29475616922,\\\"lng\\\":21.37939453125},{\\\"lat\\\":55.203953257859,\\\"lng\\\":21.522216796875},{\\\"lat\\\":55.100373319786,\\\"lng\\\":21.901245117188}]\",\"polygon_color\":\"#fa1039\",\"created_at\":\"2017-11-15 06:26:52\",\"updated_at\":\"2018-03-12 15:27:37\",\"title\":\"LIETUVA !\"},{\"id\":\"21\",\"user_id\":\"1\",\"group_id\":0,\"active\":\"1\",\"name\":\"White test\",\"coordinates\":\"[{\\\"lat\\\":54.007768761935,\\\"lng\\\":20.2587890625},{\\\"lat\\\":53.80065082633,\\\"lng\\\":16.0400390625},{\\\"lat\\\":52.616390233045,\\\"lng\\\":15.2490234375},{\\\"lat\\\":52.456009392641,\\\"lng\\\":20.8740234375},{\\\"lat\\\":53.670680193473,\\\"lng\\\":21.3134765625}]\",\"polygon_color\":\"#ffffff\",\"created_at\":\"2018-05-09 16:06:36\",\"updated_at\":\"2018-05-09 16:06:36\",\"title\":\"White test\"},{\"id\":\"22\",\"user_id\":\"1\",\"group_id\":0,\"active\":\"1\",\"name\":\"Ofisas\",\"coordinates\":\"[{\\\"lat\\\":54.698148539024,\\\"lng\\\":25.267063379288},{\\\"lat\\\":54.698278739291,\\\"lng\\\":25.266097784042},{\\\"lat\\\":54.69833143928,\\\"lng\\\":25.265550613403},{\\\"lat\\\":54.698319039289,\\\"lng\\\":25.265030264854},{\\\"lat\\\":54.697928437622,\\\"lng\\\":25.264493823051},{\\\"lat\\\":54.697463430735,\\\"lng\\\":25.264370441437},{\\\"lat\\\":54.697193724298,\\\"lng\\\":25.264370441437},{\\\"lat\\\":54.696914715753,\\\"lng\\\":25.264568924904},{\\\"lat\\\":54.696635705289,\\\"lng\\\":25.265089273453},{\\\"lat\\\":54.696381494084,\\\"lng\\\":25.265486240387},{\\\"lat\\\":54.69634739246,\\\"lng\\\":25.266414284706},{\\\"lat\\\":54.696480698646,\\\"lng\\\":25.267803668976},{\\\"lat\\\":54.697249525777,\\\"lng\\\":25.26779294014},{\\\"lat\\\":54.697621533675,\\\"lng\\\":25.267733931541},{\\\"lat\\\":54.6980772387,\\\"lng\\\":25.267615914345}]\",\"polygon_color\":\"#d000df\",\"created_at\":\"2018-05-29 14:46:26\",\"updated_at\":\"2018-05-31 19:13:42\",\"title\":\"Ofisas\"},{\"id\":\"23\",\"user_id\":\"1\",\"group_id\":0,\"active\":\"1\",\"name\":\"Home\",\"coordinates\":\"[{\\\"lat\\\":54.716806315371,\\\"lng\\\":25.214910507202},{\\\"lat\\\":54.716930258603,\\\"lng\\\":25.216423273087},{\\\"lat\\\":54.717698698179,\\\"lng\\\":25.216509103775},{\\\"lat\\\":54.718268821355,\\\"lng\\\":25.2152967453},{\\\"lat\\\":54.71821304879,\\\"lng\\\":25.213730335236},{\\\"lat\\\":54.716855892709,\\\"lng\\\":25.214052200317}]\",\"polygon_color\":\"#b741bf\",\"created_at\":\"2018-05-29 14:47:16\",\"updated_at\":\"2018-05-29 14:47:16\",\"title\":\"Home\"},{\"id\":\"24\",\"user_id\":\"1\",\"group_id\":\"3\",\"active\":\"1\",\"name\":\"Cross\",\"coordinates\":\"[{\\\"lat\\\":54.709579769991,\\\"lng\\\":25.240960121155},{\\\"lat\\\":54.709492994039,\\\"lng\\\":25.23893237114},{\\\"lat\\\":54.708228523397,\\\"lng\\\":25.239146947861},{\\\"lat\\\":54.708439271242,\\\"lng\\\":25.242161750793},{\\\"lat\\\":54.709623157897,\\\"lng\\\":25.241560935974}]\",\"polygon_color\":\"#546cab\",\"created_at\":\"2018-05-29 14:49:29\",\"updated_at\":\"2018-06-15 14:35:31\",\"title\":\"Cross\"}],\"default\":[]}]},{\"type\":\"geofence_inout\",\"title\":\"Geofence In\\/Out\",\"attributes\":[{\"name\":\"geofences\",\"title\":\"Geofences\",\"type\":\"multiselect\",\"options\":[{\"id\":\"9\",\"user_id\":\"1\",\"group_id\":0,\"active\":\"1\",\"name\":\"Geo\",\"coordinates\":\"[{\\\"lat\\\":55.670614828967,\\\"lng\\\":12.628784179688},{\\\"lat\\\":55.672260536717,\\\"lng\\\":12.610416412354},{\\\"lat\\\":55.665193184436,\\\"lng\\\":12.60046005249},{\\\"lat\\\":55.664805895369,\\\"lng\\\":12.615222930908},{\\\"lat\\\":55.659480282028,\\\"lng\\\":12.627410888672}]\",\"polygon_color\":\"#d000df\",\"created_at\":\"2017-07-18 06:16:03\",\"updated_at\":\"2018-05-23 21:28:30\",\"title\":\"Geo\"},{\"id\":\"11\",\"user_id\":\"1\",\"group_id\":0,\"active\":\"1\",\"name\":\"Test\",\"coordinates\":\"[{\\\"lat\\\":55.689197622339,\\\"lng\\\":12.431030273438},{\\\"lat\\\":55.690358753803,\\\"lng\\\":12.420558929443},{\\\"lat\\\":55.692874420351,\\\"lng\\\":12.398071289062},{\\\"lat\\\":55.697131333455,\\\"lng\\\":12.383480072021},{\\\"lat\\\":55.700033508435,\\\"lng\\\":12.369403839111},{\\\"lat\\\":55.683197893777,\\\"lng\\\":12.335243225098},{\\\"lat\\\":55.664031305736,\\\"lng\\\":12.31481552124},{\\\"lat\\\":55.642917836815,\\\"lng\\\":12.340393066406},{\\\"lat\\\":55.643692844267,\\\"lng\\\":12.382278442383},{\\\"lat\\\":55.642917836815,\\\"lng\\\":12.400131225586},{\\\"lat\\\":55.642724082556,\\\"lng\\\":12.417812347412},{\\\"lat\\\":55.647083321567,\\\"lng\\\":12.421760559082},{\\\"lat\\\":55.657930876627,\\\"lng\\\":12.421245574951},{\\\"lat\\\":55.669356299863,\\\"lng\\\":12.420644760132},{\\\"lat\\\":55.674777367624,\\\"lng\\\":12.421073913574},{\\\"lat\\\":55.678165153556,\\\"lng\\\":12.423820495605},{\\\"lat\\\":55.682907560981,\\\"lng\\\":12.42639541626}]\",\"polygon_color\":\"#65c999\",\"created_at\":\"2017-09-11 11:43:14\",\"updated_at\":\"2018-05-23 21:28:12\",\"title\":\"Test\"},{\"id\":\"12\",\"user_id\":\"1\",\"group_id\":0,\"active\":\"1\",\"name\":\"Testinh\",\"coordinates\":\"[{\\\"lat\\\":49.213865232086,\\\"lng\\\":18.735176324844},{\\\"lat\\\":49.21379865151,\\\"lng\\\":18.738502264023},{\\\"lat\\\":49.212782410539,\\\"lng\\\":18.735128045082}]\",\"polygon_color\":\"#a660ab\",\"created_at\":\"2017-09-18 16:19:38\",\"updated_at\":\"2017-09-18 16:19:38\",\"title\":\"Testinh\"},{\"id\":\"13\",\"user_id\":\"1\",\"group_id\":0,\"active\":\"1\",\"name\":\"Geo2\",\"coordinates\":\"[{\\\"lat\\\":51.488544993181,\\\"lng\\\":3.9162826538086},{\\\"lat\\\":51.495064730144,\\\"lng\\\":3.8553428649902},{\\\"lat\\\":51.474219674077,\\\"lng\\\":3.8503646850586},{\\\"lat\\\":51.468766318141,\\\"lng\\\":3.9114761352539}]\",\"polygon_color\":\"#d000df\",\"created_at\":\"2017-09-19 11:50:01\",\"updated_at\":\"2017-09-19 11:50:01\",\"title\":\"Geo2\"},{\"id\":\"19\",\"user_id\":\"1\",\"group_id\":0,\"active\":\"1\",\"name\":\"labas\",\"coordinates\":\"[{\\\"lat\\\":47.871670554416,\\\"lng\\\":3.1394200399518},{\\\"lat\\\":53.748294056322,\\\"lng\\\":2.8318025171757},{\\\"lat\\\":53.903922960948,\\\"lng\\\":10.478288568556},{\\\"lat\\\":48.6904942835,\\\"lng\\\":11.313246823847}]\",\"polygon_color\":\"#ff6790\",\"created_at\":\"2017-11-14 17:23:44\",\"updated_at\":\"2017-11-14 17:23:44\",\"title\":\"labas\"},{\"id\":\"20\",\"user_id\":\"1\",\"group_id\":0,\"active\":\"0\",\"name\":\"LIETUVA !\",\"coordinates\":\"[{\\\"lat\\\":55.053202585371,\\\"lng\\\":22.582397460938},{\\\"lat\\\":54.961848039842,\\\"lng\\\":22.758178710938},{\\\"lat\\\":54.826007999095,\\\"lng\\\":22.922973632813},{\\\"lat\\\":54.724620194924,\\\"lng\\\":22.780151367188},{\\\"lat\\\":54.549765673898,\\\"lng\\\":22.659301757812},{\\\"lat\\\":54.399748155638,\\\"lng\\\":22.780151367188},{\\\"lat\\\":54.338946534621,\\\"lng\\\":23.04931640625},{\\\"lat\\\":54.25559837127,\\\"lng\\\":23.3349609375},{\\\"lat\\\":54.130259578795,\\\"lng\\\":23.516235351563},{\\\"lat\\\":53.985165238685,\\\"lng\\\":23.48876953125},{\\\"lat\\\":53.933453949447,\\\"lng\\\":23.7744140625},{\\\"lat\\\":53.952853199669,\\\"lng\\\":24.08203125},{\\\"lat\\\":53.910810087254,\\\"lng\\\":24.41162109375},{\\\"lat\\\":54.001311864648,\\\"lng\\\":24.570922851563},{\\\"lat\\\":53.998083040359,\\\"lng\\\":24.78515625},{\\\"lat\\\":54.149567212541,\\\"lng\\\":24.845581054688},{\\\"lat\\\":54.14634989866,\\\"lng\\\":25.0927734375},{\\\"lat\\\":54.262015759179,\\\"lng\\\":25.328979492188},{\\\"lat\\\":54.294087719276,\\\"lng\\\":25.499267578125},{\\\"lat\\\":54.139914520834,\\\"lng\\\":25.576171875},{\\\"lat\\\":54.159217654167,\\\"lng\\\":25.762939453125},{\\\"lat\\\":54.29088164657,\\\"lng\\\":25.713500976562},{\\\"lat\\\":54.310114339162,\\\"lng\\\":25.614624023438},{\\\"lat\\\":54.40614309032,\\\"lng\\\":25.609130859375},{\\\"lat\\\":54.597527852114,\\\"lng\\\":25.740966796875},{\\\"lat\\\":54.816513684853,\\\"lng\\\":25.762939453125},{\\\"lat\\\":54.955540054617,\\\"lng\\\":25.933227539063},{\\\"lat\\\":54.987070078949,\\\"lng\\\":26.202392578125},{\\\"lat\\\":55.141209644495,\\\"lng\\\":26.328735351562},{\\\"lat\\\":55.144349170977,\\\"lng\\\":26.614379882813},{\\\"lat\\\":55.288500557924,\\\"lng\\\":26.834106445313},{\\\"lat\\\":55.335393612016,\\\"lng\\\":26.6748046875},{\\\"lat\\\":55.329144408405,\\\"lng\\\":26.444091796875},{\\\"lat\\\":55.410307210052,\\\"lng\\\":26.531982421875},{\\\"lat\\\":55.50686080246,\\\"lng\\\":26.52099609375},{\\\"lat\\\":55.575239380091,\\\"lng\\\":26.641845703125},{\\\"lat\\\":55.67758441109,\\\"lng\\\":26.641845703125},{\\\"lat\\\":55.71164005362,\\\"lng\\\":26.356201171875},{\\\"lat\\\":55.862982311976,\\\"lng\\\":26.174926757812},{\\\"lat\\\":56.087362474951,\\\"lng\\\":25.724487304688},{\\\"lat\\\":56.16084725409,\\\"lng\\\":25.504760742188},{\\\"lat\\\":56.185310993255,\\\"lng\\\":25.087280273438},{\\\"lat\\\":56.301301022161,\\\"lng\\\":24.993896484375},{\\\"lat\\\":56.441240391404,\\\"lng\\\":24.889526367188},{\\\"lat\\\":56.380460326029,\\\"lng\\\":24.664306640625},{\\\"lat\\\":56.283010159802,\\\"lng\\\":24.554443359375},{\\\"lat\\\":56.270811388415,\\\"lng\\\":24.19189453125},{\\\"lat\\\":56.313490068258,\\\"lng\\\":24.021606445312},{\\\"lat\\\":56.337856494411,\\\"lng\\\":23.812866210938},{\\\"lat\\\":56.368292660394,\\\"lng\\\":23.675537109375},{\\\"lat\\\":56.334811541652,\\\"lng\\\":23.527221679688},{\\\"lat\\\":56.377418773876,\\\"lng\\\":23.367919921875},{\\\"lat\\\":56.362207370444,\\\"lng\\\":23.175659179688},{\\\"lat\\\":56.295205040556,\\\"lng\\\":23.082275390625},{\\\"lat\\\":56.398704540115,\\\"lng\\\":22.955932617188},{\\\"lat\\\":56.359164361149,\\\"lng\\\":22.703247070313},{\\\"lat\\\":56.401744392759,\\\"lng\\\":22.439575195313},{\\\"lat\\\":56.42605447605,\\\"lng\\\":22.033081054688},{\\\"lat\\\":56.32872090718,\\\"lng\\\":21.796875},{\\\"lat\\\":56.289108086465,\\\"lng\\\":21.478271484375},{\\\"lat\\\":56.191424492755,\\\"lng\\\":21.264038085938},{\\\"lat\\\":56.096555750568,\\\"lng\\\":21.192626953125},{\\\"lat\\\":56.072035471801,\\\"lng\\\":21.044311523438},{\\\"lat\\\":55.80128097118,\\\"lng\\\":20.994873046875},{\\\"lat\\\":55.297883605106,\\\"lng\\\":20.8740234375},{\\\"lat\\\":55.244683663497,\\\"lng\\\":21.170654296875},{\\\"lat\\\":55.29475616922,\\\"lng\\\":21.37939453125},{\\\"lat\\\":55.203953257859,\\\"lng\\\":21.522216796875},{\\\"lat\\\":55.100373319786,\\\"lng\\\":21.901245117188}]\",\"polygon_color\":\"#fa1039\",\"created_at\":\"2017-11-15 06:26:52\",\"updated_at\":\"2018-03-12 15:27:37\",\"title\":\"LIETUVA !\"},{\"id\":\"21\",\"user_id\":\"1\",\"group_id\":0,\"active\":\"1\",\"name\":\"White test\",\"coordinates\":\"[{\\\"lat\\\":54.007768761935,\\\"lng\\\":20.2587890625},{\\\"lat\\\":53.80065082633,\\\"lng\\\":16.0400390625},{\\\"lat\\\":52.616390233045,\\\"lng\\\":15.2490234375},{\\\"lat\\\":52.456009392641,\\\"lng\\\":20.8740234375},{\\\"lat\\\":53.670680193473,\\\"lng\\\":21.3134765625}]\",\"polygon_color\":\"#ffffff\",\"created_at\":\"2018-05-09 16:06:36\",\"updated_at\":\"2018-05-09 16:06:36\",\"title\":\"White test\"},{\"id\":\"22\",\"user_id\":\"1\",\"group_id\":0,\"active\":\"1\",\"name\":\"Ofisas\",\"coordinates\":\"[{\\\"lat\\\":54.698148539024,\\\"lng\\\":25.267063379288},{\\\"lat\\\":54.698278739291,\\\"lng\\\":25.266097784042},{\\\"lat\\\":54.69833143928,\\\"lng\\\":25.265550613403},{\\\"lat\\\":54.698319039289,\\\"lng\\\":25.265030264854},{\\\"lat\\\":54.697928437622,\\\"lng\\\":25.264493823051},{\\\"lat\\\":54.697463430735,\\\"lng\\\":25.264370441437},{\\\"lat\\\":54.697193724298,\\\"lng\\\":25.264370441437},{\\\"lat\\\":54.696914715753,\\\"lng\\\":25.264568924904},{\\\"lat\\\":54.696635705289,\\\"lng\\\":25.265089273453},{\\\"lat\\\":54.696381494084,\\\"lng\\\":25.265486240387},{\\\"lat\\\":54.69634739246,\\\"lng\\\":25.266414284706},{\\\"lat\\\":54.696480698646,\\\"lng\\\":25.267803668976},{\\\"lat\\\":54.697249525777,\\\"lng\\\":25.26779294014},{\\\"lat\\\":54.697621533675,\\\"lng\\\":25.267733931541},{\\\"lat\\\":54.6980772387,\\\"lng\\\":25.267615914345}]\",\"polygon_color\":\"#d000df\",\"created_at\":\"2018-05-29 14:46:26\",\"updated_at\":\"2018-05-31 19:13:42\",\"title\":\"Ofisas\"},{\"id\":\"23\",\"user_id\":\"1\",\"group_id\":0,\"active\":\"1\",\"name\":\"Home\",\"coordinates\":\"[{\\\"lat\\\":54.716806315371,\\\"lng\\\":25.214910507202},{\\\"lat\\\":54.716930258603,\\\"lng\\\":25.216423273087},{\\\"lat\\\":54.717698698179,\\\"lng\\\":25.216509103775},{\\\"lat\\\":54.718268821355,\\\"lng\\\":25.2152967453},{\\\"lat\\\":54.71821304879,\\\"lng\\\":25.213730335236},{\\\"lat\\\":54.716855892709,\\\"lng\\\":25.214052200317}]\",\"polygon_color\":\"#b741bf\",\"created_at\":\"2018-05-29 14:47:16\",\"updated_at\":\"2018-05-29 14:47:16\",\"title\":\"Home\"},{\"id\":\"24\",\"user_id\":\"1\",\"group_id\":\"3\",\"active\":\"1\",\"name\":\"Cross\",\"coordinates\":\"[{\\\"lat\\\":54.709579769991,\\\"lng\\\":25.240960121155},{\\\"lat\\\":54.709492994039,\\\"lng\\\":25.23893237114},{\\\"lat\\\":54.708228523397,\\\"lng\\\":25.239146947861},{\\\"lat\\\":54.708439271242,\\\"lng\\\":25.242161750793},{\\\"lat\\\":54.709623157897,\\\"lng\\\":25.241560935974}]\",\"polygon_color\":\"#546cab\",\"created_at\":\"2018-05-29 14:49:29\",\"updated_at\":\"2018-06-15 14:35:31\",\"title\":\"Cross\"}],\"default\":[]}]},{\"type\":\"custom\",\"title\":\"Custom events\",\"attributes\":[{\"name\":\"events_custom\",\"title\":\"Event\",\"type\":\"multiselect\",\"options\":[],\"default\":[],\"description\":\"Custom events can be defined at Setup->Events tab.\"}]}],\"schedules\":[{\"id\":\"monday\",\"title\":\"Monday\",\"items\":[{\"id\":\"00:00\",\"title\":\"12:00 AM\",\"active\":true},{\"id\":\"00:15\",\"title\":\"12:15 AM\",\"active\":true},{\"id\":\"00:30\",\"title\":\"12:30 AM\",\"active\":true},{\"id\":\"00:45\",\"title\":\"12:45 AM\",\"active\":true},{\"id\":\"01:00\",\"title\":\"01:00 AM\",\"active\":true},{\"id\":\"01:15\",\"title\":\"01:15 AM\",\"active\":true},{\"id\":\"01:30\",\"title\":\"01:30 AM\",\"active\":true},{\"id\":\"01:45\",\"title\":\"01:45 AM\",\"active\":true},{\"id\":\"02:00\",\"title\":\"02:00 AM\",\"active\":true},{\"id\":\"02:15\",\"title\":\"02:15 AM\",\"active\":true},{\"id\":\"02:30\",\"title\":\"02:30 AM\",\"active\":true},{\"id\":\"02:45\",\"title\":\"02:45 AM\",\"active\":true},{\"id\":\"03:00\",\"title\":\"03:00 AM\",\"active\":true},{\"id\":\"03:15\",\"title\":\"03:15 AM\",\"active\":true},{\"id\":\"03:30\",\"title\":\"03:30 AM\",\"active\":true},{\"id\":\"03:45\",\"title\":\"03:45 AM\",\"active\":true},{\"id\":\"22:00\",\"title\":\"10:00 PM\",\"active\":true},{\"id\":\"22:15\",\"title\":\"10:15 PM\",\"active\":true},{\"id\":\"22:30\",\"title\":\"10:30 PM\",\"active\":true},{\"id\":\"22:45\",\"title\":\"10:45 PM\",\"active\":true},{\"id\":\"23:00\",\"title\":\"11:00 PM\",\"active\":true},{\"id\":\"23:15\",\"title\":\"11:15 PM\",\"active\":true},{\"id\":\"23:30\",\"title\":\"11:30 PM\",\"active\":true},{\"id\":\"23:45\",\"title\":\"11:45 PM\",\"active\":true}]},{\"id\":\"tuesday\",\"title\":\"Tuesday\",\"items\":[{\"id\":\"00:00\",\"title\":\"12:00 AM\",\"active\":true},{\"id\":\"00:15\",\"title\":\"12:15 AM\",\"active\":true},{\"id\":\"00:30\",\"title\":\"12:30 AM\",\"active\":true},{\"id\":\"00:45\",\"title\":\"12:45 AM\",\"active\":true},{\"id\":\"01:00\",\"title\":\"01:00 AM\",\"active\":true},{\"id\":\"01:15\",\"title\":\"01:15 AM\",\"active\":true},{\"id\":\"01:30\",\"title\":\"01:30 AM\",\"active\":true},{\"id\":\"01:45\",\"title\":\"01:45 AM\",\"active\":true},{\"id\":\"02:00\",\"title\":\"02:00 AM\",\"active\":true},{\"id\":\"21:45\",\"title\":\"09:45 PM\",\"active\":true},{\"id\":\"22:00\",\"title\":\"10:00 PM\",\"active\":true},{\"id\":\"22:15\",\"title\":\"10:15 PM\",\"active\":true},{\"id\":\"22:30\",\"title\":\"10:30 PM\",\"active\":true},{\"id\":\"22:45\",\"title\":\"10:45 PM\",\"active\":true},{\"id\":\"23:00\",\"title\":\"11:00 PM\",\"active\":true},{\"id\":\"23:15\",\"title\":\"11:15 PM\",\"active\":true},{\"id\":\"23:30\",\"title\":\"11:30 PM\",\"active\":true},{\"id\":\"23:45\",\"title\":\"11:45 PM\",\"active\":true}]},{\"id\":\"wednesday\",\"title\":\"Wednesday\",\"items\":[{\"id\":\"00:00\",\"title\":\"12:00 AM\",\"active\":true},{\"id\":\"00:15\",\"title\":\"12:15 AM\",\"active\":true},{\"id\":\"00:30\",\"title\":\"12:30 AM\",\"active\":true},{\"id\":\"00:45\",\"title\":\"12:45 AM\",\"active\":true},{\"id\":\"01:00\",\"title\":\"01:00 AM\",\"active\":true},{\"id\":\"01:15\",\"title\":\"01:15 AM\",\"active\":true},{\"id\":\"01:30\",\"title\":\"01:30 AM\",\"active\":true},{\"id\":\"19:45\",\"title\":\"07:45 PM\",\"active\":true},{\"id\":\"20:00\",\"title\":\"08:00 PM\",\"active\":true},{\"id\":\"20:15\",\"title\":\"08:15 PM\",\"active\":true},{\"id\":\"20:30\",\"title\":\"08:30 PM\",\"active\":true},{\"id\":\"20:45\",\"title\":\"08:45 PM\",\"active\":true},{\"id\":\"21:00\",\"title\":\"09:00 PM\",\"active\":true},{\"id\":\"21:15\",\"title\":\"09:15 PM\",\"active\":true},{\"id\":\"21:30\",\"title\":\"09:30 PM\",\"active\":true},{\"id\":\"21:45\",\"title\":\"09:45 PM\",\"active\":true},{\"id\":\"22:00\",\"title\":\"10:00 PM\",\"active\":true},{\"id\":\"22:15\",\"title\":\"10:15 PM\",\"active\":true},{\"id\":\"22:30\",\"title\":\"10:30 PM\",\"active\":true},{\"id\":\"22:45\",\"title\":\"10:45 PM\",\"active\":true},{\"id\":\"23:00\",\"title\":\"11:00 PM\",\"active\":true},{\"id\":\"23:15\",\"title\":\"11:15 PM\",\"active\":true},{\"id\":\"23:30\",\"title\":\"11:30 PM\",\"active\":true},{\"id\":\"23:45\",\"title\":\"11:45 PM\",\"active\":true}]},{\"id\":\"thursday\",\"title\":\"Thursday\",\"items\":[{\"id\":\"00:00\",\"title\":\"12:00 AM\",\"active\":true},{\"id\":\"00:15\",\"title\":\"12:15 AM\",\"active\":true},{\"id\":\"00:30\",\"title\":\"12:30 AM\",\"active\":true},{\"id\":\"00:45\",\"title\":\"12:45 AM\",\"active\":true},{\"id\":\"01:00\",\"title\":\"01:00 AM\",\"active\":true},{\"id\":\"01:15\",\"title\":\"01:15 AM\",\"active\":true},{\"id\":\"20:15\",\"title\":\"08:15 PM\",\"active\":true},{\"id\":\"20:30\",\"title\":\"08:30 PM\",\"active\":true},{\"id\":\"20:45\",\"title\":\"08:45 PM\",\"active\":true},{\"id\":\"21:00\",\"title\":\"09:00 PM\",\"active\":true},{\"id\":\"21:15\",\"title\":\"09:15 PM\",\"active\":true},{\"id\":\"21:30\",\"title\":\"09:30 PM\",\"active\":true},{\"id\":\"21:45\",\"title\":\"09:45 PM\",\"active\":true},{\"id\":\"22:00\",\"title\":\"10:00 PM\",\"active\":true},{\"id\":\"22:15\",\"title\":\"10:15 PM\",\"active\":true},{\"id\":\"22:30\",\"title\":\"10:30 PM\",\"active\":true},{\"id\":\"22:45\",\"title\":\"10:45 PM\",\"active\":true},{\"id\":\"23:00\",\"title\":\"11:00 PM\",\"active\":true},{\"id\":\"23:15\",\"title\":\"11:15 PM\",\"active\":true},{\"id\":\"23:30\",\"title\":\"11:30 PM\",\"active\":true},{\"id\":\"23:45\",\"title\":\"11:45 PM\",\"active\":true}]},{\"id\":\"friday\",\"title\":\"Friday\",\"items\":[{\"id\":\"00:00\",\"title\":\"12:00 AM\",\"active\":true},{\"id\":\"00:15\",\"title\":\"12:15 AM\",\"active\":true},{\"id\":\"00:30\",\"title\":\"12:30 AM\",\"active\":true},{\"id\":\"00:45\",\"title\":\"12:45 AM\",\"active\":true},{\"id\":\"01:00\",\"title\":\"01:00 AM\",\"active\":true},{\"id\":\"01:15\",\"title\":\"01:15 AM\",\"active\":true},{\"id\":\"01:30\",\"title\":\"01:30 AM\",\"active\":true},{\"id\":\"20:30\",\"title\":\"08:30 PM\",\"active\":true},{\"id\":\"20:45\",\"title\":\"08:45 PM\",\"active\":true},{\"id\":\"21:00\",\"title\":\"09:00 PM\",\"active\":true},{\"id\":\"21:15\",\"title\":\"09:15 PM\",\"active\":true},{\"id\":\"21:30\",\"title\":\"09:30 PM\",\"active\":true},{\"id\":\"21:45\",\"title\":\"09:45 PM\",\"active\":true},{\"id\":\"22:00\",\"title\":\"10:00 PM\",\"active\":true},{\"id\":\"22:15\",\"title\":\"10:15 PM\",\"active\":true},{\"id\":\"22:30\",\"title\":\"10:30 PM\",\"active\":true},{\"id\":\"22:45\",\"title\":\"10:45 PM\",\"active\":true},{\"id\":\"23:00\",\"title\":\"11:00 PM\",\"active\":true},{\"id\":\"23:15\",\"title\":\"11:15 PM\",\"active\":true},{\"id\":\"23:30\",\"title\":\"11:30 PM\",\"active\":true},{\"id\":\"23:45\",\"title\":\"11:45 PM\",\"active\":true}]},{\"id\":\"saturday\",\"title\":\"Saturday\",\"items\":[{\"id\":\"00:00\",\"title\":\"12:00 AM\",\"active\":true},{\"id\":\"00:15\",\"title\":\"12:15 AM\",\"active\":true},{\"id\":\"00:30\",\"title\":\"12:30 AM\",\"active\":true},{\"id\":\"00:45\",\"title\":\"12:45 AM\",\"active\":true},{\"id\":\"01:00\",\"title\":\"01:00 AM\",\"active\":true},{\"id\":\"01:15\",\"title\":\"01:15 AM\",\"active\":true},{\"id\":\"21:45\",\"title\":\"09:45 PM\",\"active\":true},{\"id\":\"22:00\",\"title\":\"10:00 PM\",\"active\":true},{\"id\":\"22:15\",\"title\":\"10:15 PM\",\"active\":true},{\"id\":\"22:30\",\"title\":\"10:30 PM\",\"active\":true},{\"id\":\"22:45\",\"title\":\"10:45 PM\",\"active\":true},{\"id\":\"23:00\",\"title\":\"11:00 PM\",\"active\":true},{\"id\":\"23:15\",\"title\":\"11:15 PM\",\"active\":true},{\"id\":\"23:30\",\"title\":\"11:30 PM\",\"active\":true},{\"id\":\"23:45\",\"title\":\"11:45 PM\",\"active\":true}]},{\"id\":\"sunday\",\"title\":\"Sunday\",\"items\":[{\"id\":\"00:00\",\"title\":\"12:00 AM\",\"active\":true},{\"id\":\"00:15\",\"title\":\"12:15 AM\",\"active\":true},{\"id\":\"00:30\",\"title\":\"12:30 AM\",\"active\":true},{\"id\":\"00:45\",\"title\":\"12:45 AM\",\"active\":true},{\"id\":\"01:00\",\"title\":\"01:00 AM\",\"active\":true},{\"id\":\"01:15\",\"title\":\"01:15 AM\",\"active\":true},{\"id\":\"01:30\",\"title\":\"01:30 AM\",\"active\":true},{\"id\":\"23:30\",\"title\":\"11:30 PM\",\"active\":true},{\"id\":\"23:45\",\"title\":\"11:45 PM\",\"active\":true}]}],\"notifications\":[{\"active\":true,\"name\":\"sound\",\"title\":\"Sound notification\"},{\"active\":true,\"name\":\"push\",\"title\":\"Push notification\"},{\"active\":false,\"name\":\"email\",\"title\":\"Email notification\",\"input\":\"\",\"description\":\"For multiple emails separate them via semicolon ex.: user@example.com;user1@example.com\"},{\"active\":false,\"name\":\"sms\",\"title\":\"SMS notification\",\"input\":\"\",\"description\":\"For multiple mobile phones separate them via semicolon ex.: +440000000000;+440000000001\"},{\"active\":false,\"name\":\"webhook\",\"title\":\"Webhook notification\",\"input\":\"\",\"description\":\"The URL you would like event data posted to.\"}],\"alert_zones\":[{\"id\":1,\"value\":\"Zone in\",\"title\":\"Zone in\"},{\"id\":2,\"value\":\"Zone out\",\"title\":\"Zone out\"}],\"status\":1}";
        Gson jsonHandler = new Gson();
        tempData = jsonHandler.fromJson(rawJson, AlertsDataItem.class);
        return tempData;
    }
}
