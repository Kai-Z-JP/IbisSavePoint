package jp.kaiz.ibissp.savepoint;

import jp.kaiz.ibissp.utils.SQLManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SavePointManager {
	public static final String firstLine = ChatColor.BLUE + "╣" + ChatColor.DARK_BLUE + "╠ SavePoint ╣" + ChatColor.BLUE + "╠";

	public static void changeSign(SignChangeEvent sign, SPType type) {
		String spName;
		if (!(spName = sign.getLine(1)).isEmpty()) {
			sign.setLine(0, firstLine);
			sign.setLine(1, ChatColor.DARK_GRAY + "right click");
			sign.setLine(2, spName);
			sign.setLine(3, type.getSignCharacter());
		}
	}

	public static void teleportLastSavePoint(Player player) {
		Location location;
		if ((location = getLastSavePointLocation(player)) == null) {
			player.sendMessage("SPが存在しません");
		} else {
			player.teleport(location);
		}
	}

	private static Location getLastSavePointLocation(Player player) {
		try {
			Connection conn = SQLManager.getSQLiteLib().getDatabase(SQLManager.SAVE_POINT_DATA).getSQLConnection();
			PreparedStatement prep = conn.prepareStatement("SELECT * FROM spdata WHERE world = ? AND uuid = ?");
			prep.setString(1, player.getWorld().getName());
			prep.setString(2, player.getUniqueId().toString());
			ResultSet rs = prep.executeQuery();
			if (rs.isClosed()) {
				return null;
			} else {
				double x, y, z;
				float yaw, pitch;
				if (Double.isNaN(x = rs.getDouble("x"))) {
					return null;
				}
				if (Double.isNaN(y = rs.getDouble("y"))) {
					return null;
				}
				if (Double.isNaN(z = rs.getDouble("z"))) {
					return null;
				}
				if (Float.isNaN(yaw = rs.getFloat("yaw"))) {
					return null;
				}
				if (Float.isNaN(pitch = rs.getFloat("pitch"))) {
					return null;
				}
				if (rs.wasNull()) {
					yaw = player.getLocation().getYaw();
					pitch = player.getLocation().getPitch();
					x = x + 0.5;
					z = z + 0.5;
				}
				prep.close();
				conn.close();
				return new Location(player.getWorld(), x, y, z, yaw, pitch);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void setSavePoint(Player player, Location location, String name, SPType type) {
		try {
			Connection conn = SQLManager.getSQLiteLib().getDatabase(SQLManager.SAVE_POINT_DATA).getSQLConnection();
			PreparedStatement prep1 = conn.prepareStatement("DELETE FROM spdata WHERE uuid = ? AND world = ?");
			prep1.setString(1, player.getUniqueId().toString());
			prep1.setString(2, player.getWorld().getName());
			prep1.execute();
			prep1.close();
			PreparedStatement prep2 = conn.prepareStatement("INSERT INTO spdata values(?,?,?,?,?,?,?,?)");
			prep2.setString(1, player.getUniqueId().toString());
			prep2.setString(2, location.getWorld().getName());
			prep2.setString(3, name);
			prep2.setDouble(5, location.getY());
			prep2.setDouble(4, location.getX());
			prep2.setDouble(6, location.getZ());
			if (type != SPType.MIDAIR) {
				prep2.setFloat(7, location.getYaw());
				prep2.setFloat(8, location.getPitch());
			}
			prep2.execute();
			prep2.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
