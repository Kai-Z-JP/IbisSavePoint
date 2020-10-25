package jp.kaiz.ibissp.utils;

import com.pablo67340.SQLiteLib.Main.SQLiteLib;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class SQLManager {
	public final static String SAVE_POINT_DATA = "SavePointData";
	private static SQLiteLib sqlLib;

	public SQLManager(Plugin plugin) {
		File f = new File(plugin.getDataFolder() + "/");
		if (!f.exists()) {
			f.mkdir();
		}
		sqlLib = SQLiteLib.hookSQLiteLib(plugin);
		sqlLib.initializeDatabase(plugin, SAVE_POINT_DATA, "CREATE TABLE IF NOT EXISTS spdata(uuid text, world text, name text, x real, y real, z real, yaw real, pitch real)");
	}

	public static SQLiteLib getSQLiteLib() {
		return sqlLib;
	}
}
