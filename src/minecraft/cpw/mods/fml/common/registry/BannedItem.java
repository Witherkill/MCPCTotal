package cpw.mods.fml.common.registry;

public class BannedItem {

    public final int blockID;
    public final int meta;

    public BannedItem(int blockID, int meta) {
        this.blockID = blockID;
        this.meta = meta;
    }
}
