    package cn.konfan.clearentityr.nms;

public class v1_20_R1 extends Nms{
    @Override
    public String getNmsVersion() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getSaveIDMethodName() {
        return "br";
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
    public String getCraftItemStackSaveMethodName() {
        return "b";
    }
}
