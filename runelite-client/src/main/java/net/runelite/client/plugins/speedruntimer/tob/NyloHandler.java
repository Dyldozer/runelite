package net.runelite.client.plugins.speedruntimer.tob;

import lombok.Getter;

public class NyloHandler
{
	int current;
	@Getter
	String[] rows;

	public NyloHandler()
	{
		this.rows = new String[]
				{
						"75%:",
						"50%:",
						"25%:",
						"Killed:",
				};
		this.current = 0;
	}
}
