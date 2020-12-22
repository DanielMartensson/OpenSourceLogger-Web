package se.danielmartensson.tools;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.XAxis;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.DataLabelsBuilder;
import com.github.appreciated.apexcharts.config.builder.GridBuilder;
import com.github.appreciated.apexcharts.config.builder.LegendBuilder;
import com.github.appreciated.apexcharts.config.builder.StrokeBuilder;
import com.github.appreciated.apexcharts.config.builder.TitleSubtitleBuilder;
import com.github.appreciated.apexcharts.config.builder.TooltipBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.builder.YAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.AnimationsBuilder;
import com.github.appreciated.apexcharts.config.chart.builder.ToolbarBuilder;
import com.github.appreciated.apexcharts.config.chart.builder.ZoomBuilder;
import com.github.appreciated.apexcharts.config.grid.builder.RowBuilder;
import com.github.appreciated.apexcharts.config.stroke.Curve;
import com.github.appreciated.apexcharts.config.subtitle.Align;
import com.github.appreciated.apexcharts.config.yaxis.builder.TitleBuilder;
import lombok.Data;
import se.danielmartensson.views.MySQLView;

@Data
/**
 * This class is made for creating the charts. It's just a basic template.
 * 
 * @author dell
 *
 */
public class Graf {

	private ApexCharts apexChart;
	private XAxis xAxis;

	public Graf(String title) {
		apexChart = ApexChartsBuilder.get()
				.withChart(ChartBuilder.get()
						.withType(Type.line)
						.withZoom(ZoomBuilder.get()
								.withEnabled(true)
								.build())
						.withToolbar(ToolbarBuilder.get()
								.withShow(true)
								.build())
						.withAnimations(AnimationsBuilder.get()
								.withEnabled(false)
								.build())
						.build())
				.withLegend(LegendBuilder.get()
						.withShow(true)
						.build())
				.withDataLabels(DataLabelsBuilder.get()
						.withEnabled(false)
						.build())
				.withColors("#48912c", "#13ebd5", "#215ed9", "#e6c222", "#a524e0", "#633326") // PWM1, PWM2, PMW4, Temp1, Temp2
				.withTooltip(TooltipBuilder.get()
						.withEnabled(false)
						.build())
				.withStroke(StrokeBuilder.get()
						.withCurve(Curve.straight)
						.build())
				.withTitle(TitleSubtitleBuilder.get()
						.withText(title)
						.withAlign(Align.left)
						.build())
				.withGrid(GridBuilder.get()
						.withRow(RowBuilder.get()
								.withColors("#f3f3f3", "transparent")
								.withOpacity(0.5)
								.build())
						.build())
				.withYaxis(YAxisBuilder.get()
						.withTitle(TitleBuilder.get()
								.withText("Measurements")
								.build())
						.build())
				.withXaxis(XAxisBuilder.get()
						.withTitle(com.github.appreciated.apexcharts.config.xaxis.builder.TitleBuilder.get()
								.withText("Time")
								.build())
						.withCategories("")
						.build())
				.withSeries(
						MySQLView.createSerie(new Float[] { 0f }, "analog"))
				.build();
		apexChart.setWidthFull();

	}
}
