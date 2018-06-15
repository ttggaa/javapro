package com.kariqu.zwsrv.web.utilityex;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kariqu.zwsrv.web.persistance.service.WebDeliveryOrderService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityJson {

    //@Autowired
    //WebDeliveryOrderService dbService;

    private Map<Integer, City> allCity;
    private static CityJson instance;

    private City parseCity(JSONObject json_obj)
    {
        City city = new City();
        city.setDistrictId(json_obj.getInteger("districtId"));
        city.setShortName(json_obj.getString("shortName"));
        city.setfName(json_obj.getString("fName"));
        city.setPinyin(json_obj.getString("pingyin"));

        JSONArray json_array = json_obj.getJSONArray("list");
        Map<Integer, City> sub_array = new HashMap<Integer, City>();
        for (int i = 0; i != json_array.size(); ++i) {
            City sub_city = parseCity(json_array.getJSONObject(i));
            sub_array.put(sub_city.getDistrictId(), sub_city);
        }
        city.setList(sub_array);
        return  city;
    }

    private CityJson(){
    }

    public static CityJson getInstance(){
        if(instance==null){
            instance = new CityJson();
        }
        return instance;
    }

    public void init(WebDeliveryOrderService dbService) {
        if (allCity == null) {
            Map<Integer, String> config = dbService.loadWebConfig();
            String str = config.get(1);
            JSONArray province_array = JSONArray.parseArray(str);
            Map<Integer, City> city_list = new HashMap<Integer, City>();
            for (int i = 0; i != province_array.size(); ++i) {
                JSONObject province = province_array.getJSONObject(i);
                City city = parseCity(province);
                city_list.put(city.getDistrictId(), city);
            }

            /*  test
            City a = city_list.get(440000);
            City b = a.getList().get(445100);
            City c = b.getList().get(445103);
            System.out.println(String.format("xxxx->>>>>> json size:%d %s %s %s", city_list.size(),
                    a.getfName(), b.getfName(), c.getfName()
            ));
            */
            allCity = city_list;
        }
    }

    public List<String> getLocation(int province, int city, int district) {
        List<String> location = new ArrayList<String>();
        if (allCity == null){
            location.add("");
            location.add("");
            location.add("");
            return  location;
        }

        City province_slot = null;
        City city_slot = null;
        City district_slot = null;

        province_slot = allCity.get(province);
        if (province_slot == null) {
            location.add("");
        } else {
            location.add(province_slot.getfName());
            city_slot = province_slot.getList().get(city);
        }

        if (city_slot == null) {
            location.add("");
        } else {
            location.add(city_slot.getfName());
            district_slot = city_slot.getList().get(district);
        }

        if (district_slot == null) {
            location.add("");
        } else {
            location.add(district_slot.getfName());
        }
        return location;
    }

}
