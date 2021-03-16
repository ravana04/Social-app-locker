package com.example.app_lock;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import android.widget.ArrayAdapter;

public class NumbersViewAdapter extends ArrayAdapter<time_layout> {

    public NumbersViewAdapter(@NonNull Context context, ArrayList<time_layout> arrayList) {

        // pass the context and arrayList for the super
        // constructor of the ArrayAdapter class
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.row_time, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        time_layout currentNumberPosition = getItem(position);





        // then according to the position of the view assign the desired TextView 2 for the same
        TextView textView2 = currentItemView.findViewById(R.id.textView1);
        textView2.setText(currentNumberPosition.getNumbersInText());

        // then return the recyclable view
        return currentItemView;
    }

}
