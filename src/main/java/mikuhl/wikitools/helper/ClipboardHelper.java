package mikuhl.wikitools.helper;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class ClipboardHelper {

    private static Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    public static void setClipboard(NBTTagCompound nbt) {
        setClipboard(walkNBT(nbt));
    }

    private static String walkNBT(NBTTagCompound nbt) {
        StringBuilder stringbuilder = new StringBuilder("{");
        for (String key : nbt.getKeySet()) {
            NBTBase tag = nbt.getTag(key);
            if (tag instanceof NBTTagCompound) {
                stringbuilder.append(key + ":" + walkNBT(((NBTTagCompound) tag)));
            } else if (tag instanceof NBTTagByteArray) {
                byte[] byteArray = ((NBTTagByteArray) tag).getByteArray();
                StringBuilder string = new StringBuilder("[B;");
                for (byte b : byteArray) string.append(b + ",");
                string.setLength(string.length() - 1);
                stringbuilder.append(key + ":" + string.append("]").toString());
            } else {
                stringbuilder.append(key + ":" + tag);
            }
            stringbuilder.append(",");
        }
        stringbuilder.setLength(stringbuilder.length() - 1);
        stringbuilder.append("}");
        return stringbuilder.toString();
    }

    public static void setClipboard(String string) {
        clipboard.setContents(new StringSelection(string), null);
    }
}
