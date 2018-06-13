package com.kariqu.zwsrv.app.service;

import com.kariqu.zwsrv.app.model.URLScheme;
import com.kariqu.zwsrv.thelib.persistance.entity.Notice;
import com.kariqu.zwsrv.thelib.persistance.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by simon on 27/11/17.
 */
@Service
public class NoticeBusinessService extends NoticeService {

//    1.充值
//    2.邀请
//    3.系统推送公告等信息(全部推送)

    public static final int kNoticeTypeDefault = 0;             // 默认
    public static final int kNoticeTypeBuyCoins = 10;
    public static final int kNoticeTypeBindInvitationCode = 20;
    public static final int kNoticeTypeExpressage = 30;         // 快递
    public static final int kNoticeSystemPush = 40;             // 系统推送
    public static final int kNoticeTypePlatform = 100;

    BlockingQueue<NoticeQueueItem> blockingQueue = new LinkedBlockingDeque<>(1000);
    WorkerThread thread;

    @Autowired
    UserCountServiceCache userCountService;

    @Autowired
    UmengPushService umengPushService;


    public NoticeBusinessService() {
        this.thread = new WorkerThread(blockingQueue);
        this.thread.start();
    }

    //充值成功,当前账户余额10娃娃币; 跳转到"充值账单"
    public void sendBuyCoinsNotification(int userId, int fee) {
        float fFee = ((float)(fee))/100.f;
        String strFee = "";
        if ((int)(fee%100)>0) {
            if ((int)(fee%10)>0) {
                strFee = String.format("%.2f", fFee);
            } else {
                strFee = String.format("%.1f", fFee);
            }
        } else {
            strFee = String.format("%d", (int)fFee);
        }
        String title = "充值成功";
        String content = "微信充值 " + strFee + " 元";
        sendNotification(kNoticeTypeBuyCoins, userId, title, content,
                title, URLScheme.kURLSchemeListBuyCoins());
    }

    //邀请成功
    public void sendBindInvitationCodeNotification(String inviteeNickName, int invitorId, int rewardCoins) {
        inviteeNickName=inviteeNickName!=null?inviteeNickName:"";
        String title = "邀请成功";
        String content = inviteeNickName+" 玩家成功绑定您的邀请码, 各获得 " + rewardCoins + " 娃娃币";
        sendNotification(kNoticeTypeBindInvitationCode, invitorId, title, content,
                title, URLScheme.kURLSchemeListCoins());
    }

    public void sendNotification(int type, int userId, String title, String content, String alert, String redirectURI) {
        Notice notice = new Notice();
        notice.setType(type);
        notice.setUserId(userId);
        notice.setTitle(title!=null?title:"");
        notice.setRedirectURI(redirectURI!=null?redirectURI:"");
        notice.setContent(content!=null?content:"");
        notice = super.save(notice);
        blockingQueue.add(new NoticeQueueItem(userId,alert,redirectURI));
    }

    public void sendBroadcastNotification(int type, String title, String content, String alert, String redirectURI) {
        Notice notice = new Notice();
        notice.setType(type);
        notice.setUserId(0);
        notice.setTitle(title!=null?title:"");
        notice.setRedirectURI(redirectURI!=null?redirectURI:"");
        notice.setContent(content!=null?content:"");
        notice = super.save(notice);
        blockingQueue.add(new NoticeQueueItem(0,alert,redirectURI));
    }

    public class WorkerThread extends Thread {

        private volatile boolean isRunning = true;
        private BlockingQueue<NoticeQueueItem> queue;

        public WorkerThread(BlockingQueue<NoticeQueueItem> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            super.run();
            try {
                while (isRunning) {
                    NoticeQueueItem item = queue.poll(5000, TimeUnit.MILLISECONDS);
                    if (item !=null) {
                        if (item.getUserId()>0) {
                            umengPushService.sendNotification(item.getUserId(),item.getTitle(),item.getRedirectURI(),0);
                        } else {
                            umengPushService.sendBroadcastNotification(item.getTitle(),item.getRedirectURI());
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class NoticeQueueItem {
        private int userId;
        private String title;
        private String redirectURI;

        public NoticeQueueItem(int userId, String title, String redirectURI) {
            this.userId=userId;
            this.title=title;
            this.redirectURI=redirectURI;
        }

        public String getTitle() {
            return title;
        }

        public String getRedirectURI() {
            return redirectURI;
        }

        public int getUserId() {
            return userId;
        }
    }
}
