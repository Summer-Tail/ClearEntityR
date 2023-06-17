    package cn.konfan.clearentityr.nms;

public class v1_17_R1 extends Nms{
    @Override
    public String getNmsVersion() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getSaveIDMethodName() {
        return "getSaveID";
    }

    @Override
    public String getNbtPathName() {
        return "net.minecraft.nbt.NBTTagCompound";
    }

    @Override
    public String getNBTMethodName() {
        return "get";
    }

    @Override
    public String getCraftItemStackSaveMethodName() {
        return "save";
    }
}
