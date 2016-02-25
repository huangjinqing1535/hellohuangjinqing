package com.huang.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CustomImageView extends ImageView {

	public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CustomImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomImageView(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		Drawable drawable = getDrawable();

		if (drawable == null) {
			return;
		}

		if (getWidth() == 0 || getHeight() == 0) {
			return;
		}

		Bitmap b = ((BitmapDrawable) drawable).getBitmap();

		if (null == b) {
			return;
		}

		Bitmap bitmap = b.copy(Config.ARGB_8888, true);

		int w = getWidth();
		int h = getHeight();

		Bitmap roundBitmap = getCircularBitmap(bitmap, w);
		
		canvas.drawBitmap(roundBitmap, 0, 0, null);
	}

	private Bitmap getCircularBitmap(Bitmap bitmap, int radius) {
		Bitmap sbmp = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
		
		Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.BLACK);
		canvas.drawCircle(sbmp.getWidth() / 2 ,
				sbmp.getHeight() / 2 , sbmp.getWidth() / 2 , paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(sbmp, rect, rect, paint);

		return output;
	}

}
