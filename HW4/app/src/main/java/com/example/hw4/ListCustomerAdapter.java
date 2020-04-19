package com.example.hw4;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListCustomerAdapter extends BaseAdapter {
    private List<Customer> customers;
    private LayoutInflater layoutInflater;
    private Context context;

    public ListCustomerAdapter(Context aContext, List<Customer> customerList){
        this.context = aContext;
        this.customers = customerList;
        layoutInflater = LayoutInflater.from(aContext);

    }
    @Override
    public int getCount() {
        return customers.size();
    }

    @Override
    public Object getItem(int position) {
        return customers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_layout, null);
            holder = new ViewHolder();
            holder.userView = (ImageView) convertView.findViewById(R.id.imageView_user);
            holder.nameView = (TextView) convertView.findViewById(R.id.textView_name);
            holder.phoneView = (TextView) convertView.findViewById(R.id.textView_phone);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Customer customer = this.customers.get(position);
        holder.nameView.setText(customer.getFullName());
        System.out.println("FullName"+customer.getFullName());
        holder.phoneView.setText(customer.getPhoneNumber());
        System.out.println("ImgName"+customer.getImageName());
        int imageId = this.getMipmapResIdByName(customer.getImageName());

        holder.userView.setImageResource(imageId);

        return convertView;
    }

    // Find Image ID corresponding to the name of the image (in the directory mipmap).
    public int getMipmapResIdByName(String resName)  {
        String pkgName = context.getPackageName();
        // Return 0 if not found.
        int resID = context.getResources().getIdentifier(resName , "mipmap", pkgName);
        System.out.print("RESID......................"+resID);
        Log.i("CustomListView", "Res Name: "+ resName+"==> Res ID = "+ resID);

        return resID;
    }

    static class ViewHolder {
        ImageView userView;
        TextView nameView;
        TextView phoneView;
    }
}
