    package cn.konfan.clearentityr.nms;

public class v1_15_R1 extends Nms{
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
        return "net.minecraft.server." + getNmsVersion() + ".NBTTagCompound";
    }

    @Override
    public String getNBTMethodName() {
        return "get";
    }

    @Override
    public String getItemStackSaveMethodName() {
        return "save";
    }
}
