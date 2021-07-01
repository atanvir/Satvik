package com.satvick.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.satvick.R;
import com.satvick.databinding.AdapterPriceRangeBinding;

public class PriceRangeAdapter extends RecyclerView.Adapter<PriceRangeAdapter.MyViewHolder> {
    private Context context;
    private PriceRangerClickListner listner;


    public PriceRangeAdapter(Context context,PriceRangerClickListner listner) {
        this.context = context;
        this.listner=listner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PriceRangeAdapter.MyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_price_range, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements OnRangeSeekbarChangeListener {
        AdapterPriceRangeBinding binding;

        public MyViewHolder(AdapterPriceRangeBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
            binding.rangeSeekbar.setOnRangeSeekbarChangeListener(this);
        }


        @Override
        public void valueChanged(Number minValue, Number maxValue) {
            binding.minValueText.setText("" + minValue);
            long max = (long) maxValue;
            if (max == 10000) binding.maxValueText.setText("" + max + "+");
            else binding.maxValueText.setText("" + max);
            if(listner!=null) listner.getPriceRange((long)minValue,(long)maxValue);
        }
    }


   public interface PriceRangerClickListner {
        void getPriceRange(long minValue, long maxValue);
    }
}









