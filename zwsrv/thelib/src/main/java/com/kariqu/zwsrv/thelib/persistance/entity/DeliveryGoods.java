package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.TimedEntity;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by simon on 03/11/17.
 */
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_delivery_goods")
public class DeliveryGoods extends TimedEntity {

    @Id
    @Column(name="order_goods_id", nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int orderGoodsId;

    @Column(name="order_id", nullable = false)
    private int orderId;

    @Column(name="type", nullable = false)
    private int type;

    @Column(name="idvalue", nullable = false)
    private int idvalue;

    @Column(name="item_id", nullable = false)
    private int itemId;

    @Column(name="image_url", nullable = false)
    private String imageUrl;

    @Column(name="name", nullable = false)
    private String name;

    public DeliveryGoods() {
        name="";
    }

    public int getOrderGoodsId() {
        return orderGoodsId;
    }

    public void setOrderGoodsId(int orderGoodsId) {
        this.orderGoodsId = orderGoodsId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIdvalue() {
        return idvalue;
    }

    public void setIdvalue(int idvalue) {
        this.idvalue = idvalue;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}


//CREATE TABLE `zww_delivery_goods` (
//        `order_goods_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
//        `order_id` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '',
//        `type` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '0默认为抓取的娃娃',
//        `idvalue` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT 'game_id',
//        `image_url` varchar(80) NOT NULL DEFAULT '',
//        `name` varchar(80) NOT NULL DEFAULT '',
//        `updatetime` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        `createtime` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        PRIMARY KEY (`order_goods_id`),
//        KEY `order_id` (`order_id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=2;
