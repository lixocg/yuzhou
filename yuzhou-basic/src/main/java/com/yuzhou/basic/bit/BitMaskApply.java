package com.yuzhou.basic.bit;

/**
 * 位掩码适应
 * 场景：在一个系统中，用户一般有查询(Select)、新增(Insert)、修改(Update)、删除(Delete)四种权限，四种权限有多种组合方式，也就是有16中不同的权限状态（2的4次方）。
 */
public class BitMaskApply {

    public static class Perm {

        public static final int SELECT = 1 << 0;//1

        public static final int INSERT = 1 << 1;//2

        public static final int UPDATE = 1 << 2;//4

        public static final int DELETE = 1 << 3;//8

        /**
         * 当前权限
         */
        private int flag;

        /**
         * 重新设置权限
         *
         * @param perm
         */
        public void setPerm(int perm) {
            this.flag = perm;
        }

        /**
         * 增加一项或多项权限
         */
        public void addPerm(int... perms) {
            for (int perm : perms) {
                flag |= perm;
            }
        }

        public void delPerm(int... perms) {
            for (int perm : perms) {
                flag &= ~perm;
            }
        }

        public boolean hasPerm(int perm) {
            return (flag & perm) == perm;
        }

        /**
         * 是否禁用了某些权限
         */
        public boolean isNotAllow(int permission) {
            return (flag & permission) == 0;
        }

        /**
         * 是否仅仅拥有某些权限
         */
        public boolean isOnlyAllow(int permission) {
            return flag == permission;
        }
    }


    public static void main(String[] args) {
        Perm perm = new Perm();
        perm.addPerm(Perm.INSERT, Perm.UPDATE);
        System.out.println(String.format("添加权限:flag = %d, flagBit = %s", perm.flag, Integer.toBinaryString(perm.flag)));

        perm.delPerm(Perm.INSERT);
        System.out.println(String.format("删除权限:flag = %d, flagBit = %s", perm.flag, Integer.toBinaryString(perm.flag)));

        perm.delPerm(Perm.INSERT);
        System.out.println(String.format("删除权限:flag = %d, flagBit = %s", perm.flag, Integer.toBinaryString(perm.flag)));

        boolean b = perm.hasPerm(Perm.INSERT);
        System.out.println("是否有INSERT权限，" + b);


    }

}
