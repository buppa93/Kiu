package com.domain.my.giuseppe.kiu.helper;

import android.app.Fragment;

/**
 * Created by UserOLD on 29/06/2016.
 */
public class FragmentSettings extends Fragment {

    /*private static FloatingActionButton fab;
    private int REQ_CODE = 1;
    private static final String TIME_START = "time_start";
    private static final String TIME_END = "time_end";
    private static final String MONDAY = "monday";
    private static final String THUESDAY = "thuesday";
    private static final String WEDNESDAY = "wednesday";
    private static final String THURSDAY = "thursday";
    private static final String FRIDAY = "friday";
    private static final String SATURDAY = "saturday";
    private static final String SUNDAY = "sunday";
    private static final String WEEKLY = "weekly";
    private static List<Availability> availabilityList = new ArrayList<Availability>();
    ArrayAdapter<Availability> availabilityArrayAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        fab = (FloatingActionButton) v.findViewById(R.id.floating_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddAvailability.class);
                startActivityForResult(intent,REQ_CODE);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_CODE && data!=null){

            Availability av = new Availability();
            av.setStart_time(data.getStringExtra(TIME_START));
            av.setEnd_time(data.getStringExtra(TIME_END));
            av.setMonday(data.getExtras().getBoolean(MONDAY));
            av.setThuesday(data.getExtras().getBoolean(THUESDAY));
            av.setWednesday(data.getExtras().getBoolean(WEDNESDAY));
            av.setThursday(data.getExtras().getBoolean(THURSDAY));
            av.setFriday(data.getExtras().getBoolean(FRIDAY));
            av.setSaturday(data.getExtras().getBoolean(SATURDAY));
            av.setSunday(data.getExtras().getBoolean(SUNDAY));
            av.setWeekly(data.getExtras().getBoolean(WEEKLY));

            Toast.makeText(getActivity(),"LUN: "+av.getMonday()+"\nMAR: "+av.getThuesday()+
                    "MER: "+av.getWednesday()+"\nGIO: "+av.getThursday()+"VEN: "+av.getFriday()+"\nSAB: "+av.getSaturday()+
                    "DOM: "+av.getSunday(),Toast.LENGTH_LONG).show();

            ListView listView = (ListView)getActivity().findViewById(R.id.list_availability);
            if(availabilityList.size()==0){
                availabilityArrayAdapter = new AvailabilityArrayAdapter();
                listView.setAdapter(availabilityArrayAdapter);
                availabilityList.add(av);
            }else {
                availabilityList.add(av);
                availabilityArrayAdapter.notifyDataSetChanged();
            }
        }
    }

    public class AvailabilityArrayAdapter extends ArrayAdapter<Availability> {

        public AvailabilityArrayAdapter() {
            super(getActivity(),R.layout.raw_availability,availabilityList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemview = convertView;
            if(itemview==null){
                itemview = getActivity().getLayoutInflater().inflate(R.layout.raw_availability,parent,false);
            }

            Availability currentAvailability = availabilityList.get(position);
            TextView from_text = (TextView) itemview.findViewById(R.id.from_text);
            TextView to_text = (TextView) itemview.findViewById(R.id.to_text);
            TextView weekly_text = (TextView) itemview.findViewById(R.id.weekly_text);
            ImageView image_monday = (ImageView) itemview.findViewById(R.id.image_monday_raw);
            ImageView image_thuesday = (ImageView) itemview.findViewById(R.id.image_thuesday_raw);
            ImageView image_wednesday = (ImageView) itemview.findViewById(R.id.image_wednesday_raw);
            ImageView image_thursday = (ImageView) itemview.findViewById(R.id.image_thursday_raw);
            ImageView image_friday = (ImageView) itemview.findViewById(R.id.image_friday_raw);
            ImageView image_saturday = (ImageView) itemview.findViewById(R.id.image_saturday_raw);
            ImageView image_sunday = (ImageView) itemview.findViewById(R.id.image_sunday_raw);

            from_text.setText(currentAvailability.getStart_time());
            to_text.setText(currentAvailability.getEnd_time());

            if(currentAvailability.getMonday()){
                image_monday.setBackgroundColor(Color.GREEN);
            }
            if (currentAvailability.getThuesday()){
                image_thuesday.setBackgroundColor(Color.GREEN);
            }
            if (currentAvailability.getWednesday()){
                image_wednesday.setBackgroundColor(Color.GREEN);
            }
            if (currentAvailability.getThursday()){
                image_thursday.setBackgroundColor(Color.GREEN);
            }
            if (currentAvailability.getFriday()){
                image_friday.setBackgroundColor(Color.GREEN);
            }
            if (currentAvailability.getSaturday()){
                image_saturday.setBackgroundColor(Color.GREEN);
            }
            if (currentAvailability.getSunday()){
                image_sunday.setBackgroundColor(Color.GREEN);
            }
            if (currentAvailability.getWeekly()){
                weekly_text.setText(R.string.yes_option);
            }else{
                weekly_text.setText("NO");
            }

            return  itemview;
        }



    }*/
}

