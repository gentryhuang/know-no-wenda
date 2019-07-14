
package com.know.wenda.dao.mapper;

import com.know.wenda.domain.TokenDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TokenMapper
 *
 * @author shunhua
 */
@Mapper
public interface TokenMapper {

    /**
     * 新增
     *
     * @param tokenDO
     * @return
     */
    int insert(TokenDO tokenDO);

    /**
     * 根据token获取
     *
     * @param token
     * @return
     */
    TokenDO findByToken(String token);

    /**
     * @param token
     * @return
     */
    int delete(String token);

    /**
     * 查询token列表
     * @return
     */
    List<TokenDO> findTokenList();

    /**
     * 清除无用的token
     * @param tokenIds
     * @return
     */
    int cleanTokens(@Param("tokenIds") List<Integer> tokenIds);
}
