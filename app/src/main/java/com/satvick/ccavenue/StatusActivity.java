package com.satvick.ccavenue;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.view.View;

import com.satvick.R;
import com.satvick.activities.MainActivity;
import com.satvick.activities.MyOrderActivity;
import com.satvick.databinding.ActivityStatusBinding;

public class StatusActivity extends AppCompatActivity implements View.OnClickListener {
	ActivityStatusBinding binding;
	String transStatus="";


	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		binding= DataBindingUtil.setContentView(this,R.layout.activity_status);
		init();
	}

	private void init() {
		transStatus=getIntent().getStringExtra("transStatus");
		if(transStatus.equalsIgnoreCase("Transaction Declined!"))
		{
			binding.lottieAnimationView.setAnimation("warning.json");
			binding.textView1.setText("Transaction Declined");

		}
		else if(transStatus.equalsIgnoreCase("Transaction Successful!"))
		{
			binding.lottieAnimationView.setAnimation("done.json");
			binding.textView1.setText("Transaction Successful");

		}
		else if(transStatus.equalsIgnoreCase("Transaction Cancelled!"))
		{
			binding.lottieAnimationView.setAnimation("warning.json");
			binding.textView1.setText("Transaction Cancelled");


		}else {
			binding.lottieAnimationView.setAnimation("error.json");
			binding.textView1.setText("Status Not Known");
		}


		binding.okbtn.setOnClickListener(this);
		binding.statusBtn.setOnClickListener(this);
	}



	@Override
	public void onClick(View v) {
		switch (v.getId())
		{

			case R.id.statusBtn:

				Intent intent=new Intent(StatusActivity.this, MainActivity.class);
				intent.putExtra("from","StatusActivity");
				startActivity(intent);
				break;

			case R.id.okbtn:
				Intent intent2=new Intent(StatusActivity.this, MyOrderActivity.class);
				intent2.putExtra("from","StatusActivity");
				startActivity(intent2);
				break;
		}



	}
}