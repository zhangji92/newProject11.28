/**
 * This file is part of GraphView.
 * <p/>
 * GraphView is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * <p/>
 * GraphView is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License along with GraphView. If not,
 * see <http://www.gnu.org/licenses/lgpl.html>.
 * <p/>
 * Copyright Jonas Gehring
 */
package com.jrd.loan.graphview;

import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.jrd.loan.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jrd.loan.util.DensityUtil;

/**
 * Line Graph View. This draws a line chart.
 */
public class LineGraphView extends GraphView {

    private Paint paintBackground;
    private boolean drawBackground;
    private boolean drawDataPoints;
    private float dataPointsRadius = 10f;
    private float markerX;
    private float markerY;
    private String content;

    public LineGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineGraphView(Context context, String title) {
        super(context, title);
        init();
    }

    private void init() {
        paintBackground = new Paint();
        paintBackground.setColor(Color.rgb(20, 40, 60));
        paintBackground.setStrokeWidth(4);
        paintBackground.setAlpha(128);
    }

    @Override
    public void drawSeries(Canvas canvas, GraphViewDataInterface[] values, float graphwidth, float graphheight, float border, double minX, double minY, double diffX, double diffY, float horstart,
                           GraphViewSeriesStyle style) {
        // draw background
        double lastEndY = 0;
        double lastEndX = 0;
        // draw data
        paint.setStrokeWidth(style.thickness);
        paint.setColor(style.color);
        // 折线阴影部分
        Path bgPath = null;
        if (drawBackground) {
            bgPath = new Path();
        }
        lastEndY = 0;
        lastEndX = 0;
        float firstX = 0;
        for (int i = 0; i < values.length; i++) {
            double valY = values[i].getY() - minY;
            double ratY = valY / diffY;
            double y = graphheight * ratY;
            double valX = values[i].getX() - minX;
            double ratX = valX / diffX;
            double x = graphwidth * ratX;
            if (i > 0) {
                float startX = (float) lastEndX + (horstart + 1);
                float startY = (float) (border - lastEndY) + graphheight;
                float endX = (float) x + (horstart + 1);
                float endY = (float) (border - y) + graphheight;
                // draw data point
                if (drawDataPoints) {
                    // fix: last value was not drawn. Draw here now the end values
                    canvas.drawCircle(endX, endY, dataPointsRadius, paint);
                }
                canvas.drawLine(startX, startY, endX, endY, paint);
                if (bgPath != null) {
                    if (i == 1) {
                        firstX = startX;
                        bgPath.moveTo(startX, startY + style.thickness);
                    }
                    bgPath.lineTo(endX, endY + style.thickness);
                }
                // 保存下最后一个标点
                if (i == values.length - 1) {
                    markerX = endX;
                    markerY = endY;
                    DecimalFormat df = new DecimalFormat("0.000");
                    content = df.format(values[i].getY());
                }
            } else if (drawDataPoints) {
                // fix: last value not drawn as datapoint. Draw first point here, and then on every step the
                // end values (above)
                float first_X = (float) x + (horstart + 1);
                float first_Y = (float) (border - y) + graphheight;
                canvas.drawCircle(first_X, first_Y, dataPointsRadius, paint);
            }
            lastEndY = y;
            lastEndX = x;
        }
        if (bgPath != null) {
            // end / close path
            bgPath.lineTo((float) lastEndX + DensityUtil.dip2px(context, GraphViewConfig.BORDER / 2), graphheight + border);
            bgPath.lineTo(firstX, graphheight + border);
            bgPath.close();
            canvas.drawPath(bgPath, paintBackground);
        }
        // TODO 填充色 可配置
        paint.setColor(Color.rgb(250, 98, 65));
        // 先绘制橙色圆
        // TODO endY + style.thickness - 3
        canvas.drawCircle(markerX, markerY, DensityUtil.dip2px(context, GraphViewConfig.MARKER_MARGIN), paint);
        // 再绘制略小白色圆 盖住橙色圆上方
        paint.setColor(Color.WHITE);
        // TODO endY + style.thickness - 3
        canvas.drawCircle(markerX, markerY, DensityUtil.dip2px(context, GraphViewConfig.RECT_RADIUS), paint);
        // 绘制标记
        drawMarker(canvas, content, markerX, markerY + style.thickness);
    }

    private void drawMarker(Canvas canvas, String content, float x, float y) {
        Rect popupTextRect = new Rect();
        paint.getTextBounds(content, 0, content.length(), popupTextRect);
        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(250, 98, 65));
        // 创建marker
        RectF r = new RectF(x - popupTextRect.width() * 5 / 6 - GraphViewConfig.MARKER_MARGIN, y - DensityUtil.dip2px(context, GraphViewConfig.MARKER_HEIGHT_OFFSET), x + popupTextRect.width() * 1 / 6
                + DensityUtil.dip2px(context, GraphViewConfig.MARKER_MARGIN), y - DensityUtil.dip2px(context, GraphViewConfig.MARKER_HEIGHT_OFFSET / 2));
        canvas.drawRoundRect(r, DensityUtil.dip2px(context, GraphViewConfig.RECT_RADIUS), DensityUtil.dip2px(context, GraphViewConfig.RECT_RADIUS), paint);
        Path path = new Path();
        path.moveTo(x, y - DensityUtil.dip2px(context, GraphViewConfig.MARKER_HEIGHT_OFFSET / 2));
        path.lineTo(x, y - DensityUtil.dip2px(context, 10));
        path.lineTo(x - DensityUtil.dip2px(context, 5), y - DensityUtil.dip2px(context, GraphViewConfig.MARKER_HEIGHT_OFFSET / 2));
        path.close();
        canvas.drawPath(path, paint);
        paint.setColor(Color.WHITE);
        FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = (int) (r.top + (r.bottom - r.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(content, r.centerX(), baseline, paint);
    }

    public int getBackgroundColor() {
        return paintBackground.getColor();
    }

    public float getDataPointsRadius() {
        return dataPointsRadius;
    }

    public boolean getDrawBackground() {
        return drawBackground;
    }

    public boolean getDrawDataPoints() {
        return drawDataPoints;
    }

    /**
     * sets the background color for the series. This is not the background color of the whole graph.
     * @see #setDrawBackground(boolean)
     */
    @Override
    public void setBackgroundColor(int color) {
        paintBackground.setColor(color);
    }

    /**
     * sets the radius of the circles at the data points.
     * @see #setDrawDataPoints(boolean)
     * @param dataPointsRadius
     */
    public void setDataPointsRadius(float dataPointsRadius) {
        this.dataPointsRadius = dataPointsRadius;
    }

    /**
     * @param drawBackground true for a light blue background under the graph line
     * @see #setBackgroundColor(int)
     */
    public void setDrawBackground(boolean drawBackground) {
        this.drawBackground = drawBackground;
    }

    /**
     * You can set the flag to let the GraphView draw circles at the data points
     * @see #setDataPointsRadius(float)
     * @param drawDataPoints
     */
    public void setDrawDataPoints(boolean drawDataPoints) {
        this.drawDataPoints = drawDataPoints;
    }
}
