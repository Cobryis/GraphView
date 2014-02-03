package com.jjoe64.graphview;

import android.content.Context;
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
	public MultiBarGraphView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MultiBarGraphView(Context context, String title) {
		super(context, title);
	}
}
