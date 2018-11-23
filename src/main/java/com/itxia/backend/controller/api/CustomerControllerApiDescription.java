package com.itxia.backend.controller.api;

/**
 * @author Yzh
 */
public class CustomerControllerApiDescription {
    public static final String METHOD_MAKE_APM = "用户添加一个预约";
    public static final String NOTES_MAKE_APM = "用户同时应该只能有一个状态为未完成的预约单";
    public static final String PARAM_MAKE_APM = "预约单的内容";

    public static final String METHOD_CANCEL_APM = "用户取消一个预约";
    public static final String NOTES_CANCEL_APM = "取消的预约单还在所有订单当中，只是状态标记为取消";
    public static final String PARAM_CANCEL_APM_ID = "预约单的id";

    public static final String METHOD_CANCEL_CUR_APM = "用户取消当前预约";
    public static final String NOTES_CANCEL_CUR_APM = "将当前预约状态标记为取消";

    public static final String METHOD_CUR_APM = "用户获取当前未完成的预约单";
    public static final String NOTES_CUR_APM = "在header中传入用户的id";

    public static final String METHOD_MOD_APM = "用户修改当前未完成的预约单";
    public static final String NOTES_MOD_APM = "在header中传入用户的id";
    public static final String PARAM_MOD_APM_CTN = "预约单的内容";

    public static final String METHOD_ALL_APM = "用户获取自己的所有预约单";
    public static final String NOTES_ALL_APM = "在header中传入用户的id";

    public static final String METHOD_REPLY = "用户给预约单添加一些评论回复";
    public static final String NOTES_REPLY = "在header中传入用户的id";
    public static final String PARAM_REPLY_ID = "预约单的id";
    public static final String PARAM_REPLY_CTN = "具体回复内容";

}
