package jp.kaiz.ibissp.event;

import jp.kaiz.ibissp.savepoint.SPType;
import jp.kaiz.ibissp.savepoint.SavePointManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class IbisSPEventLister implements Listener {
	private final Plugin plugin;

	public IbisSPEventLister(Plugin plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		String line = event.getLine(0);
		if (line == null) {
			return;
		}
		switch (line) {
			case "A":
				SavePointManager.changeSign(event, SPType.MIDAIR);
				break;
			case "B":
				SavePointManager.changeSign(event, SPType.GROUND);
				break;
			case "D":
				SavePointManager.changeSign(event, SPType.DIRECT);
				break;
		}
	}

	/**
	 * アイテムクリック時全部
	 */
	@EventHandler
	public void onPlayerClick(PlayerInteractEvent event) {
		Action action = event.getAction();
		if (event.getHand() != EquipmentSlot.HAND) {
			return;
		}

		if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
			Player player = event.getPlayer();
			Block clickedBlock = event.getClickedBlock();
			if (clickedBlock != null) {
				if (clickedBlock.getState() instanceof Sign) {
					Sign sign;
					if ((sign = ((Sign) clickedBlock.getState())).getLine(0).equals(SavePointManager.firstLine)) {
						String line3 = sign.getLine(3);
						if (line3.equals(SPType.DIRECT.getSignCharacter())) {
							SavePointManager.setSavePoint(player, player.getLocation(), sign.getLine(2), SPType.DIRECT);
						} else if (line3.equals(SPType.MIDAIR.getSignCharacter())) {
							SavePointManager.setSavePoint(player, sign.getLocation(), sign.getLine(2), SPType.MIDAIR);
						} else if (line3.equals(SPType.GROUND.getSignCharacter())) {
							if (player.isOnGround()) {
								SavePointManager.setSavePoint(player, player.getLocation(), sign.getLine(2), SPType.GROUND);
							} else {
								player.sendMessage("§l<§c§l<§4§l SP §c§l>§f§l> §c空中では更新できません。");
								return;
							}
						} else {
							return;
						}
						player.sendMessage(String.format("§l<§e§l<§6§l SP §e§l>§f§l> §aセーブポイントを設定しました。§7(%s§7)", sign.getLine(2)));
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
						return;
					}
				}
			}
			ItemStack mainHandItem = event.getPlayer().getInventory().getItemInMainHand();
			if (mainHandItem != null && mainHandItem.getType().equals(Material.PRISMARINE_SHARD)) {
				SavePointManager.teleportLastSavePoint(player);
			}
		}
	}
}
