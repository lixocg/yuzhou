package com.yuzhou.qiyukf.controller;

import com.yuzhou.qiyukf.session.SessionClient;
import org.springframework.stereotype.Service;

/**
 * Created by zhoujianghua on 2016/10/20.
 */
@Service("qiyuSessionService")
public class QiyuSessionService extends SessionClient {

    public QiyuSessionService() {
        super(Constants.QIYU_APP_KEY, Constants.QIYU_APP_SECRET);
    }
}
