package app.jyh.com.webpdemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener{

	private WebpSurfaceView webpSurfaceView;
	private ImageView iv_webp;
	private TextView tv_type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		webpSurfaceView = (WebpSurfaceView)findViewById(R.id.sv_webp);
		iv_webp = (ImageView)findViewById(R.id.iv_webp);
		tv_type = (TextView)findViewById(R.id.tv_type);
		tv_type.setText("");
		findViewById(R.id.btn_webp_img).setOnClickListener(this);
		findViewById(R.id.btn_70).setOnClickListener(this);
		findViewById(R.id.btn_100).setOnClickListener(this);
		findViewById(R.id.btn_8sec).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btn_webp_img:
				tv_type.setText("测试用imageview显示webp静态图");
				iv_webp.setBackgroundResource(R.drawable.rocket0045);
				showWebpImageView(true);
				break;
			case R.id.btn_70:
				webpSurfaceView.show_70();
				tv_type.setText("70");
				showWebpImageView(false);
				break;
			case R.id.btn_100:
				webpSurfaceView.show_100();
				tv_type.setText("100");
				showWebpImageView(false);
				break;
			case R.id.btn_8sec:
				webpSurfaceView.show_8sec();
				tv_type.setText("8秒");
				showWebpImageView(false);
				break;
		}
	}

	private void showWebpImageView(boolean isShow){
		if(isShow){
			iv_webp.setVisibility(View.VISIBLE);
			webpSurfaceView.setVisibility(View.INVISIBLE);
		}else {
			iv_webp.setVisibility(View.INVISIBLE);
			webpSurfaceView.setVisibility(View.VISIBLE);
		}
	}

}
