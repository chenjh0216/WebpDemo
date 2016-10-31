package app.jyh.com.webpdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener{

	private WebpSurfaceView webpSurfaceView;
	private TextView tv_type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		webpSurfaceView = (WebpSurfaceView)findViewById(R.id.sv_webp);
		tv_type = (TextView)findViewById(R.id.tv_type);
		tv_type.setText("");
		findViewById(R.id.btn_70).setOnClickListener(this);
		findViewById(R.id.btn_100).setOnClickListener(this);
		findViewById(R.id.btn_8sec).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btn_70:
				webpSurfaceView.show_70();
				tv_type.setText("70");
				break;
			case R.id.btn_100:
				webpSurfaceView.show_100();
				tv_type.setText("100");
				break;
			case R.id.btn_8sec:
				webpSurfaceView.show_8sec();
				tv_type.setText("8秒");
				break;
		}
	}
}
