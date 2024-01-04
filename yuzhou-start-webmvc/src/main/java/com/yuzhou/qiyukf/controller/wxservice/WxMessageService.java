package com.yuzhou.qiyukf.controller.wxservice;

import com.alibaba.fastjson.JSONObject;
import com.yuzhou.qiyukf.controller.Constants;
import com.yuzhou.qiyukf.controller.wxutil.EmojiConverter;
import com.yuzhou.qiyukf.session.util.HttpClientPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by zhoujianghua on 2015/10/24.
 */
@Service("wxMsgService")
@Slf4j
public class WxMessageService {

    private static final String TAG_TO_USER = "touser";
    private static final String TAG_MSG_TYPE = "msgtype";
    private static final int defaultRetryTimes = 2;
    private static final String sendRetryQueue = "mq_send_retry_queue";

    private static final int MAX_BYTES_LIMIT = 2000;

    @Autowired
    private WXAuthService wxAuthService;

    @Autowired
    private EmojiConverter emojiConverter;

    public void replyText(String openId, String text) throws IOException {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        sendText(openId, text);
    }

    private void sendText( String openId, String text) {
        JSONObject body = new JSONObject();
        body.put("content", emojiConverter.convertNim(text));

        JSONObject json = new JSONObject();
        json.put(TAG_TO_USER, openId);
        json.put(TAG_MSG_TYPE, "text");
        json.put("text", body);

        String sendStr = json.toJSONString();

        replyMessage(sendStr, "replyText");
    }

    private String msgUrl() {
        return Constants.WX_MSG_URL + "?access_token=" + wxAuthService.queryAccessToken();
    }

    private void replyMessage(String sendStr, String func) {

        String msgUrl = msgUrl();
        try {
            String ret = null;
            for (int i = 0; i <= defaultRetryTimes; i++) {
                try {
                    ret = HttpClientPool.getInstance().post(msgUrl, sendStr);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    log.warn("post request exception: " + ex);
                }
                if (ret != null) break;
            }

            if (ret == null) {
                log.warn(String.format("[wx] failed and retry !! sendStr = %s", sendStr));
            }
            log.debug(String.format("[%s] url=%s, send=%s, ret=%s", func, msgUrl, sendStr, ret));
        } catch (Exception ex) {
            ex.printStackTrace();
            log.warn("replyMessage error: " + ex.toString());
        }
    }

}
