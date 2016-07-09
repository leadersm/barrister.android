package com.lsm.barrister.app;


/**
 * Title: Constants.java
 * Description:常量
 *
 * @author lsm
 * @date 2015-5-18
 */
public class Constants {

    public static String PUSH_ID = "";

    //主题id
    public static final int THEME = 0;
    //调试开关
    public static boolean DEBUG = true;

    //设备唯一标识
    public static String deviceId;
    //mac地址
    public static String mac;
    //文件夹目录
    public static String baseDir = "barrister";
    public static String tempDir = baseDir + "/temp";
    public static String imageDir = baseDir + "/images";
    public static String configDir = baseDir + "/config";
    public static String downloadDir = baseDir + "/download";
    public static String logDir = baseDir + "/log";
    //屏幕尺寸
    public static String screenSize;
    public static int screenHeight;
    public static int screenWidth;
    //正文模板
    public static final String TEMPLATE_DEF_URL = "www/template.html";
    //读报详情模板
    public static final String TEMPLATE_BBC_DETAIL_URL = "www/template_bbc_detail.html";
    public static final String TEMPLATE_NORMAL_DETAIL_URL = "www/template_normal_detail.html";

    //bundle key
    public static final String KEY = "key";
    public static final String KEY_ACCOUNT = "account";
    public static final String KEY_TITLE = "key.title";
    public static final String KEY_FILE = "key.file";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_USER = "user";
    public static final String KEY_POST = "post";
    public static final String KEY_CHANNEL = "channel";
    public static final String KEY_PICK_IMGS = "pick_imgs";
    public static final String KEY_NEWS_ITEM = "news.item";
    public static final String KEY_URL = "url";
    public static final String KEY_COMMENT_ENABLE = "commentEnable";
    public static final String KEY_COMMENT_EXAMINE = "commentExamine";
    public static final String KEY_TOPIC_TYPE = "topic.type";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_INDEX = "index";
    public static final String KEY_COMMENT = "comment";
    public static final String KEY_USER_INFO_VALUE = "user.info.value";
    public static final String KEY_ID = "id";
    public static final String KEY_THUMB = "thumb";

    //专题类型
    public static final String TOPIC_TYPE_NEWS = "topic.type.news";
    public static final String TOPIC_TYPE_TOPIC = "topic.type.topic";
    //startActivity requestCode
    public static final int REQUESTCODE_SETTING = 8009;
    public static final int REQUESTCODE_FROM_LOGIN_DO_REGISTER = 8010;
    public static final int REQUESTCODE_DO_LOGIN = 8011;
    public static final int REQUESTCODE_FROM_FORUM_NEW_POST = 8012;
    public static final int REQUESTCODE_PICK_IMAGE = 8013;
    public static final int REQUEST_CODE_CAPTURE = 8014;
    public static final int REQUEST_CODE_FROM_MAIN = 8015;
    public static final int REQUEST_CODE_FROM_USER_DETAIL = 8016;
    public static final int REQUEST_CODE_GALLERY = 8017;
    public static final int REQUEST_CODE_CROP = 8018;
    public static final int REQUEST_CODE_NEWCOMMENT = 8019;
    public static final int REQUEST_CODE_BIND_CARD = 8020;
    public static final int REQUEST_CODE_ADD_SUMMARY = 8021;


    //用户协议文档
    public static final String DOC_AGREEMENT = "file:///android_asset/agreement.htm";
    //关于文档
    public static final String DOC_ABOUT = "file:///android_asset/about.htm";
    //提现须知
    public static final String DOC_GETMONEY = "file:///android_asset/agreement.htm";
    public static final String DOC_BANKS = "banks.xml";


    //notification ids
    public static final int NOTIFICATION_ID_NEWS = 0x1001;

    //收藏类型
    public static final String FAVORITE_TYPE_NEWS = "news";//新闻
    public static final String FAVORITE_TYPE_POST = "post";//帖子
    public static final String FAVORITE_TYPE_TOPIC = "topic";//帖子，接口定义帖子为topic。。。
    public static final String FAVORITE_TYPE_EPAPER = "newspaper";//读报

    //最大字数限制
    public static final int MAX_INTRODUCTION = 35;//简介字数限制
    public static final int MAX_NICKNAME = 9;//昵称字数限制
    public static final int MAX_COMMENT = 500;//500字符，250汉字
    public static final int MAX_POST_TITLE = 50;//50字符，25汉字
    public static final int MAX_POST_CONTENT = 4000;//4000字符，2000汉字

    public static int NOTIFICATION_REQUEST_CODE = 0x2001;

    public static final String MARKET_KEY = "UMENG_CHANNEL";
    public static final String DEBUG_KEY = "DEBUG";

    public static String MARKET = "default";//

    public static final int OFFLINE_NEWS_SIZE = 20;

    public static final String ALIAS_LAN = "ALIAS_LAN";//局域网
    public static final String ALIAS_WAN = "ALIAS_WAN";//广域网

    public static final long DURATION_ANIM_ACTIONBAR = 800;
    public static final long DURATION_ANIM_TABS_BG_COLOR = 300;

    public static final String TAG_LAN = "TAG_LAN";//测试环境
    public static final String TAG_WAN = "TAG_WAN";//正式环境


}
