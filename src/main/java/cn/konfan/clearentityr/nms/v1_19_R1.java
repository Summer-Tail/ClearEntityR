    package cn.konfan.clearentityr.nms;

public class v1_19_R1 extends Nms{
    @Override
    public String getNmsVersion() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getSaveIDMethodName() {
        return "bo";
    }

    @Override
    public String getNbtPathName() {
        return "net.minecraft.nbt.NBTTagCompound";
    }

    @Override
    public String getNBTMethodName() {
        return "g";
    }

    @Override
    public String getItemStackSaveMethodName() {
        return "b";
    }
}
