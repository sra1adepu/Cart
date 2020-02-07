package com.example.sqlitecart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recycler_view;

    CartAdapter cartAdapter;

    Button go_to_cart;
    List<Item> itemList;

    DataHandler dataHandler;
    private List<Item> selected = new ArrayList<>();
    private int[] productimages = {
            R.drawable.fan1,
            R.drawable.fan2,
            R.drawable.fan3,
            R.drawable.fan4,
            R.drawable.fan5,
            R.drawable.fan6,
            R.drawable.fan7,
            R.drawable.fan8,
            R.drawable.fan9,
            R.drawable.fan10,
            R.drawable.fan11,
            R.drawable.fan12,
            R.drawable.fan13,
            R.drawable.fan14,
            R.drawable.fan15, R.drawable.fan16,
    };

    private String[] productnames = {"fan1", "fan2", "fan3", "fan4", "fan5", "fan6", "fan7", "fan8", "fan9", "fan10", "fan11", "fan12", "fan13", "fan14", "fan15", "fan16"};
    private String[] productprices = {"100", "120", "105", "99", "203", "195", "187", "171", "101", "129", "175", "199", "293", "165", "1870", "71"};

//    private String[] productnames = {"fan1", "fan2", "fan3", "fan4", "fan5", "fan6", "fan7", "fan8", };
//
//    private String[] productprices = {"100", "120", "105", "99", "203", "195", "187", "171"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycler_view = findViewById(R.id.recycler_view);
        go_to_cart = findViewById(R.id.go_to_cart);
        dataHandler = new DataHandler(this);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recycler_view.setLayoutManager(linearLayoutManager);

//        itemList = new ArrayList<>();
//
//
//        for (int i = 0; i < productimages.length; i++) {
//            Item item = new Item();
//            item.setName(productnames[i]);
//            item.setPrice(productprices[i]);
//            itemList.add(item);
//        }


        go_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ArrayList<Item> item1 = (ArrayList<Item>) selected;

                for (int i = 0; i < selected.size(); i++) {
                    Log.d("Products", selected.get(i).getName() + " " + selected.get(i).getPrice() + "  " + selected.get(i).getCount());

                    if (selected.get(i).getName() != null) {
                        Cursor c = dataHandler.getProductName(selected.get(i).getName());
                        if (c != null && c.getCount() > 0) {
                            dataHandler.update(selected.get(i).getName(), selected.get(i).getCount() + "");
                        } else {
                            dataHandler.insert(1, selected.get(i).getName(), selected.get(i).getCount(), selected.get(i).getPrice());
                        }
                    }

                }
                //we can use CartActivity also
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });


    }

    private void cartData() {
        Cursor c = dataHandler.getAllContacts();

        while (c.moveToNext()) {

            Item cartitem = new Item();
            cartitem.setName(c.getString(1));
            cartitem.setQuantity(c.getString(2));
            //   cartitem.setPrice(c.getString(3));

            // cartitem_list.add(cartitem);
            itemList.add(cartitem);

        }

    }


    private class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
        Context context;
        List<Item> itemList;
        int[] productimages;


        public CartAdapter(MainActivity mainActivity, List<Item> itemList, int[] productimages) {
            context = mainActivity;
            this.itemList = itemList;
            this.productimages = productimages;
            selected = new ArrayList<>();
        }
//        public void swap(List<Item> datas)
//        {
//            itemList.clear();
//            itemList.addAll(datas);
//            notifyDataSetChanged();
//        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // infalte the item Layout
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            // set the view's size, margins, paddings and layout parameters
            MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
            final Item itm = itemList.get(position);
            holder.from_name.setText(itemList.get(position).getName());
            holder.price_text.setText(itemList.get(position).getPrice());
            Glide.with(context).load(productimages[position]).into(holder.list_image);

            Log.d("InitialValue", itm.getName());

            if (itm.getQuantity() != null && Integer.parseInt(itm.getQuantity()) > 0) {
                holder.cart_plus_minus_layout.setVisibility(View.VISIBLE);
                holder.add_to_cart_text.setVisibility(View.GONE);
                holder.cart_product_quantity_tv.setText(itemList.get(position).getQuantity() + "");
            }
            holder.add_to_cart_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.cart_plus_minus_layout.setVisibility(View.VISIBLE);
                    holder.add_to_cart_text.setVisibility(View.GONE);
                    holder.cart_product_quantity_tv.setText(itemList.get(position).getCount() + "");

                    selected.add(itm);
                }
            });

            holder.cart_minus_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (itm.getCount() == 1) {
                        holder.add_to_cart_text.setVisibility(View.VISIBLE);
                        holder.cart_plus_minus_layout.setVisibility(View.GONE);
              //     selected.remove(itm);
                    }
                   if(itm.getCount()==1){
                        selected.remove(itm) ;
                        dataHandler.delete(itm.getName());
                   }else{
                       itm.setCount(itm.getCount() - 1);
                     selected.add(itm);
                   }
                    holder.cart_product_quantity_tv.setText(itm.getCount() + "");

                }
            });
            holder.cart_plus_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itm.setCount(itemList.get(position).getCount() + 1);
                    holder.cart_product_quantity_tv.setText(itm.getCount() + "");



                    if (!selected.contains(itm))
                        selected.add(itm);
                }
            });

        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return itemList.size();
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

    @Override
    protected void onResume() {
        super.onResume();
        itemList = new ArrayList<>();

        for (int i = 0; i < productimages.length; i++) {
            Item item = new Item();
            item.setName(productnames[i]);
            item.setPrice(productprices[i]);
            itemList.add(item);
        }
        Cursor c = dataHandler.getAllContacts();
        c.moveToFirst();
        do {
            if (c != null && c.getCount() > 0) {
                for (int i = 0; i < itemList.size(); i++) {
                    if (itemList.get(i).getName().equalsIgnoreCase(c.getString(c.getColumnIndex("productname")))) {
                        itemList.get(i).setQuantity(c.getString(c.getColumnIndex("productquantity")));
                        itemList.get(i).setCount(Integer.parseInt(c.getString(c.getColumnIndex("productquantity"))));
                    }
                }
            }
        } while (c.moveToNext());


        cartAdapter = new CartAdapter(MainActivity.this, itemList, productimages);
        // recycler_view.swapAdapter(cartAdapter,false);
        recycler_view.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();


    }
}

