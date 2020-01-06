package net.runelite.client.plugins.speedruntimer;


import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import javax.inject.Singleton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.border.EmptyBorder;

@Singleton
class TimerPanel extends PluginPanel
{

	JEditorPane editorPane = new JEditorPane();
	TimerPanel(SpeedrunPlugin plugin)
	{
		setBorder(new EmptyBorder(6, 6, 6, 6));
		setBackground(ColorScheme.DARK_GRAY_COLOR);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		editorPane.setEditable(false);
		this.add(editorPane);

		JButton reset = new JButton("Reset Time");
		reset.addActionListener(e ->
		{
			plugin.resetWatch();
		});
		this.add(reset);
	}

	void setEditorPane(String pane)
	{
		this.editorPane.setText(pane);
	}



}
