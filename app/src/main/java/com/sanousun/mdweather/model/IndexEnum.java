package com.sanousun.mdweather.model;

import com.sanousun.mdweather.R;

//Android中枚举的效率较低
public enum IndexEnum {
    ct("穿衣指数", R.drawable.index_ct),
    fs("防晒指数", R.drawable.index_fs),
    gm("感冒指数", R.drawable.index_gm),
    ls("晾晒指数", R.drawable.index_ls),
    xc("洗车指数", R.drawable.index_xc),
    yd("运动指数", R.drawable.index_yd);

    private String desc;
    private int icon;

    IndexEnum(String desc, int icon) {
        this.desc = desc;
        this.icon = icon;
    }

    public String getDesc() {
        return desc;
    }

    public int getIcon() {
        return icon;
    }
}
