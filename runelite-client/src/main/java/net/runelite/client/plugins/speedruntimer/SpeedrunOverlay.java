package net.runelite.client.plugins.speedruntimer;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.table.TableAlignment;
import net.runelite.client.ui.overlay.components.table.TableComponent;
import org.apache.commons.lang3.time.StopWatch;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.concurrent.TimeUnit;

@Singleton
class SpeedrunOverlay extends Overlay
{
	private final Client client;
	private final PanelComponent panelComponent = new PanelComponent();
	private final SpeedrunPlugin plugin;
	StopWatch stopWatch = new StopWatch();

	@Inject
	private SpeedrunOverlay(final Client client, final SpeedrunPlugin plugin) {
		super(plugin);
		this.plugin = plugin;
		this.client = client;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!stopWatch.isStarted() && stopWatch.getTime() < 1)
		{
			return null;
		}
		panelComponent.getChildren().clear();

		TableComponent tableComponent = new TableComponent();
		tableComponent.setColumnAlignments(TableAlignment.LEFT, TableAlignment.RIGHT);

		if (stopWatch.isStarted())
		{
			tableComponent.addRow("Zulrah: ", String.valueOf(stopWatch.getTime(TimeUnit.MILLISECONDS)));
		}
		if (stopWatch.isStopped())
		{
			tableComponent.addRow("Zulrah Killed:" , (stopWatch.getTime(TimeUnit.SECONDS) + 1) + " secs");
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
}
