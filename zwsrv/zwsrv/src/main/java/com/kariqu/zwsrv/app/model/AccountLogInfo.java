package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.thelib.model.BaseModel;
import com.kariqu.zwsrv.thelib.persistance.entity.AccountLog;
import com.kariqu.zwsrv.thelib.persistance.entity.AccountLogData;
import com.kariqu.zwsrv.thelib.persistance.service.AccountLogService;
import com.kariqu.zwsrv.thelib.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 09/12/17.
 */
public class AccountLogInfo extends BaseModel {

    //微信充值 10元   获得   100 钻 =>客户端拼
    //抓取     xx    消耗   100 钻 =>
    //抵运费   xx    消耗   100 钻 =>

    //xx 抓娃娃 +10
    //xx 抓娃娃 +10

    //xx 抵运费 -10
    //xx 兑换  -10
    //xx 抓娃娃  -10

    private int    logId;
    private String changedType; //微信充值, 抓娃娃,抵运费
    private int    changedNum;//+10,-10 (正:获得，负值:消耗)
    private String changedDesc;//如果是多个用“|”分割(比如寄快递的话，同时两个娃娃)
    private int    fee;//精确到分,账户变动涉及到的用户花费的钱(目前只在充值页面使用)
    private String imageUrl;//是多个用“|”分割(比如寄快递的话，同时两个娃娃)
    private long   timestamp;

    public AccountLogInfo() {
        changedType="";
        changedDesc="";
        imageUrl="";
    }

    public AccountLogInfo(AccountLog accountLog, boolean isForCoinsChangedLog) {

        List<AccountLogData> accountLogDataList = null;
        if (accountLog.getData()!=null&&accountLog.getData().length()>0) {
            accountLogDataList = JsonUtil.convertJson2ModelList(accountLog.getData(),AccountLogData.class);
        }
        if (accountLogDataList==null) {
            accountLogDataList=new ArrayList<>();
        }
        String changedDesc="";
        for (AccountLogData data : accountLogDataList) {
            if (changedDesc.length()>0) {
                changedDesc+="|";
            }
            changedDesc+=data.getName();
        }
        this.changedDesc=changedDesc;


        String imageUrl="";
        for (AccountLogData data : accountLogDataList) {
            if (imageUrl.length()>0) {
                imageUrl+="|";
            }
            imageUrl+=data.getImgUrl();
        }
        this.imageUrl=imageUrl;


        String changedTypeString = AccountLogService.changeTypeDescMap.get(
                isForCoinsChangedLog?accountLog.getCoinsChangedType():accountLog.getPointsChangedType());
        if (changedTypeString==null) {
            changedTypeString="";
        }
        this.changedType=changedTypeString;


        this.logId=accountLog.getLogId();
        this.changedNum=isForCoinsChangedLog?accountLog.getAvailableCoins():accountLog.getAvailablePoints();
        this.fee=accountLog.getFee();
        this.timestamp=accountLog.getTimestamp();
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getChangedType() {
        return changedType;
    }

    public void setChangedType(String changedType) {
        this.changedType = changedType;
    }

    public int getChangedNum() {
        return changedNum;
    }

    public void setChangedNum(int changedNum) {
        this.changedNum = changedNum;
    }

    public String getChangedDesc() {
        return changedDesc;
    }

    public void setChangedDesc(String changedDesc) {
        this.changedDesc = changedDesc;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}


