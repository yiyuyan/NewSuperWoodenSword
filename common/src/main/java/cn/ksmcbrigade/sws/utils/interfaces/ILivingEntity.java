package cn.ksmcbrigade.sws.utils.interfaces;

import net.minecraft.world.entity.LivingEntity;

public interface ILivingEntity {
    void setZero();
    void dropLoot();
    void setAttacker(LivingEntity livingEntity);
    LivingEntity getAttacker();
    boolean allowModifyByte();
    void setCannotModify();
    boolean zero();
    void playerUnZero();
    void unsetRemoved_i();
    void updateAttr();
    void setTpAllow(boolean allow);
}
