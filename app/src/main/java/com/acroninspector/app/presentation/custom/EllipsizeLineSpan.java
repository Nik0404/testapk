package com.acroninspector.app.presentation.custom;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.style.LineBackgroundSpan;
import android.text.style.ReplacementSpan;

import org.jetbrains.annotations.NotNull;

public class EllipsizeLineSpan extends ReplacementSpan implements LineBackgroundSpan {

    private int layoutLeft = 0;
    private int layoutRight = 0;

    public EllipsizeLineSpan() {
    }

    @Override
    public void drawBackground(Canvas c, @NotNull Paint p, int left, int right, int top, int baseline,
                               int bottom, @NotNull CharSequence text, int start, int end, int lnum) {
        Rect clipRect = new Rect();
        c.getClipBounds(clipRect);
        layoutLeft = clipRect.left;
        layoutRight = clipRect.right;
    }

    @Override
    public int getSize(@NotNull Paint paint, CharSequence text, int start, int end,
                       Paint.FontMetricsInt fm) {
        return layoutRight - layoutLeft;
    }

    @Override
    public void draw(@NotNull Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {
        float textWidth = paint.measureText(text, start, end);

        if (x + (int) Math.ceil(textWidth) < layoutRight) {  //text fits
            canvas.drawText(text, start, end, x, y, paint);
        } else {
            float ellipsiswid = paint.measureText("\u2026");

            // move 'end' to the ellipsis point
            end = start + paint.breakText(text, start, end, true,
                    layoutRight - x - ellipsiswid, null);

            canvas.drawText(text, start, end, x, y, paint);
            canvas.drawText("\u2026", x + paint.measureText(text, start, end), y, paint);
        }
    }
}