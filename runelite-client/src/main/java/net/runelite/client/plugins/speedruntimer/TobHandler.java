package net.runelite.client.plugins.speedruntimer;

import lombok.Getter;

public class TobHandler
{
	int current;
	@Getter
	String[] rows;

	public TobHandler()
	{
		this.rows = new String[]
				{
						"Maiden: 111.33(1:52)",
						"Bloat: 86.62(1:26)",
						"Nylo: 270.58(4:32)",
						"Sotetseg: 138.81(2:19)",
						"Xarpus: 152.14(2:32)",
						"Verzik: 293.57(4:48)",
						"Total Wave: 1053.05(17:30)",
				};
		this.current = 0;
	}
}
