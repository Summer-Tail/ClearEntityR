/**
 * 多版本兼容 Nms
 */
package cn.konfan.clearentityr.nms;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;

public abstract class Nms {

    /**
     * 版本
     *
     * @return 版本名
     */
    public abstract String getNmsVersion();

    /**
     * Entity.getSaveID() 映射后方法名称
     *
     * @return 方法名称
     */
    public abstract String getSaveIDMethodName(); //net.minecraft.world.entity

    /**
     * ItemStack.save() 映射后方法名称
     *
     * @return 方法名称
     */
    public abstract String getCraftItemStackSaveMethodName(); //net.minecraft.world.item.ItemStack


    /**
     * NBTTagCompound 类包命
     *
     * @return 包名
     */
    public abstract String getNbtPathName();

    /**
     * NBTTagCompound.get() 映射后方法名称
     *
     * @return 方法名称
     */
    public abstract String getNBTMethodName();


    /**
     * 获取 entity 注册名称
     *
     * @param entity 实体
     * @return 实体注册名称
     */
    public String getSaveID(Entity entity) {
        try {
            Object craftEntity = Class.forName("org.bukkit.craftbukkit." + getNmsVersion() + ".entity.CraftEntity").cast(entity);
            Method getHandle = craftEntity.getClass().getMethod("getHandle");
            Object nmsEntity = getHandle.invoke(craftEntity);
            Object saveId = nmsEntity.getClass().getMethod(getSaveIDMethodName()).invoke(nmsEntity);
            return saveId == null ? "" : saveId.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取 itemStack 注册名称
     *
     * @param stack 物品
     * @return 物品注册名称
     */
    public String getItemID(ItemStack stack) {

        try {
            //ItemStack nmsItemStack =CraftItemStack.asNMSCopy(stack);
            Class<?> craftItemStack = Class.forName("org.bukkit.craftbukkit." + getNmsVersion() + ".inventory.CraftItemStack");
            Method asNMSCopy = craftItemStack.getMethod("asNMSCopy", ItemStack.class);
            Object nmsItemStack = asNMSCopy.invoke(asNMSCopy, stack);


            //nmsItemStack.save(new NBTTagCompound());
            Object id = null;
            try {
                Class<?> NBTTagCompoundClass = Class.forName(getNbtPathName());
                Method itemSave = nmsItemStack.getClass().getMethod(getCraftItemStackSaveMethodName(), NBTTagCompoundClass);
                Object nbt = itemSave.invoke(nmsItemStack, NBTTagCompoundClass.newInstance());
                Method nbtGet = nbt.getClass().getMethod(getNBTMethodName(), String.class);
                id = nbtGet.invoke(nbt, "id");
            } catch (ClassNotFoundException ignore) {
                //
            }

            return id == null ? "" : id.toString().replaceAll("\"", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
