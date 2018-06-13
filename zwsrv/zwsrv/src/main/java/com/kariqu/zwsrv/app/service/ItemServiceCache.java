package com.kariqu.zwsrv.app.service;

import com.kariqu.zwsrv.thelib.persistance.entity.Item;
import com.kariqu.zwsrv.thelib.persistance.service.ItemService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemServiceCache extends ItemService {

    // 道具类型
    //public static final int TYPE_WW = 0;              // 娃娃类型
    //public static final int TYPE_HUA_FEI = 1;         // 话费
    //public static final int TYPE_OTHER = 10;          // 其他类型

    // 我的背包
    public static final int CLASSIFY_ENTITY = 0;        // 实物
    public static final int CLASSIFY_HUA_FEI = 1;       // 话费

    // 我的碎片
    public static final int CLASSIFY_FRAGMENT = 100;    // 碎片

    // 道具来源类型
    public static final int ORIGIN_USER_CATCH = 0;      // 用户抓取
    public static final int ORIGIN_SYSTEM_PRESENT = 1;  // 系统赠送
    public static final int ORIGIN_MERGE = 2;           // 合成

    // 所有在背包中显示的分类
    public static Integer[] getBagClassify()
    {
        return new Integer[]{CLASSIFY_ENTITY,
                CLASSIFY_HUA_FEI};
    }

    public Map<Integer, Item> findItemAsMap()
    {
        Map<Integer, Item> ret = new HashMap<>();
        List<Item> list = findAll();
        if (list == null)
            return ret;
        for (Item i : list) {
            ret.put(i.getItemType(), i);
        }
        return ret;
    }
}
