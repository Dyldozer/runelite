package net.runelite.client.plugins.speedruntimer.tob;

import lombok.Getter;

public class BloatHandler
{
	int current;
	@Getter
	String[] rows;

	public BloatHandler()
	{
		this.rows = new String[]
				{
						"Bloat:",
				};
		this.current = 0;
	}
}
