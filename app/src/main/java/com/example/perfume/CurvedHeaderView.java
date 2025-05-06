package com.example.perfume;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class CurvedHeaderView extends View {
    private Paint paint;
    private Path path;

    public CurvedHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.parseColor("#EC407A")); // màu nền header
        paint.setStyle(Paint.Style.FILL);
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        path.reset();

        // 🔽 Bạn chọn 1 trong 2 kiểu: TAM GIÁC hoặc VÒNG CUNG

        // ❶ TAM GIÁC:
        path.moveTo(width / 2f, 0);       // Đỉnh trên giữa
        path.lineTo(0, height);           // Góc trái dưới
        path.lineTo(width, height);       // Góc phải dưới
        path.close();

        // ❷ VÒNG CUNG: (bình luận lại 3 dòng trên nếu dùng cái này)
//        path.moveTo(0, 0);
//        path.lineTo(0, height - 100);
//        path.quadTo(width / 2, height + 100, width, height - 100);
//        path.lineTo(width, 0);
//        path.close();

        canvas.clipPath(path);            // Chỉ hiển thị vùng trong path
        canvas.drawPath(path, paint);     // Vẽ màu nền trong vùng path
    }
}
