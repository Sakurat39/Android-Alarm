package com.example.akb.edaalarm;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;

import java.util.Random;

/**
 * Created by akb on 2018/2/1.
 */

class Fontic {
    public static Bitmap createTextImage( int txtSize, String innerTxt) {
        //若使背景为透明，必须设置为Bitmap.Config.ARGB_4444
        int h;
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(txtSize);
        FontMetrics fm = paint.getFontMetrics();
        h=(int)(fm.bottom-fm.top);
        float[] widths = new float[innerTxt.length()];
        paint.getTextWidths(innerTxt, widths);
        float totalWidth = 0;
        for (float w : widths) {
            totalWidth += w;
        }



        Bitmap bm = Bitmap.createBitmap((int)totalWidth, h, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bm);

        //计算得出文字的绘制起始x、y坐标
        //int posX = width/2 - txtSize*innerTxt.length()/2;
        //int posY = h/2 - txtSize/2;

        canvas.drawText(innerTxt, 0,0, paint);


        return bm;
    }

}


public class FontPic {

    private static FontPic  bmpCode;

    public static FontPic getInstance() {
        if (bmpCode == null)
            bmpCode = new FontPic ();
        return bmpCode;
    }

    private FontPic(){

    }

    // default settings
    private static final int DEFAULT_FONT_SIZE = 25;
    private static final int DEFAULT_LINE_NUMBER = 2;
    private static final int BASE_PADDING_LEFT = 5, RANGE_PADDING_LEFT = 25, BASE_PADDING_TOP = 15, RANGE_PADDING_TOP = 30;
    private static final int DEFAULT_WIDTH = 40, DEFAULT_HEIGHT = 60;

    // settings decided by the layout xml
    // canvas width and height
    private int width = DEFAULT_WIDTH, height = DEFAULT_HEIGHT;

    // random word space and pading_top
    private int base_padding_left = BASE_PADDING_LEFT, range_padding_left = RANGE_PADDING_LEFT, base_padding_top = BASE_PADDING_TOP, range_padding_top = RANGE_PADDING_TOP;

    // number of chars, lines; font size
    private int line_number = DEFAULT_LINE_NUMBER, font_size = DEFAULT_FONT_SIZE;

    // variables
    private int padding_left, padding_top;
    private Random random = new Random();

    // 验证码图片
    public Bitmap createBitmap(String text) {
        padding_left=0;
        width=DEFAULT_WIDTH*text.length();
        Bitmap bp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bp);
        c.drawColor(Color.argb(100,240,248,255));
        Paint paint = new Paint();
        paint.setTextSize(font_size);

        for (int i = 0; i < text.length(); i++) {
            randomTextStyle(paint);
            randomPadding();
            c.drawText(String.valueOf(text.charAt(i)), padding_left, padding_top, paint);
        }

        for (int i = 0; i < line_number; i++) {
            drawLine(c, paint);
        }
        c.save(Canvas.ALL_SAVE_FLAG);// 保存
        c.restore();//
        return bp;
    }

    private void drawLine(Canvas canvas, Paint paint) {
        int color = randomColor();
        int startX = random.nextInt(width);
        int startY = random.nextInt(height);
        int stopX = random.nextInt(width);
        int stopY = random.nextInt(height);
        paint.setStrokeWidth(1);
        paint.setColor(color);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    private int randomColor() {
        return randomColor(1);
    }

    private int randomColor(int rate) {
        int red = random.nextInt(256) / rate;
        int green = random.nextInt(256) / rate;
        int blue = random.nextInt(256) / rate;
        return Color.rgb(red, green, blue);
    }

    private void randomTextStyle(Paint paint) {
        int color = randomColor();
        paint.setColor(color);
        paint.setFakeBoldText(random.nextBoolean()); // true为粗体，false为非粗体
        float skewX = random.nextInt(11) / 10;
        skewX = random.nextBoolean() ? skewX : -skewX;
        paint.setTextSkewX(skewX); // float类型参数，负数表示右斜，整数左斜
        // paint.setUnderlineText(true); //true为下划线，false为非下划线
        // paint.setStrikeThruText(true); //true为删除线，false为非删除线
    }

    private void randomPadding() {
        padding_left += base_padding_left + random.nextInt(range_padding_left) + 10;
        padding_top = base_padding_top + random.nextInt(range_padding_top);
    }
}

