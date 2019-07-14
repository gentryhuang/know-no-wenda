package com.know.wenda.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * GratitudeDTO
 *
 * @author hlb
 */
@Data
public class GratitudeDTO implements Serializable {

    private static final long serialVersionUID = 7808409304436613984L;

    /**
     * 话题id
     */
    private Integer questionId;
    /**
     * 话题对应的博主id
     */
    private Integer userId;
}