package jp.kaiz.ibissp.savepoint;

import org.bukkit.ChatColor;

public enum SPType {
	GROUND {
		private final String signCharacter = ChatColor.DARK_GREEN + "╣" + ChatColor.GREEN + "╠ ground ╣" + ChatColor.DARK_GREEN + "╠";

		@Override
		public String getSignCharacter() {
			return signCharacter;
		}
	}, MIDAIR {
		private final String signCharacter = ChatColor.RED + "╣" + ChatColor.DARK_RED + "╠ midair ╣" + ChatColor.RED + "╠";

		@Override
		public String getSignCharacter() {
			return signCharacter;
		}
	}, DIRECT {
		private final String signCharacter = ChatColor.LIGHT_PURPLE + "╣" + ChatColor.DARK_PURPLE + "╠ direct ╣" + ChatColor.LIGHT_PURPLE + "╠";

		@Override
		public String getSignCharacter() {
			return signCharacter;
		}
	};

	public abstract String getSignCharacter();
}