package jp.kaiz.ibissp;

import jp.kaiz.ibissp.event.IbisSPEventLister;
import jp.kaiz.ibissp.utils.SQLManager;
import org.bukkit.plugin.java.JavaPlugin;

public class IbisSP extends JavaPlugin {
	@Override
	public void onEnable() {
		new IbisSPEventLister(this);
		new SQLManager(this);
	}

	@Override
	public void onDisable() {
	}
}
