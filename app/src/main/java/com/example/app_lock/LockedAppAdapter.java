package com.example.app_lock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;


public class LockedAppAdapter extends ArrayAdapter<AppModel> {

    Context con;
    int res;
    List<AppModel> applist;
    SharedPreferences sharedPreferences;

    public LockedAppAdapter(@NonNull Context context, int resource, @NonNull List<AppModel> objects)
    {
        super(context, resource, objects);
        con = context;
        res = resource;
        applist = objects;
    }

    @SuppressLint("ViewHolder")
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final AppModel app = applist.get(position);
        final DatabaseHandler db =  new DatabaseHandler(con);
        final List<AppModel> dblist = db.getAllLockedApps();

        final AppModel dbapp = dblist.get(position);

        LayoutInflater inflater = LayoutInflater.from(con);
        convertView = inflater.inflate(res, parent,false);

        //final ImageView tvappicon = (ImageView) convertView.findViewById(R.id.appicon);
        final TextView tvappname = (TextView) convertView.findViewById(R.id.app);
        //   final TextView txtstat = (TextView) convertView.findViewById(R.id.subtitle);
        final ImageView tvappst = (ImageView) convertView.findViewById(R.id.appst);

        //tvappicon.setImageDrawable(app.getAppicon());
        tvappname.setText(app.getAppname());
        tvappst.setImageResource(R.drawable.lock);
        if(dbapp.getStatus() == 0){
            tvappst.setImageResource(R.drawable.unlock);
        }
        else
            tvappst.setImageResource(R.drawable.lock);


        tvappst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dbapp.getStatus() == 0) {
                    dbapp.setStatus(1);
                    db.changeStatus(dbapp,1);
                    tvappst.setImageResource(R.drawable.lock);
                   // Toast.makeText(getContext(),dbapp.appname + " is Locked",Toast.LENGTH_SHORT).show();
                    db.addApptoLockedList(app);
                    db.display();
                }
                else {
                    sharedPreferences = getContext().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
                    int f = sharedPreferences.getInt("Flag", -1);
                    Log.e("f : ", String.valueOf(f));
                    if(f == 1) {
                        Log.e("Flag : ", "Not Unlockable");
                       // Toast.makeText(getContext(),"Cannot Unlock before Tomorrow",Toast.LENGTH_LONG).show();
                    }
                    else if(f == 0){
                        Log.e("Flag : ", "Unlockable");
                        dbapp.setStatus(0);
                        db.changeStatus(dbapp, 0);
                        tvappst.setImageResource(R.drawable.unlock);
                     //   Toast.makeText(getContext(), dbapp.appname + " is Unlocked", Toast.LENGTH_SHORT).show();
                        db.removeAppfromLockedList(app);
                        db.display();
                    }
                }

            }

        });
        return convertView;
    }
}
