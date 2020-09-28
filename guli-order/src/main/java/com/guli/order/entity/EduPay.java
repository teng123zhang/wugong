package com.guli.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author testjava
 * @since 2020-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="EduPay对象", description="")
public class EduPay implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    private String orderNo;

    private Date payTime;

    private BigDecimal totalFee;

    private String transactionId;

    private String tradeState;

    private Integer payType;

    private String attr;

    private Boolean isDeleted;

    private Date gmtCreate;

    private Date gmtModified;


}
