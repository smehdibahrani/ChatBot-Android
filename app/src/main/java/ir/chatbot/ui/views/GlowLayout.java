package ir.chatbot.ui.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

import ir.chatbot.R;
import ir.chatbot.utils.DimensionUtils;




public class GlowLayout extends ConstraintLayout {

    private Paint glowPaint = new Paint();
    private Paint strokePaint = new Paint();
    private Path path = new Path();
    private RectF rectF = new RectF();
    private int glowSize = DimensionUtils.convertDipToPixels(12);
    private int shadowDistance = DimensionUtils.convertDipToPixels(4);
    private int cornerRadius = DimensionUtils.convertDipToPixels(8);
    private int effectColor = Color.parseColor("#1A6ABE79");
    private int backgroundColor = Color.parseColor("#FE3249");
    private int glowColor = Color.parseColor("#4Dfe3249");
    private int strokeWidth = 0;
    private int strokeColor = Color.BLACK;
    private Paint clipPaint = new Paint();
    private Path clipPath = new Path();
    private PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

    public void setGlowLayoutBackgroundColor(int color){
        backgroundColor = color;
        glowPaint.setColor(backgroundColor);
        invalidate();
    }
    public void setGlowLayoutBackgroundColor(String strColor){
        int color = Color.parseColor(strColor);
        backgroundColor = color;
        glowPaint.setColor(backgroundColor);
        invalidate();
    }
    public void setGlowColor(String strColor){
        int color = Color.parseColor(strColor);
        glowColor = color;
        if(glowSize > 0)
            glowPaint.setShadowLayer(glowSize, 0, shadowDistance, glowColor);
        invalidate();
    }




    public GlowLayout(Context context) {
        super(context);
        init();
    }

    public GlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        init();
    }

    public GlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        init();
    }

    private void initAttrs (AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.GlowLayout);
        effectColor = typedArray.getColor(R.styleable.GlowLayout_glowLayoutRippleColor, effectColor);
        backgroundColor = typedArray.getColor(R.styleable.GlowLayout_glowLayoutBackgroundColor, backgroundColor);
        glowColor = typedArray.getColor(R.styleable.GlowLayout_glowLayoutGlowColor, glowColor);
        cornerRadius = typedArray.getDimensionPixelSize(R.styleable.GlowLayout_glowLayoutCornerRadius, DimensionUtils.convertDipToPixels(8));
        glowSize = typedArray.getDimensionPixelSize(R.styleable.GlowLayout_glowLayoutGlowSize, DimensionUtils.convertDipToPixels(12));
        strokeColor = typedArray.getColor(R.styleable.GlowLayout_glowLayoutStrokeColor, strokeColor);
        strokeWidth = typedArray.getDimensionPixelSize(R.styleable.GlowLayout_glowLayoutStrokeWidth, 0);
        typedArray.recycle();
    }

    private void init () {
        setWillNotDraw(false);
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        clipPaint.setAntiAlias(true);

        glowPaint.setAntiAlias(true);
        glowPaint.setColor(backgroundColor);
        if(glowSize > 0)
            glowPaint.setShadowLayer(glowSize, 0, shadowDistance, glowColor);
        else
            shadowDistance = 0;

        strokePaint.setAntiAlias(true);
        strokePaint.setColor(strokeColor);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(strokeWidth);

        setPadding(glowSize + strokeWidth, (glowSize - shadowDistance) + strokeWidth, glowSize + strokeWidth, glowSize + shadowDistance + strokeWidth);
    }

    @Override
    public void draw(Canvas canvas) {
        float with = getWidth();
        float height = getHeight();
        rectF.set(glowSize + strokeWidth, (glowSize - shadowDistance) + strokeWidth, with - (glowSize + strokeWidth), height - (glowSize + shadowDistance + strokeWidth));
        path.reset();
        path.addRoundRect(rectF, cornerRadius >= 0 ? cornerRadius : rectF.bottom / 2, cornerRadius >= 0 ? cornerRadius : rectF.bottom / 2, Path.Direction.CW);
        canvas.drawPath(path, glowPaint);

        if(strokeWidth > 0)
            canvas.drawPath(path, strokePaint);

        int saveCount = 0;
        if(!isInEditMode())
            saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

        super.draw(canvas);

        if(!isInEditMode()) {

            clipPath.reset();
            clipPaint.setXfermode(porterDuffXfermode);
            clipPath.addRoundRect(rectF, cornerRadius >= 0 ? cornerRadius : height / 2, cornerRadius >= 0 ? cornerRadius : height / 2, Path.Direction.CW);
            clipPath.setFillType(Path.FillType.INVERSE_WINDING);

            canvas.drawPath(clipPath, clipPaint);

            canvas.restoreToCount(saveCount);
            clipPaint.setXfermode(null);
        }
    }

}