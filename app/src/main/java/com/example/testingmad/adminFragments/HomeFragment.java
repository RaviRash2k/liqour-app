package com.example.testingmad.adminFragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.testingmad.R;

public class HomeFragment extends Fragment {

    ListView listView;
    String[] title = {"aaa", "bbb", "ccc", "ddd", "eee"};
    String[] price = {"10", "20", "30", "40", "50"};
    int[] img = {R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        listView = rootView.findViewById(R.id.lst);
        Adapter adapter = new Adapter(getContext(), title, img, price);
        listView.setAdapter(adapter);

        return rootView;
    }
}

class Adapter extends ArrayAdapter<String>{
    Context context;
    int[] images;
    String[] titles;
    String[] price;

    Adapter(Context context, String[] title, int[] images, String[] price){

        super(context, R.layout.single_item, R.id.forName, title);
        this.context = context;
        this.images = images;
        titles = title;
        this.price = price;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.single_item, parent, false);

        ImageView imageView = row.findViewById(R.id.forimg);
        TextView titleView = row.findViewById(R.id.forName);
        TextView priceView = row.findViewById(R.id.forPrice);

        imageView.setImageResource(images[position]);
        titleView.setText(titles[position]);
        priceView.setText(price[position]);

        return row;
    }
}