package com.itxia.backend.controller.api;

/**
 * @author Yzh
 * 将一些描述性的文字配置放在这里，美化controller
 */
public class AdminControllerApiDescription {
    public static final String CLASS_API = "IT侠后台用户使用的api";
    public static final String METHOD_MOD_PWD = "后台用户修改自己密码";
    public static final String NOTES_MOD_PWD = "这个是修改自己的密码的";
    public static final String PARAM_MOD_PWD_OLD_PWD = "当前的密码";
    public static final String PARAM_MOD_PWD_NEW_PWD = "新密码";

    public static final String UPDATE_SELF_INFO = "修改自己的个人信息";
    public static final String PARAM_SELF_INFO_LOCATION = "个人信息-校区";
    public static final String PARAM_SELF_INFO_ACCEPT_EMAIL = "个人信息-是否接受邮箱";
    public static final String PARAM_SELF_INFO_EMAIL = "个人信息-邮箱";

    public static final String METHOD_APM_ALL = "后台用户查看所有的维修单";
    public static final String NOTES_APM_ALL = "这个接口会查询很久，谨慎使用";

    public static final String METHOD_APM_BY_PAGE = "后台用户所有维修单";
    public static final String PARAM_APM_BY_PAGE_PAGE = "页码";
    public static final String PARAM_APM_BY_PAGE_SIZE = "页的大小";

    public static final String METHOD_APM_BY_COND = "根据条件搜索维修单";
    public static final String PARAM_APM_BY_COND_LOC = "校区";
    public static final String PARAM_APM_BY_COND_STATE = "状态";
    public static final String PARAM_APM_BY_COND_SEARCH = "搜索字符串";
    public static final String PARAM_APM_BY_COND_PAGE = "页码";
    public static final String PARAM_APM_BY_COND_SIZE = "页的大小";

    public static final String METHOD_ACT_APM = "后台用户接单";
    public static final String NOTES_ACT_APM = "只能对状态为新创建的预约单进行此操作";
    public static final String PARAM_ACT_APM_ID = "预约单的ID";

    public static final String METHOD_REPLY = "后台用户对某一个预约单添加评论回复";
    public static final String NOTES_REPLY = "用来给一个预约单添加解决问题的方式的回复、评论\nIT侠后台成员和此预约单的" +
            "用户可以在订单的任意状态下添加评论\n调用此接口，请在header中设置id，表示IT侠用户的账号";
    public static final String PARAM_REPLY_OID = "预约单的id";
    public static final String PARAM_REPLY_CONTENT = "评论的具体内容";

    public static final String METHOD_CREATE_MBR = "管理员账号添加后台用户";
    public static final String NOTES_CREATE_MBR = "需要管理员权限，考虑加一个参数email";
    public static final String PARAM_CREATE_MBR_USER = "登陆名";
    public static final String PARAM_CREATE_MBR_PWD = "密码";
    public static final String PARAM_CREATE_MBR_LOC = "校区";
    public static final String PARAM_CREATE_MBR_NAME = "姓名";

    public static final String METHOD_MBR_ALL = "管理员账号查询所有后台用户";
    public static final String NOTES_MBR_ALL = "需要管理员权限";

    public static final String METHOD_MOD_MBR_PWD = "管理员账号修改后台用户密码";
    public static final String NOTES_MOD_MBR_PWD = "需要管理员权限";
    public static final String PARAM_MOD_MBR_PWD_USER = "IT侠账号的登录名";
    public static final String PARAM_MOD_MBR_PWD_PWD = "新的密码";

    public static final String METHOD_RCD_UPLOAD = "上传维修记录至OSS";
    public static final String NOTES_RCD_UPLOAD = "感觉这个功能没有什么用，不如直接显示出来";
    public static final String PARAM_RCD_UPLOAD_WEEK = "这一年的第几个周";

    public static final String METHOD_APM_NUM = "获取不同状态的维修单的数量";
    public static final String NOTES_APM_NUM = "根据校区/维修单状态来获取";
    public static final String PARAM_APM_NUM_LOC = "校区，可以是仙林、鼓楼、全部";
    public static final String PARAM_APM_NUM_STATE = "维修单状态，必须是CREATED，ACCEPTED,或FINISHED";
}
