package com.lawyer.customerloyaltysms.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import com.lawyer.customerloyaltysms.R;

/**
 * Created by stiven on 5/10/2016.
 */
public class Customer_RecyclerView_adapter extends RecyclerView.Adapter<Customer_RecyclerView_adapter.CostumersViewHolder> {
    private final LayoutInflater mInflater;
    private List<Customer_adapter> mCustomer;
    AdapterView.OnItemClickListener mItemClickListener;


    public Customer_RecyclerView_adapter(Context context, List<Customer_adapter> customers) {
        mInflater = LayoutInflater.from(context);
        mCustomer =new ArrayList<>(customers);
    }

    // Define listener member variable
    private static OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(String idperson, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public CostumersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.customer_row, parent, false);
        return new CostumersViewHolder(itemView);
    }

    public static class CostumersViewHolder extends RecyclerView.ViewHolder{
        public TextView allname, cells, document, idperson;
        public ImageView sent;

        public CostumersViewHolder(final View view) {
            super(view);
            allname = (TextView) view.findViewById(R.id.txtAllName);
            cells = (TextView) view.findViewById(R.id.txtLastCells);
            document = (TextView) view.findViewById(R.id.txtDocument);
            idperson = (TextView) view.findViewById(R.id.txtIdPerson);
            sent=(ImageView) view.findViewById(R.id.imgSent);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null)
                        listener.onItemClick(idperson.getText().toString(), getLayoutPosition());
                }
            });
        }

        public void bind(Customer_adapter model) {
            allname.setText(model.getAllName());
            cells.setText(model.getCells());
            document.setText(model.getDocument());
            idperson.setText(model.getIdPerson());
            String sentModel=model.getSent();
            if (sentModel=="1") {
                sent.setImageResource(R.mipmap.ic_done);
            }
            else {
                sent.setImageResource(R.mipmap.ic_close);
            }
        }
    }



    @Override
    public void onBindViewHolder(CostumersViewHolder holder, int position) {
        final Customer_adapter model = mCustomer.get(position);
        holder.bind(model);
    }



    @Override
    public int getItemCount() {
        return mCustomer.size();
    }

    public void animateTo(List<Customer_adapter> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<Customer_adapter> newModels) {
        for (int i = mCustomer.size() - 1; i >= 0; i--) {
            final Customer_adapter model = mCustomer.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Customer_adapter> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Customer_adapter model = newModels.get(i);
            if (!mCustomer.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Customer_adapter> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Customer_adapter model = newModels.get(toPosition);
            final int fromPosition = mCustomer.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public Customer_adapter removeItem(int position) {
        final Customer_adapter model = mCustomer.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Customer_adapter model) {
        mCustomer.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Customer_adapter model = mCustomer.remove(fromPosition);
        mCustomer.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }



}
