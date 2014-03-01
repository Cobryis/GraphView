package com.jjoe64.graphview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import junit.framework.Assert;

/**
 * Created by Cobryis on 2/2/14.
 *
 * This bar graph is for single to multiple series.
 * Horizontal labels must be set, and all series' data
 * must match the label count.
 */
public class MultiBarGraphView extends BarGraphView
{
	private Field[] dataFields;

	public MultiBarGraphView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public MultiBarGraphView(Context context, String title)
	{
		super(context, title);
		init();
	}

	private void init()
	{
		setManualYAxis(0.0);
	}

	/**
	 * Sets the horizontal fields of the graph.
	 * @param fieldNames
	 */
	public void setFields(String[] fieldNames)
	{
		staticHorizontalLabels = true;
		dataFields = new Field[fieldNames.length];
		for (int i = 0; i < fieldNames.length; ++i)
		{
			dataFields[i] = new Field(fieldNames[i]);
		}
	}

	@Deprecated
	@Override
	public void setHorizontalLabels(String[] horlabels) {
		setFields(horlabels);
	}

	@Override
	public void addSeries(GraphViewSeries series)
	{
		Assert.assertEquals("Series data length must match the number of fields in the graph!", series.values.length, dataFields.length);
		super.addSeries(series);
	}

	@Override
	protected void drawHorizontalLabels
	(
		Canvas canvas,
        float border,
        float horstart,
        float height,
        String[] unused,
        float graphwidth
	)
	{
		// horizontal labels + lines
		int segmentLines = dataFields.length + 2;
		for (int i = 0; i < dataFields.length; ++i)
		{
			paint.setColor(graphViewStyle.getGridColor());
			float x = ((graphwidth / (segmentLines - 1)) * (i + 1)) + horstart;
			canvas.drawLine(x, height - border, x, border, paint);

			dataFields[i].setAlignPosition(x);

			paint.setTextAlign(Paint.Align.CENTER);
			paint.setColor(graphViewStyle.getHorizontalLabelsColor());
			canvas.drawText(dataFields[i].getName(), x, height - 4, paint);
		}
	}

	// cache
	private float colWidth;
	private float baseAlignOffset;

	@Override
	protected void preDraw
	(
		float graphWidth
	)
	{
		colWidth = graphWidth / (dataFields.length + 2);
		baseAlignOffset = colWidth / 2f;
		colWidth /= getSeriesCount();
	}

	@Override
	public void drawSeries
	(
		Canvas canvas,
        GraphViewSeries series,
        float graphWidth, float graphheight,
        float border, float horstart
    )
	{
		double minY = getMinY();
		double diffY = getMaxY() - minY;

		// if min/max is the same, fake it so that we can render a line
		if (Double.compare(diffY, 0.0) == 0)
		{
			if (Double.compare(minY, 0.0) == 0)
			{
				// if both are zero, change the values to prevent division by zero
				diffY = 1.0;
			}
			else
			{
				diffY = minY * .1;
				minY *= .95;
			}
		}

		GraphViewDataInterface[] values = _values(series.index);

		paint.setStrokeWidth(series.style.thickness);

		// draw data
		for (int i = 0; i < values.length; i++)
		{
			// hook for value dependent color
			if (series.style.getValueDependentColor() != null)
			{
				paint.setColor(series.style.getValueDependentColor().get(values[i]));
			}
			else
			{
				paint.setColor(series.style.color);
			}

			float valY = (float) (values[i].getY() - minY);
			float ratY = (float) (valY / diffY);
			float y = graphheight * ratY;

			float left = horstart + dataFields[i].getAlignPosition() - baseAlignOffset + (series.index * colWidth);
			float top = (border - y) + graphheight;
			float right = left + colWidth - 1;
			canvas.drawRect(left, top, right, graphheight + border - 1, paint);
		}
	}

}
