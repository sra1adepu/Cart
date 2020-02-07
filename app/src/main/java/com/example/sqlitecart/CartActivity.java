package com.example.sqlitecart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    RecyclerView product_list_view;

    List<Item> cartitem_list;

    CartViewAdapter cartViewAdapter;

    DataHandler db;

    private List<Item> selected = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        product_list_view = findViewById(R.id.product_list_view);
        cartitem_list = new ArrayList<>();

        db = new DataHandler(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        product_list_view.setLayoutManager(linearLayoutManager);

        Cursor c = db.getAllContacts();

        while (c.moveToNext()) {

            Item cartitem = new Item();
            cartitem.setName(c.getString(1));
            cartitem.setQuantity(c.getString(2));
            cartitem.setPrice(c.getString(3));
            cartitem_list.add(cartitem);
        }
//        Cursor c1 = db.getAllContacts();
//        c.moveToFirst();
//        do {
//            if (c1 != null && c1.getCount() > 0) {
//                for (int i = 0; i < cartitem_list.size(); i++) {
//                    if (cartitem_list.get(i).getName().equalsIgnoreCase(c.getString(c.getColumnIndex("productname")))) {
//                        cartitem_list.get(i).setQuantity(c.getString(c.getColumnIndex("productquantity")));
//                    }
//                }
//            }
//        } while (c.moveToNext());
//
        cartViewAdapter = new CartViewAdapter(CartActivity.this, cartitem_list);
        product_list_view.setAdapter(cartViewAdapter);
//        cartViewAdapter.notifyDataSetChanged();
    }

    private class CartViewAdapter extends RecyclerView.Adapter<CartViewAdapter.MyViewHolder> {
        Context context;
        List<Item> cartitem_list;

        public CartViewAdapter(CartActivity cartActivity, List<Item> cartitem_list) {
            context = cartActivity;
            this.cartitem_list = cartitem_list;
            selected = new ArrayList<>();

        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // infalte the item Layout
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_view_item, parent, false);
            // set the view's size, margins, paddings and layout parameters
            MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
            return vh;
        }


        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
            final Item itm = cartitem_list.get(position);
            holder.from_name.setText(cartitem_list.get(position).getName());
            holder.price_text.setText(cartitem_list.get(position).getPrice());
            holder.cart_product_quantity_tv.setText(cartitem_list.get(position).getQuantity() + "");
            //final int i = 1;

            holder.cart_plus_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Cursor c = db.getProductName(cartitem_list.get(position).getName());
                        c.moveToFirst();
                    //while (c.moveToNext()) {
                       int i = Integer.parseInt(c.getString(c.getColumnIndex("productquantity"))) + 1;
                      //  int i=Integer.parseInt(cartitem_list.get(position).getQuantity()) + 1;
                        db.update(cartitem_list.get(position).getName(), i + "");



                        holder.cart_product_quantity_tv.setText((Integer.parseInt(c.getString(2))+1)+"");
                    }
            });
            holder.cart_minus_img.setOnClickListener(new View.OnClickListener() {
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
                        holder.cart_product_quantity_tv.setText((Integer.parseInt(c.getString(2)) - 1) + "");
                    }


                }
            });


        }

        @Override
        public int getItemCount() {
            return cartitem_list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView from_name, price_text, cart_product_quantity_tv;
            ImageView list_image, cart_plus_img, cart_minus_img;
            Button add_to_cart_text;
            LinearLayout cart_plus_minus_layout;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                from_name = itemView.findViewById(R.id.from_name);
                price_text = itemView.findViewById(R.id.price_text);
                list_image = itemView.findViewById(R.id.list_image);
                add_to_cart_text = itemView.findViewById(R.id.add_to_cart_text);
                cart_plus_minus_layout = itemView.findViewById(R.id.cart_plus_minus_layout);
                cart_product_quantity_tv = itemView.findViewById(R.id.cart_product_quantity_tv);
                cart_plus_img = itemView.findViewById(R.id.cart_plus_img);
                cart_minus_img = itemView.findViewById(R.id.cart_minus_img);
            }
        }

    }
}
