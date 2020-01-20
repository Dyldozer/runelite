package net.runelite.client.plugins.speedruntimer;


import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.table.TableAlignment;
import net.runelite.client.ui.overlay.components.table.TableComponent;
import net.runelite.client.util.ColorUtil;
import org.apache.commons.lang3.time.StopWatch;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.util.concurrent.TimeUnit;

@Singleton
class SpeedrunOverlay extends Overlay
{
	private final Client client;
	private final PanelComponent panelComponent = new PanelComponent();
	private final SpeedrunPlugin plugin;

	@Getter
	@Setter
	String[] rows = new String[0];

	StopWatch stopWatch = new StopWatch();
	@Getter
	@Setter
	private String title = "Theatre of Blood";
	@Getter
	@Setter
	private boolean onZulrah = false;
	private boolean zulrahLoaded = false;

	int width = 0;

	private String[] times = new String[0];

	@Setter
	private int current = 0;
	private boolean tobLoaded = false;

	@Inject
	private SpeedrunOverlay(final Client client, final SpeedrunPlugin plugin) {
		super(plugin);
		this.plugin = plugin;
		this.client = client;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		//if (!stopWatch.isStarted() && stopWatch.getTime() < 1)
		//{
		//	return null;
		//}
		//panelComponent.getChildren().clear();

		TableComponent tableComponent = new TableComponent();
		tableComponent.setColumnAlignments(TableAlignment.LEFT, TableAlignment.RIGHT);

		if (onZulrah && !zulrahLoaded)
		{
			rows = plugin.zulrahHandler.getRows();
			current = 0;
			times = new String[]{
					"0.00",
					"0.00",
					"0.00",
					"0.00",
			};
			zulrahLoaded = true;
		}

		String ms = Long.toString(stopWatch.getTime(TimeUnit.MILLISECONDS) % 1000);

		if (ms.length() == 1)
		{
			ms = "00";
		}
		else
		{
			ms = ms.substring(0,2);
		}

		String time = stopWatch.getTime(TimeUnit.SECONDS) + "." + ms;


		if (onZulrah)
		{
			int rowNumber = 0;
			for (String row: rows)
			{
				if (rowNumber == current)
				{
					tableComponent.addRow(ColorUtil.prependColorTag(row, Color.GREEN), ColorUtil.prependColorTag(time, Color.GREEN));
				}
				else
				{
					tableComponent.addRow(row, times[rowNumber]);
				}
				rowNumber++;
			}
		}

		if (stopWatch.isStopped())
		{

		}
		if (!tobLoaded)
		{
			tableComponent.addRow(title);
			tobLoaded = true;
			plugin.tobHandler = new TobHandler();
			rows = plugin.tobHandler.getRows();
			int rowNumber = 0;
			for (String row : rows) {
				if (graphics.getFontMetrics().stringWidth(row) > width) width = graphics.getFontMetrics().stringWidth(row);
				panelComponent.setPreferredSize(new Dimension(width, 0));
				tableComponent.addRow(row);

				rowNumber++;
			}
		}
		panelComponent.getChildren().add(tableComponent);

		return panelComponent.render(graphics);
	}

	void resetStopWatch()
	{
		stopWatch.reset();
	}
	void startStopWatch()
	{
		stopWatch.start();
	}
	void stopStopWatch()
	{
		stopWatch.stop();
	}
	public void setTimes(int row)
	{
		String ms = Long.toString(stopWatch.getTime(TimeUnit.MILLISECONDS) % 1000);

		if (ms.length() == 1)
		{
			ms = "00";
		}
		else
		{
			ms = ms.substring(0,2);
		}

		times[row] = stopWatch.getTime(TimeUnit.SECONDS) + "." + ms;
	}
}
