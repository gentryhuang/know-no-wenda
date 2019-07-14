package com.know.wenda.configuration.component;

import com.know.wenda.dao.TokenDAO;
import com.know.wenda.domain.TokenDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ScheduledService
 *
 * @author hlb
 */
@Component
public class ScheduledService {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledService.class);

    @Resource
    private TokenDAO tokenDAO;

    /**
     * 定期  清除无用token
     * 每个月的的周日 凌晨1点执行一次
     * test:  @Scheduled(cron = "0 50 22 ? * 3")
     */
    @Scheduled(cron = "0 0 1 ? * 7")
    public void cleanToken(){
        // 取出token
        List<TokenDO> tokenDOS =  tokenDAO.findTokenList();
        // 筛选出过期的
        List<Integer> tokenIds = new ArrayList<>();
        tokenDOS.stream().forEach(tokenDO -> {
            if(tokenDO.getExpired().before(new Date())){
                tokenIds.add(tokenDO.getId());
            }
        });
        // 清除无用token
        int count = tokenDAO.cleanToken(tokenIds);
        if(count == tokenIds.size()){
            logger.info("清理了{}条Token",count);
        }else {
            logger.error("清理token失败");
        }
    }
}