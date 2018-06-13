package com.kariqu.zwsrv.thelib.persistance.entity;

public class AccountLogExtraData {

    // 领取周卡，月卡的json数据
    public class JsonCardReward
    {
        public int cardId;      // 领取的卡片id
        public int cardType;    // 卡片类型

        public JsonCardReward(int cardId, int cardType) {
            this.cardId = cardId;
            this.cardType = cardType;
        }

        /*
        public int getCardId() {
            return cardId;
        }

        public void setCardId(int cardId) {
            this.cardId = cardId;
        }

        public int getCardType() {
            return cardType;
        }

        public void setCardType(int cardType) {
            this.cardType = cardType;
        }
        */
    }

    public JsonCardReward createJsonCardReward(int cardId, int cardType)
    {
        return new JsonCardReward(cardId, cardType);
    }
}
