package net.cobryis.test;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jjoe64.graphview.FieldBarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewStyle;

import java.util.Random;


public class MainActivity extends ActionBarActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);

	    FieldBarGraphView graph = new FieldBarGraphView(getApplicationContext());
	    graph.setTitle("Some Title");

	    String[] names = {"One", "Two", "Three", "Four", "Five", "Six"};
	    graph.setFields(names);

	    applyGraphStyle(graph);

	    // we set the horizontal labels, which will describe the data of the bars above
	    // data should correspond perfectly to the labels
	    // this means the data's x value does not matter, but instead it's index matters
	    Random rand = new Random();
	    GraphViewSeries.GraphViewSeriesStyle style;

	    // series 1
	    {
		    GraphView.GraphViewData[] data1 = new GraphView.GraphViewData[names.length];

		    for (int i = 0; i < data1.length; ++i)
		    {
			    data1[i] = new GraphView.GraphViewData(i, rand.nextFloat() * 10);
		    }
		    style = new GraphViewSeries.GraphViewSeriesStyle(getResources().getColor(android.R.color.holo_orange_light), 10);

		    GraphViewSeries series1 = new GraphViewSeries("Series 1", style, data1);
		    graph.addSeries(series1);
	    }

	    // series 2
	    {
		    GraphView.GraphViewData[] data2 = new GraphView.GraphViewData[names.length];

		    for (int i = 0; i < data2.length; ++i)
		    {
			    data2[i] = new GraphView.GraphViewData(i, rand.nextFloat() * 11);
		    }
		    style = new GraphViewSeries.GraphViewSeriesStyle(getResources().getColor(android.R.color.holo_blue_bright), 10);

		    GraphViewSeries series2 = new GraphViewSeries("Series 2", style, data2);
		    graph.addSeries(series2);
        }

		// series 3
	    {
		    GraphView.GraphViewData[] data3 = new GraphView.GraphViewData[names.length];

		    for (int i = 0; i < data3.length; ++i)
		    {
			    data3[i] = new GraphView.GraphViewData(i, rand.nextFloat() * 10);
		    }
		    style = new GraphViewSeries.GraphViewSeriesStyle(getResources().getColor(android.R.color.holo_red_light), 10);

		    GraphViewSeries series3 = new GraphViewSeries("Series 3", style, data3);
		    graph.addSeries(series3);
	    }

	    LinearLayout layout = (LinearLayout) findViewById(R.id.container);
	    layout.addView(graph);

	    graph.setOnFieldClickedListener(new FieldBarGraphView.FieldClickedListener()
	    {
		    @Override
		    public void OnClick(int fieldIndex)
		    {
			    int duration = Toast.LENGTH_SHORT;

			    Toast toast = Toast.makeText(getApplicationContext(), "Selected Field: " + fieldIndex, duration);
			    toast.show();
		    }
	    });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	private void applyGraphStyle(GraphView graph)
	{
		GraphViewStyle style = graph.getGraphViewStyle();
		style.setLegendWidth(500);
		graph.setScrollable(true);

		graph.setShowLegend(true);
		graph.setLegendAlign(LegendAlign.TOP);

		style.setLegendBorder(20);
		style.setLegendSpacing(30);
		style.setLegendWidth(300);
		// style.setVerticaLabelsShouldRound(true);
		style.setVerticalLabelsDecimalCount(2);
	}
    /**
     * A placeholder fragment containing a simple view.
     */
    /*
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
	*/
}
