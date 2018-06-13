package com.kariqu.zwsrv.app.controller;

import com.kariqu.zwsrv.app.Application;
import com.kariqu.zwsrv.app.error.ErrorDefs;
import com.kariqu.zwsrv.app.model.UserCardInfo;
import com.kariqu.zwsrv.app.model.UserCardRewardInfo;
import com.kariqu.zwsrv.app.service.AccountBusinessService;
import com.kariqu.zwsrv.app.service.CardServiceCache;
import com.kariqu.zwsrv.app.service.UserCardServiceCache;
import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.http.HttpRequestContext;
import com.kariqu.zwsrv.thelib.model.ErrorResponse;
import com.kariqu.zwsrv.thelib.model.ResponseData;
import com.kariqu.zwsrv.thelib.persistance.entity.Card;
import com.kariqu.zwsrv.thelib.persistance.entity.UserCard;
import com.kariqu.zwsrv.thelib.persistance.service.CardService;
import com.kariqu.zwsrv.thelib.persistance.service.UserCardService;
import com.kariqu.zwsrv.thelib.security.SecurityUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("card/v1")
public class UserCardController extends BaseController {

    private class PackageUserCard
    {
        public PackageUserCard(UserCard c)
        {
            this.userCard = c;
        }

        public UserCard userCard;
        public long remainTime;
    }

    @Autowired
    private UserCardServiceCache userCardServiceCache;

    @Autowired
    private AccountBusinessService accountBusinessService;

    @Autowired
    private CardServiceCache cardServiceCache;

    private void classifyList(List<UserCard> all, List<PackageUserCard> weekly, List<PackageUserCard> monthly)
    {
        for (UserCard card : all) {
            int type = card.getCardType();
            if (type == CardService.CARD_TYPE_WEEKLY) {
                weekly.add(new PackageUserCard(card));
            } else if (type == CardService.CARD_TYPE_MONTHLY) {
                monthly.add(new PackageUserCard(card));
            }
        }

        long tnow = System.currentTimeMillis();
        for (PackageUserCard c : weekly) {
            long remainTime = calcRemainTime(c.userCard, tnow);
            if (remainTime < 0)
                remainTime = 0;
            c.remainTime = remainTime;
        }

        for (PackageUserCard c : monthly) {
            long remainTime = calcRemainTime(c.userCard, tnow);
            if (remainTime < 0)
                remainTime = 0;
            c.remainTime = remainTime;
        }

        // 按可以领取时间排序
        Comparator<PackageUserCard> cmp = new Comparator<PackageUserCard>() {
            @Override
            public int compare(PackageUserCard o1, PackageUserCard o2) {
                if (o1.remainTime < o2.remainTime)
                    return 1;
                else if (o1.remainTime == o2.remainTime)
                    return 0;
                else
                    return -1;
            }
        };

        weekly.sort(cmp);
        monthly.sort(cmp);
    }

    private List<UserCard> findEarlist(List<UserCard> card_list)
    {
        // 获取最早能领取的卡
        Map<Integer, UserCard> card_map = new HashedMap();
        for (UserCard card : card_list) {
            int type = card.getCardType();
            UserCard temp = card_map.get(type);
            if (temp == null) {
                card_map.put(type, card);
            } else {
                if (temp.getRewardDatetime().compareTo(card.getRewardDatetime()) < 0) {
                    card_map.put(type, temp);
                }
            }
        }

        List<UserCard> ret = new ArrayList<>();
        for (Map.Entry<Integer, UserCard> e : card_map.entrySet()) {
            ret.add(e.getValue());
        }
        return ret;
    }

    long calcRemainTime(UserCard user_card, long currentMs)
    {
        long next_reward_time = user_card.getRewardDatetime().getTime() + (UserCardService.CARD_REWARD_SECONDS * 1000);
        return next_reward_time - currentMs;
    }

    @RequestMapping(value="/get_user_card")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData getCard(HttpServletRequest request,
                               @RequestParam Map<String,String> allRequestParams,
                               HttpServletResponse response) {
        int currentUserId = 0;
        currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }

        try {
            List<UserCard> user_card_list = userCardServiceCache.findValidCard(currentUserId, new Date());
            /*
            if (!user_card_list.isEmpty()) {
                user_card_list = findEarlist(user_card_list);
            }
            */

            List<PackageUserCard> weekly = new ArrayList<>();
            List<PackageUserCard> monthly = new ArrayList<>();
            classifyList(user_card_list, weekly, monthly);

            int weekly_reward = 0;
            int monthly_reward = 0;
            List<Card> card_list = cardServiceCache.findAll();
            for (Card c : card_list) {
                if (c.getCardType() == CardService.CARD_TYPE_WEEKLY)
                    weekly_reward = c.getExtraCoins();
                else if (c.getCardType() == CardService.CARD_TYPE_MONTHLY)
                    monthly_reward = c.getExtraCoins();
            }

            List<UserCardInfo> card_info_list = new ArrayList<>();
            for (PackageUserCard c : weekly) {
                card_info_list.add(new UserCardInfo(c.userCard, c.remainTime, weekly_reward));
            }
            for (PackageUserCard c : monthly) {
                card_info_list.add(new UserCardInfo(c.userCard, c.remainTime, monthly_reward));
            }
            return new ResponseData().put("list", card_info_list);
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }

    @RequestMapping(value="/draw_reward")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData drawReward(HttpServletRequest request,
                               @RequestParam Map<String,String> allRequestParams,
                               HttpServletResponse response)
    {
        int userId = SecurityUtil.currentUserId();
        if (userId==0) {
            return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }

        try {
            HttpRequestContext requestContext = new HttpRequestContext(request,allRequestParams);
            requestContext.validateInputParams("card_type");
            int card_type = requestContext.getInegerValue("card_type");

            if (!cardServiceCache.isValidCardType(card_type)) {
                return new ErrorResponse(ErrorDefs.ERROR_CARD_UNKNOWN_TYPE);
            }

            Date tnow = new Date();
            List<UserCard> user_card_list = userCardServiceCache.findAllCanRewardCardByType(userId, card_type
                    , tnow, (int)UserCardService.CARD_REWARD_SECONDS);
            if (user_card_list.isEmpty()) {
                Application.getLog().debug("can't find card_id uid: {} card_type: {}", userId, card_type);
                return new ErrorResponse(ErrorDefs.ERROR_CARD_NONE_CAN_REWARD);
            }

            Map<Integer, Card> card_rewards = cardServiceCache.findAllAsMap();
            List<UserCardRewardInfo> result_list = new ArrayList<>();
            for (UserCard uc : user_card_list) {
                Card reward = card_rewards.get(uc.getCardType());
                if (reward == null) {
                    Application.getLog().error("card reward is null! user_id: {} card_type: {} card_id: {}",
                            userId, uc.getCardType(), uc.getCardId());
                    continue;
                }

                // 先保存用户卡片数据
                uc.setRewardDatetime(tnow);
                userCardServiceCache.save(uc);
                Application.getLog().info("draw_reward saveUserCard user_id: {} card_id: {}", userId, uc.getCardId());

                // 后保存金币和日志
                accountBusinessService.rewardForCard(userId, reward.getExtraCoins(), uc.getCardId(), uc.getCardType());
                Application.getLog().info("draw_reward rewardForCard user_id: {} card_id: {} card_type: {} coins: {} points: {}"
                        , userId, uc.getCardId(), uc.getCardType(), reward.getExtraCoins(), reward.getExtraPoints());

                result_list.add(new UserCardRewardInfo(reward.getExtraCoins(), reward.getExtraPoints()));
            }
            return new ResponseData().put("list", result_list);
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }
}
