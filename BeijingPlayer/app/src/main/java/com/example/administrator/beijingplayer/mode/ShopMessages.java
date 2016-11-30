package com.example.administrator.beijingplayer.mode;

import java.util.List;

/**
 * 商品的集合类
 */
public class ShopMessages {

    private List<ShopMessage> heng;

    private List<ShopMessage> shu;

    public ShopMessages() {
    }

    public ShopMessages(List<ShopMessage> heng, List<ShopMessage> shu) {
        this.heng = heng;
        this.shu = shu;
    }

    public List<ShopMessage> getHeng() {
        return heng;
    }

    public void setHeng(List<ShopMessage> heng) {
        this.heng = heng;
    }

    public List<ShopMessage> getShu() {
        return shu;
    }

    public void setShu(List<ShopMessage> shu) {
        this.shu = shu;
    }
}
