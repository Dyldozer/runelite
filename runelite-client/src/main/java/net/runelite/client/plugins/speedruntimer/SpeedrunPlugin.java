package net.runelite.client.plugins.speedruntimer;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.AnimationID;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.Prayer;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.NPCManager;
import net.runelite.client.game.Sound;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.speedruntimer.boss.ZulrahHandler;
import net.runelite.client.plugins.zulrah.phase.ZulrahPhase;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.image.BufferedImage;

@PluginDescriptor(
		name = "Speedrun Timer",
		description = "Tracks splits and times for bosses",
		tags = {"time"},
		type = PluginType.UTILITY,
		enabledByDefault = false
)
@Singleton
@Slf4j
public class SpeedrunPlugin extends Plugin
{
	private TimerPanel panel;
	private NavigationButton navButton;
	@Inject
	private ClientToolbar clientToolbar;
	@Inject
	private Client client;

	@Inject
	private InfoBoxManager infoBoxManager;

	@Inject
	private OverlayManager overlayManager;

	@Getter
	@Setter
	@Inject
	private SpeedrunOverlay speedrunOverlay;

	int tick = 0;
	private boolean zulrahSpawned = false;
	private boolean zulrahAttacked = false;
	private boolean timerStarted = false;
	public ZulrahHandler zulrahHandler;
	public TobHandler tobHandler;
	int cHp = 100;
	private NPCManager npcManager;

	@Override
	protected void startUp()
	{

		overlayManager.add(speedrunOverlay);
		panel = new TimerPanel(this);

		final BufferedImage icon = ImageUtil.getResourceStreamFromClass(getClass(), "icon.png");

		navButton = NavigationButton.builder()
				.tooltip("Speedrun Timer")
				.icon(icon)
				.priority(6)
				.panel(panel)
				.build();

		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown()
	{
		clientToolbar.removeNavigation(navButton);
	}

	@Subscribe
	void onGameTick(GameTick event)
	{
		for (NPC npc : client.getNpcs())
		{
			if (npc.getName() != null && npc.getName().equals("Zulrah"))
			{
				float ratio = npc.getHealthRatio();
				int health = npc.getHealth();
				speedrunOverlay.setOnZulrah(true);
				if (health == -1) break;
				float hpPerc = (ratio * 100.0f) / 120.0f;
				System.out.println(hpPerc);
				if (hpPerc <= 25 && hpPerc > 1 && cHp > 25)
				{
					speedrunOverlay.setTimes(2);
					speedrunOverlay.setCurrent(3);
					cHp = 25;
				}
				else if (hpPerc <= 50 && cHp > 50)
				{
					speedrunOverlay.setTimes(1);
					speedrunOverlay.setCurrent(2);
					cHp = 50;
				}
				else if (hpPerc <= 75 && cHp > 75)
				{
					speedrunOverlay.setTimes(0);
					speedrunOverlay.setCurrent(1);
					cHp = 75;
				}

			}
		}
	}

	@Subscribe
	private void onAnimationChanged(AnimationChanged event)
	{

		final Actor actor = event.getActor();

		if (zulrahSpawned && actor.getAnimation() == 5069 && !timerStarted)
		{
			resetWatch();
			startStopWatch();
			timerStarted = true;
		}
	}

	@Subscribe
	private void onNpcSpawned(NpcSpawned npcSpawned)
	{
		if (npcSpawned.getNpc() == null)
		{
			return;
		}

		if (npcSpawned.getNpc().getId() == NpcID.ZULRAH)
		{
			zulrahSpawned = true;
			zulrahHandler = new ZulrahHandler();
		}
	}

	@Subscribe
	private void onNpcDespawned(NpcDespawned npcDespawned)
	{
		if (npcDespawned == null)
		{
			return;
		}

		if (npcDespawned.getNpc().getId() == NpcID.ZULRAH || npcDespawned.getNpc().getId() == NpcID.ZULRAH_2043 || npcDespawned.getNpc().getId() == NpcID.ZULRAH_2044)
		{
			speedrunOverlay.stopWatch.stop();
			zulrahSpawned = false;
			timerStarted = false;
			speedrunOverlay.setTimes(3);
			speedrunOverlay.setOnZulrah(false);
		}
	}

	public void resetWatch() {
		speedrunOverlay.resetStopWatch();
	}
	public void startStopWatch() {
		speedrunOverlay.startStopWatch();
	}
	public void stopStopWatch() {
		speedrunOverlay.stopStopWatch();
	}
}