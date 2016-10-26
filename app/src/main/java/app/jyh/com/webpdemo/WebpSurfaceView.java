package app.jyh.com.webpdemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.facebook.animated.webp.WebPFrame;
import com.facebook.animated.webp.WebPImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by 酱油花 on 2016/10/27.
 */
public class WebpSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

	private Subscription subscription;
	private WebPImage webPImage;
	private Paint paint;

	public WebpSurfaceView(Context context) {
		super(context);
		init();
	}

	public WebpSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public WebpSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public WebpSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	private void init() {
		paint = new Paint();
		paint.setColor(Color.GREEN);
		Observable.just(R.raw.test)
				.observeOn(Schedulers.io())
				.map(new Func1<Integer, WebPImage>() {
					@Override
					public WebPImage call(Integer integer) {
						InputStream inputStream = getResources().openRawResource(integer);
						ByteArrayOutputStream outStream = new ByteArrayOutputStream();
						byte[] data = new byte[100];
						int count = -1;
						try {
							while ((count = inputStream.read(data, 0, 100)) != -1) {
								outStream.write(data, 0, count);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						byte[] out = outStream.toByteArray();
						return WebPImage.create(out);
					}
				}).subscribeOn(AndroidSchedulers.mainThread())
				.subscribe(new Action1<WebPImage>() {
					@Override
					public void call(WebPImage image) {
						webPImage = image;
					}
				}, new Action1<Throwable>() {
					@Override
					public void call(Throwable throwable) {
						Log.e("Main", Log.getStackTraceString(throwable));
					}
				});
		SurfaceHolder holder = getHolder();
		holder.addCallback(this); //设置Surface生命周期回调
	}

	@Override
	public void surfaceCreated(final SurfaceHolder holder) {
		subscription = Observable.interval(100, TimeUnit.MILLISECONDS)
				.observeOn(Schedulers.computation())
				.map(new Func1<Long, Long>() {
					@Override
					public Long call(Long aLong) {
						return aLong % webPImage.getFrameCount();
					}
				})
				.observeOn(Schedulers.computation())
				.subscribe(new Action1<Long>() {
					@Override
					public void call(Long aLong) {
						Log.d("Main","draw " + aLong + " frame");
						Canvas canvas = holder.lockCanvas(null);
						canvas.drawColor(Color.BLACK);
						WebPFrame frame = webPImage.getFrame(aLong.intValue());
						Bitmap bitmap = Bitmap.createBitmap(frame.getWidth(), frame.getHeight(), Bitmap.Config.ARGB_8888);
						frame.renderFrame(frame.getWidth(), bitmap.getHeight(), bitmap);
						canvas.drawBitmap(bitmap, 0, 0, paint);
						holder.unlockCanvasAndPost(canvas);
					}
				}, new Action1<Throwable>() {
					@Override
					public void call(Throwable throwable) {
						Log.e("Main", Log.getStackTraceString(throwable));
					}
				});
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (subscription != null && !subscription.isUnsubscribed()) {
			subscription.unsubscribe();
		}
	}
}
