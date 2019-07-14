package com.know.wenda.dao;

import com.know.wenda.dao.mapper.TokenMapper;
import com.know.wenda.domain.TokenDO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * TokenDAO
 *
 * @author shunhua
 */
@Repository
public class TokenDAO {

    @Resource
    private TokenMapper tokenMapper;

    /**
     * 新增
     *
     * @param tokenDO
     * @return
     */
    public int insert(TokenDO tokenDO) {
        return tokenMapper.insert(tokenDO);
    }

    /**
     * 根据token获取
     *
     * @param token
     * @return
     */
    public TokenDO findByToken(String token) {
        return tokenMapper.findByToken(token);
    }

    /**
     * @param token
     * @return
     */
    public int delete(String token) {
        return tokenMapper.delete(token);
    }

    /**
     * 查询token列表
     * @return
     */
    public List<TokenDO> findTokenList(){
        return tokenMapper.findTokenList();
    }

    /**
     * 清除无用token
     * @param tokenIds
     * @return
     */
    public int cleanToken(List<Integer> tokenIds){
        return tokenMapper.cleanTokens(tokenIds);
    }

}
