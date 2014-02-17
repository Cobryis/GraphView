package com.jjoe64.graphview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * Created by Cobryis on 2/2/14.
 *
 * This bar graph is for single to multiple series.
 * Horizontal labels must be set, and all series' data
 * must match the label count.
 */
public class MultiBarGraphView extends BarGraphView
{
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

	public void setFields()
	{

	}

	@Override
	public void drawSeries  (
								Canvas canvas,
	                            GraphViewSeries series,
	                            float graphwidth, float graphheight,
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

		int seriesCount = getSeriesCount();

		// columns will share a single field with other series' columns
		// and so they are smaller if there are more series
		float colWidth = graphwidth / (values.length * seriesCount);

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

			float offset = series.index * colWidth;

			float left = horstart + (i * colWidth * seriesCount) + offset;
			float top = (border - y) + graphheight;
			float right = left + colWidth - 1;
			canvas.drawRect(left, top, right, graphheight + border - 1, paint);
		}
	}

}
