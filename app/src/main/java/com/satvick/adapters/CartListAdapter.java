package com.satvick.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.satvick.R;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ItemCartShoppingBagBinding;
import com.satvick.model.CartListModel;
import com.satvick.model.UpdateCartQuantity;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.GlobalVariables;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {

    private List<CartListModel.ProductListRetrievedSuccessfully> cartListModelList;
    private int clickCount = 0;
    private Context context;
    private Double convertedPrice;
    private double grand_total;
    private LayoutInflater inflater;
    final ListPopupWindow listPopupWindow;
    private CartItemClickListener listener;
    private Map<String, String> quantity;
    private String symbol;
    private TotalPriceListener totalPriceListener;

    public CartListAdapter(Context context, List<CartListModel.ProductListRetrievedSuccessfully> cartListModelList) {
        this.context = context;
        this.cartListModelList = cartListModelList;
        this.quantity = new HashMap<String, String>();
        this.listPopupWindow = new ListPopupWindow(context);
        this.convertedPrice = Double.valueOf(Double.parseDouble(SharedPreferenceWriter.getInstance(context).getString("converted_amount")));
        this.symbol = SharedPreferenceWriter.getInstance(context).getString("symbol");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }
        ItemCartShoppingBagBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_cart_shopping_bag, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        quantity.put(cartListModelList.get(position).getProductId(),cartListModelList.get(position).getQuantity());

        String imgUrl = cartListModelList.get(position).getImage();
        if (imgUrl != null && imgUrl.length() > 0) {
            Glide.with(context).load(imgUrl).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    viewHolder.binding.progressbar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    viewHolder.binding.progressbar.setVisibility(View.GONE);
                    return false;
                }
            }).into(viewHolder.binding.imgProduct);
        }

        viewHolder.binding.tvDesc.setText(cartListModelList.get(position).getName());
        viewHolder.binding.tvProductName.setText(cartListModelList.get(position).getBrand());
        viewHolder.binding.tvPrice.setText(symbol+" "+Math.round(Double.parseDouble(cartListModelList.get(position).getSp())*convertedPrice));
        viewHolder.binding.tvSoldByName.setText(cartListModelList.get(position).getSoldBy());
        viewHolder.binding.tvSizeContent.setText(cartListModelList.get(position).getSize());
        viewHolder.binding.tvSize.setText(cartListModelList.get(position).getSize_label()+": ");
        if(!cartListModelList.get(position).getDiscount_coupon().equalsIgnoreCase("0"))
        {
            viewHolder.binding.tvCouponDiscount.setVisibility(View.VISIBLE);
            viewHolder.binding.tvCouponDiscount.setText("Coupon Applied");
        }else{
            viewHolder.binding.tvCouponDiscount.setVisibility(View.GONE);
        }
        viewHolder.binding.stockLeftText.setText(MessageFormat.format("Only {0} unit in stock",cartListModelList.get(position).getTotalItems()));

        if(cartListModelList.get(position).getPercentage().equalsIgnoreCase("0")){
            viewHolder.binding.tvCuttedText.setVisibility(View.GONE);
            viewHolder.binding.tvOff.setVisibility(View.GONE);
        }else {
            viewHolder.binding.tvOff.setText(MessageFormat.format("({0}% OFF)",cartListModelList.get(position).getPercentage()));
            viewHolder.binding.tvCuttedText.setText(symbol+" "+Math.round(Double.parseDouble(cartListModelList.get(position).getMrp())*convertedPrice));
            viewHolder.binding.tvCuttedText.setPaintFlags(viewHolder.binding.tvCuttedText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        final double price = Double.parseDouble(cartListModelList.get(position).getSp());
        if(cartListModelList.get(position).getQuantity()!=null) {
            viewHolder.binding.tvSelectQty.setText(cartListModelList.get(position).getQuantity());
            double quantity = Double.parseDouble(cartListModelList.get(position).getQuantity());
            double total = price * quantity;
            grand_total = grand_total + total;

        }
        else
        {
            viewHolder.binding.tvSelectQty.setText("1");
            double quantity = 1;
            double total = price * quantity;
            grand_total = grand_total + total;

        }

        SharedPreferenceWriter.getInstance(context).writeStringValue(SharedPreferenceKey.TOTAL_PRICE, String.valueOf(grand_total));

        viewHolder.binding.tvSelectQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickCount%2==0) {
                    clickCount=clickCount+1;
                    openDropDownList(viewHolder,position);
                }
                else
                {
                    clickCount=clickCount+1;
                    listPopupWindow.dismiss();

                }


            }
        });

    }

    private void openDropDownList(final ViewHolder viewHolder, final int getAdapterPosition) {
        final List<String> selectedMenuList = new ArrayList<>();
        int loopsize=0;

        if(Integer.parseInt(cartListModelList.get(getAdapterPosition).getTotalItems())>5)
        {
            loopsize=5;
        }
        else{
            loopsize= Integer.parseInt(cartListModelList.get(getAdapterPosition).getTotalItems());
        }

        for(int i=0;i<loopsize;i++)
        {
            selectedMenuList.add(""+(i+1));

        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, selectedMenuList);



        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                final MyDialog myDialog=new MyDialog(context);
                myDialog.showDialog();
                String selectedMenu = selectedMenuList.get(i);
                viewHolder.binding.tvSelectQty.setText(selectedMenu);
                clickCount=clickCount-1;
                viewHolder.binding.tvPrice.setText(symbol+" "+Double.parseDouble(selectedMenu)*Double.parseDouble(cartListModelList.get(getAdapterPosition).getActualPrice())*convertedPrice);
                if(quantity.containsKey(cartListModelList.get(getAdapterPosition).getProductId()))
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        quantity.replace(cartListModelList.get(getAdapterPosition).getProductId(),selectedMenu);
                    }
                }
                else
                {
                    quantity.put(cartListModelList.get(getAdapterPosition).getProductId(),selectedMenu);
                }


                if (SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("false") ||
                        SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase(""))
                {
                    myDialog.hideDialog();
                    listener.setTotalPrice(view,quantity,cartListModelList.get(getAdapterPosition).getSize());
                    listPopupWindow.dismiss();
                }
                else {


                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    Call<UpdateCartQuantity> call = apiInterface.updatecart(SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.TOKEN),
                            SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.USER_ID),
                            cartListModelList.get(getAdapterPosition).getProductId(),
                            quantity.get(cartListModelList.get(getAdapterPosition).getProductId()),cartListModelList.get(getAdapterPosition).getSize()!=null?cartListModelList.get(getAdapterPosition).getSize():"");


                    call.enqueue(new Callback<UpdateCartQuantity>() {
                        @Override
                        public void onResponse(Call<UpdateCartQuantity> call, Response<UpdateCartQuantity> response) {
                            if (response.isSuccessful()) {
                                myDialog.hideDialog();

                                if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {
                                    listener.setTotalPrice(view, quantity,cartListModelList.get(getAdapterPosition).getSize());
                                    listPopupWindow.dismiss();
                                } else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        }





                        @Override
                        public void onFailure(Call<UpdateCartQuantity> call, Throwable t) {
                            myDialog.hideDialog();

                        }
                    });
                }
            }
        });

        listPopupWindow.setAnchorView(viewHolder.binding.tvSelectQty);
        listPopupWindow.setAdapter(arrayAdapter);
        listPopupWindow.show();
    }

    @Override
    public int getItemCount() {
        return cartListModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemCartShoppingBagBinding binding;

        ViewHolder(ItemCartShoppingBagBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


            binding.tvRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (listener != null)
                        listener.onRemoveItemClick(view, getAdapterPosition());
                }
            });

            binding.tvMoveToBag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (listener != null)
                        listener.onMoveToWishListItemClick(view, getAdapterPosition());
                }
            });

            binding.rlTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null)
                        listener.onImageItemClick(view, getAdapterPosition());
                }
            });
        }

    }

    public String getPrice()
    {
        return grand_total+"";
    }

    public interface CartItemClickListener {
        void onRemoveItemClick(View view, int pos);
        void onMoveToWishListItemClick(View view, int pos);
        void onImageItemClick(View view,int pos);
        void setTotalPrice(View view,Map<String,String> map,String size);

    }

    public void setListener(CartItemClickListener listener) {
        this.listener = listener;
    }

    protected interface TotalPriceListener{
        void getTotalPrice(double totalPrice);
    }

    public void setTotalPriceListener(TotalPriceListener totalPriceListener) {
        this.totalPriceListener = totalPriceListener;
    }
}
