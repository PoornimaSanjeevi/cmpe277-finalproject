package tania277.project_final.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;
import java.util.List;


import tania277.project_final.Models.RunRecord;
import tania277.project_final.R;

public class RunRecordAdapter  extends ArrayAdapter<RunRecord> {
    private static final String TAG = "RunRecordAdapter";
    private List<RunRecord> cardList = new ArrayList<RunRecord>();

    static class CardViewHolder {
        TextView line1;
        TextView line2;
        TextView line3;
        MapView mapView;
    }

    public RunRecordAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(RunRecord object) {
        cardList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.cardList.size();
    }

    @Override
    public RunRecord getItem(int index) {
        return this.cardList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        Log.i("message_runbuddy", "row is null "+(row==null));
        CardViewHolder viewHolder;
        if (row == null) {
            Log.i("message_runbuddy: " , "inside get view of run record adapter");
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.run_record_item, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.line1 = (TextView) row.findViewById(R.id.showename);
            viewHolder.line2 = (TextView) row.findViewById(R.id.showdistance);
            viewHolder.line3 = (TextView) row.findViewById(R.id.showtime);
//            viewHolder.mapView = (MapView) row.findViewById(R.id.map_view);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder)row.getTag();
        }
        RunRecord card = getItem(position);
        viewHolder.line1.setText(card.getEventName());
        viewHolder.line2.setText(card.getDistanceRan());
        viewHolder.line3.setText(card.getTimeRan());
        return row;
    }



    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}