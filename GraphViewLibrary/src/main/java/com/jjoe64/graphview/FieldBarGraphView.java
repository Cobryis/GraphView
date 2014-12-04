package com.jjoe64.graphview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import junit.framework.Assert;

/**
 * Created by Cobryis on 2/2/14.
 *
 * This bar graph is for single to multiple series.
 * Horizontal labels must be set, and all series' data
 * must match the label count.
 */
public class FieldBarGraphView extends BarGraphView
{
	private Field[] dataFields;

	private boolean graphIsPressed = false;

	public FieldBarGraphView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public FieldBarGraphView(Context context)
	{
		super(context);
		init();
	}

	public interface FieldClickedListener
	{
		void OnClick(int fieldIndex);
	}

	private FieldClickedListener fieldClickedListener;

	public void setOnFieldClickedListener(FieldClickedListener fieldClickListener)
	{
		this.fieldClickedListener = fieldClickListener;
	}

	private void init()
	{
		setManualYAxis(0.0);

		graphViewContentView.setOnTouchListener(new OnTouchListener()
		{
			Field selectedField = null;
			int selectedFieldIndex = 0;
			float selectDist = 0.0f;

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{

				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					float x = event.getX();

					// find selected series
					selectDist = seriesWidth / 2.0f;
					for (int i = 0; i < dataFields.length; ++i)
					{
						Field field = dataFields[i];
						if (Math.abs(x - field.getAlignPosition()) < selectDist)
						{
							field.isSelected = true;
							selectedField = field;
							selectedFieldIndex = i;
						}
						else
						{
							field.isSelected = false;
						}
					}

					graphIsPressed = true;
				}
				else if (event.getAction() == MotionEvent.ACTION_UP)
				{
					if (selectedField != null)
					{
						float x = event.getX();

						// if we're not on the series, deselect it
						if (Math.abs(x - selectedField.getAlignPosition()) > selectDist)
						{
							selectedField.isSelected = false;
							selectedField = null;
						}
						else if (fieldClickedListener != null) // else, inform of click
						{
								fieldClickedListener.OnClick(selectedFieldIndex);
						}
					}
					graphIsPressed = false;
				}

				return false;
			}
		});
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
        float horStart,
        float height,
        String[] unused,
        float graphWidth
	)
	{
		// horizontal labels + lines
		int segmentLines = dataFields.length + 1;
		for (int i = 0; i < dataFields.length; ++i)
		{
			paint.setColor(graphViewStyle.getGridColor());
			float x = ((graphWidth / (segmentLines)) * (i + 1)) + horStart;
			// canvas.drawLine(x, height - border, x, border, paint);

			dataFields[i].setAlignPosition(x);

			paint.setTextAlign(Paint.Align.CENTER);
			paint.setColor(graphViewStyle.getHorizontalLabelsColor());
			canvas.drawText(dataFields[i].getName(), x, height - 4, paint);
		}
	}

	// cache
	private float seriesWidth;
	private float barWidth;

	@Override
	protected void preDraw
	(
		float graphWidth
	)
	{
		seriesWidth = graphWidth / (dataFields.length + 2);
		barWidth = seriesWidth / getSeriesCount();
	}

	@Override
	public void drawSeries
	(
		Canvas canvas,
        GraphViewSeries series,
        float graphWidth, float graphHeight,
        float border, float horStart
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

		float baseAlignOffset = seriesWidth / 2.0f;

		// draw data
		for (int i = 0; i < values.length; i++)
		{
			Field curField = dataFields[i];

			if (!graphIsPressed || curField.isSelected)
			{
				paint.setColor(series.style.color);
			}
			else
			{
				paint.setColor(0xFF888888);
			}

			float valY = (float) (values[i].getY() - minY);
			float ratY = (float) (valY / diffY);
			float y = graphHeight * ratY;

			float left = horStart + dataFields[i].getAlignPosition() - baseAlignOffset + (series.index * barWidth);
			float top = (border - y) + graphHeight;
			float right = left + barWidth - 1;
			canvas.drawRect(left, top, right, graphHeight + border - 1, paint);
		}
	}

}
