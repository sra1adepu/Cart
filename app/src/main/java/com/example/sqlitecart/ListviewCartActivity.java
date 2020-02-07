package com.example.sqlitecart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListviewCartActivity extends AppCompatActivity {
    ListView product_list_view;
    List<Item> cartitem_list;
    DataHandler db;

    ListViewCartViewAdapter listViewCartViewAdapter;

    int positioni;
    TextView cart_product_quantity_tv;
    Item itm;
    LinearLayout cart_plus_minus_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_cart);
        product_list_view = findViewById(R.id.product_list_view);

        cartitem_list = new ArrayList<>();

        db = new DataHandler(this);


        Cursor c = db.getAllContacts();

        while (c.moveToNext()) {

            Item cartitem = new Item();
            cartitem.setName(c.getString(1));
            cartitem.setQuantity(c.getString(2));
            cartitem.setPrice(c.getString(3));
            cartitem_list.add(cartitem);
        }

        listViewCartViewAdapter = new ListViewCartViewAdapter(ListviewCartActivity.this, cartitem_list);
        product_list_view.setAdapter(listViewCartViewAdapter);


    }

    private class ListViewCartViewAdapter extends BaseAdapter {
        Context context;
        List<Item> cartitem_list;

        public ListViewCartViewAdapter(ListviewCartActivity listviewCartActivity, List<Item> cartitem_list) {
            context = listviewCartActivity;
            this.cartitem_list = cartitem_list;
        }

        @Override
        public int getCount() {
            return cartitem_list.size();
        }

        @Override
        public Object getItem(int i) {
            return cartitem_list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View view, final ViewGroup viewGroup) {


            final TextView from_name, price_text, cart_product_quantity_tv;
            ImageView list_image, cart_plus_img, cart_minus_img;
            Button add_to_cart_text;
            LinearLayout cart_plus_minus_layout;
            View itemView = LayoutInflater.from(context).inflate(R.layout.cart_view_item, viewGroup, false);

            from_name = itemView.findViewById(R.id.from_name);
            price_text = itemView.findViewById(R.id.price_text);
            list_image = itemView.findViewById(R.id.list_image);
            add_to_cart_text = itemView.findViewById(R.id.add_to_cart_text);
            cart_plus_minus_layout = itemView.findViewById(R.id.cart_plus_minus_layout);
            cart_product_quantity_tv = itemView.findViewById(R.id.cart_product_quantity_tv);
            cart_plus_img = itemView.findViewById(R.id.cart_plus_img);
            cart_minus_img = itemView.findViewById(R.id.cart_minus_img);


            itm = cartitem_list.get(position);
            from_name.setText(cartitem_list.get(position).getName());
            price_text.setText(cartitem_list.get(position).getPrice());
            cart_product_quantity_tv.setText(cartitem_list.get(position).getQuantity() + "");

//            if(Integer.parseInt(cartitem_list.get(position).getQuantity())<0) {
//
//            }

            //final int i = 1;
            cart_plus_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Cursor c = db.getProductName(cartitem_list.get(position).getName());
                    c.moveToFirst();
                    //while (c.moveToNext()) {
                    int i = Integer.parseInt(c.getString(c.getColumnIndex("productquantity"))) + 1;
                    //  int i=Integer.parseInt(cartitem_list.get(position).getQuantity()) + 1;
                    db.update(cartitem_list.get(position).getName(), i + "");
                    cart_product_quantity_tv.setText((Integer.parseInt(c.getString(2)) + 1) + "");

                }
            });

            cart_minus_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Cursor c = db.getProductName(cartitem_list.get(position).getName());
                    c.moveToFirst();
                    //while (c.moveToNext()) {
                    int i = Integer.parseInt(c.getString(c.getColumnIndex("productquantity"))) - 1;



                    if ((Integer.parseInt(c.getString(2))) <= 1) {
                        //cart_product_quantity_tv.setText(0 + "");
                        db.delete(cartitem_list.get(position).getName());
                        cartitem_list.remove(position);
                        notifyDataSetChanged();
                    } else {
                        db.update(cartitem_list.get(position).getName(), i + "");
                        cart_product_quantity_tv.setText((Integer.parseInt(c.getString(2)) - 1) + "");
                    }


                }
            });


//


            return itemView;
        }


    }
}
