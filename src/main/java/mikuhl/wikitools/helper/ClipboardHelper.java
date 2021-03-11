package mikuhl.wikitools.helper;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import mikuhl.wikitools.WikiTools;

public class ClipboardHelper {
	
	private static Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	
	public static void setClipboard(NBTTagCompound nbt) {
		clipboard.setContents(new StringSelection(walkNBT(nbt)), null);
		WikiTools.sendMessage("&a[WikiTools] &fNBT tag copied to your clipboard.");
	}
	
	private static String walkNBT(NBTTagCompound nbt) {
		StringBuilder stringbuilder = new StringBuilder("{");
		for (String key : nbt.getKeySet()) {
			NBTBase tag = nbt.getTag(key);
			if (tag instanceof NBTTagCompound) {
				stringbuilder.append(key + ":" + walkNBT(((NBTTagCompound) tag)));
			}
			else if (tag instanceof NBTTagByteArray) {
				byte[] byteArray = ((NBTTagByteArray) tag).getByteArray();
				StringBuilder string = new StringBuilder("[B;");
				for (byte b : byteArray) string.append(b + ",");
				string.setLength(string.length() - 1);
				stringbuilder.append(key + ":" + string.append("]").toString());
			}
			else {
				stringbuilder.append(key + ":" + tag);
			}
			stringbuilder.append(",");
		}
		stringbuilder.setLength(stringbuilder.length() - 1);
		stringbuilder.append("}");
		return stringbuilder.toString();
	}
}