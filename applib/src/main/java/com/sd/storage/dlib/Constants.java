package com.sd.storage.dlib;

/**
 */
public class Constants {

    public static final String PARAM_KEY = "param_key";
    public static final String DATA = "data";

    public static final String USER_KEY = "いってきます";

    public static final boolean STRICT_MODE = true;
    public static final boolean DEBUG = true;

    public static final int TYPE_00 = 0;
    public static final int TYPE_01 = 1;
    public static final int TYPE_02 = 2;
    public static final int TYPE_03 = 3;
    public static final int TYPE_04 = 4;
    public static final int TYPE_05 = 5;
    public static final int TYPE_06 = 6;
    public static final int TYPE_07 = 7;
    public static final int TYPE_08 = 8;
    public static final int TYPE_09 = 9;
    public static final int TYPE_10 = 10;
    public static final int TYPE_11 = 11;
    public static final int TYPE_12 = 12;

    public static final String s = "0";
    public static final String TYPE_STR_00 = "0";
    public static final String TYPE_STR_01 = "1";
    public static final String TYPE_STR_02 = "2";
    public static final String TYPE_STR_03 = "3";
    public static final String TYPE_STR_04 = "4";
    public static final String TYPE_STR_05 = "5";
    public static final String TYPE_STR_06 = "6";
    public static final String TYPE_STR_07 = "7";
    public static final String TYPE_STR_08 = "8";
    public static final String TYPE_STR_09 = "9";
    public static final String TYPE_STR_11 = "11";
    public static final String TYPE_STR_20 = "20";
    public static final String TYPE_STR_21 = "21";
    public static final String TYPE_STR_31 = "31";
    public static final String TYPE_STR_33 = "33";
    public static final String TYPE_STR_40 = "40";
    public static final String TYPE_STR_41 = "41";

    public static final String MODEL = "model";

    public static final String ID = "id";
    public static final String ITEM_ID = "item_id";
    public static final String INDEX = "index";
    public static final String PAGE_INDEX = "page";
    public static final String URL = "url";
    public static final String POSITION = "position";
    public static final String GOODS  = "goods";
    public static final String GOODS_ID  = "goods_id";
    public static final String GOODS_NAME  = "goods_name";
    public static final String STORE  = "store";
    public static final String KEYWORD  = "keyword";
    public static final String TOKEN_ID = "tokenId";
    public static final String REC_ID = "rec_id";
    public static final String SPEC_ID = "spec_id";
    public static final String QUANTITY = "quantity";
    public static final String STORE_ID = "store_id";

    public static final String RECID = "recid";
    public static final String CONSIGNEE_ID = "consignee_id";

    public static final String TYPE = "type";
    public static final String SOURCE = "source";
    public static final String USE = "use";
    public static final String STATUS = "status";
    public static final String IS_SELECT_ADDRESS = "is_select_address";
    public static final String IS_SELECT = "is_select";

    public static final String XJID = "xjid";
    public static final String HGID = "hgid";
    public static final String LAT_KEY = "lat";
    public static final String LON_KEY = "lon";
    public static final String TAG = "tag";
    public static final String ADDRESS = "address";

    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String TOTAL = "total";

    public static final String IF_LM = "if_lm";
    public static final String ZJRMB = "zjrmb";

    public static final String ORDER_ID = "order_parent_sn";
    public static final String PASSWORD = "pwd";
    public static final String TITLE = "title";

    public static final String IS_MAIN = "main";

    public static final String IS_STORE= "is_store";

    public static final String CATEGORY_ID = "categoryId";

    public static final String PARENT_ID = "parent_id";
    // 一级分类
    public static final String CATEGORY_ONE = "category_one";
    // 二级分类
    public static final String CATEGORY_TWO = "category_two";
    // 省
    public static final String PROVINCE = "province";
    // 市
    public static final String CITY = "city";
    // 县
    public static final String AREA = "area";
    // 店铺名
    public static final String STORE_NAME = "store_name";

    // 分类id
    public static final String CATE_ID = "cate_id";
    // level id
    public static final String LEVEL = "level";
    // 城市id
    public static final String REGION_ID = "region_id";
    // order
    public static final String ORDER = "order";
    // REBATE
    public static final String REBATE = "rebate";

    // 默认城市id
    public static final String DEFAULT_CITY = "广州";
    // 默认经度
    public static final String DEFAULT_LON = "113.2759";
    // 默认纬度
    public static final String DEFAULT_LAT = "23.1170";
    // 产品id
    public static final String PRODUCT_ID = "product_id";
    // 图片路径
    public static final String IMAGE_URL = "image_url";

    // 金钱
    public static final String MONEY = "money";
    public static final String AMOUNT = "amount";

    // 桌号
    public static final String TABLE = "table";
    //商品的搜索分类id
    public static final String CATE_ID_1 = "cate_id_1";
    public static final String BRAND_ID = "brand_id";
    // 3 最新上架
   // public static final String ORDER = "order";
    //gtype 1、送现金 2、兑换
    public static final String GTYPE = "gtype";
    public static final String SERVERPHONE = "400-8821-789";


    // 店员id
    public static String DY_ID = "dy_id";

    public static final int DISCOUNT = TYPE_08;
    public static final int GOLD = TYPE_09;
    public static final int BUY = TYPE_10;
    public static final int GET_MONEY = TYPE_11;

    // '0=已提交，1=已发货，3=已配货，4=选货区待提交 5=确认收货 6=代理商已回收'
    public static final String IF_FAHUO = "if_fahuo";
    // 用户类型: 1、卖家 2、买家
    public static final String U_TYPE = "utype";
    // 商家类型
    public static final String U_T = "u_t";

    public static final String DL_QUANXIAN = "dl_quanxian";

    /*****************************支付宝START**************************/
    // 商户PID
    public static String getPARTNER() {
        return "2088311541929434";
    }
    // 商户收款账号
    public static String getSELLER() {
        return "754297281@qq.com";
    }
    // 商户私钥，pkcs8格式
    public static String getRSA_PRIVATE() {

        return  "MIICXgIBAAKBgQDC7COuZwAlHnaevtzUuV7ouIko/SEP08MJOeIpaH9aNElCg2FLT1r6Jb9KUdEuEKr8+vewwM9vqAQbfGjBWIvOc5TCdSs4Cq1qMy+DwJs0Bt65GZ9XiH9YeNtbMgR8FUfnn3fm5CzqmJa4dAXV8rwvXSbdbob/fgomC6VTs5pnLwIDAQABAoGAUGfMSDLg0m8Ai3c77NwSlHkayzevnbHCsFmaO26JqhILbk53ofM4GhvcDrI1UYU6o2R7LAQ9DVgHabcmFyuJb4WxYfDz3n8/KuBSuEgN66//BInzgpRPcAcxgSNMAY33dylKGo3HXTnkBaXt3/OzM8ReuP/jbEtsTOU+mXqHAIECQQDpZaLwjRa2ZEpF8ExYSq6rXM0nm1OTJwW+Tv3gpTCizP04PewPs/vrO8ou7qhqB1qc4Gs6XzfTDoxJbwinF+uPAkEA1cykqCY2gvRthm4pKG5VYq3CuYUUm4kba6cCO2k185jsZ1QA7fI/LOrQyDq/JFZKw8Y3IHlBGPTkpfDWKjh6YQJBAMp/yVBGSWd0OlDV/Zc5LLU3LjRn+qoY2E3WwUTQbhNCIY1ThGXXdgIRfQkdPffIa6DiwYYCqS+wVp07/djj0xcCQQC5Xzk4zgEfpDy+fQ9Uje2cpJocbsXCYauTuHdX+c4civAWWyB/LzYVhPdHVKBcKjGi817i2h9WOydwtZOLgfKhAkEA5BNFbrnmUckL61tpxzKn4GcrzSLEJad0eEcV5hLbirddmiPOug9Jz7Ldx42VYU46+0xlm2rDAblkctNsoK/ZQg==";
    }
    /*****************************支付宝END**************************/

    /*****************************微信START**************************/
    // APP ID
//    public static String getAPP_ID() {
//        return "wxda281cb263a474eb";
//    }
    // APP ID
    public static String getAPP_ID() {
        return "wxa2beced8218b16d3";
    }
    //商户号
    public static String getMCH_ID() {
        return "1284879201";
    }
    // PI密钥，在商户平台设置
    public static String getAPI_KEY() {
        return "CQZRD13509328466CDZRN18628973525";
    }

    public static String getAPP_SECRET() {
        return "d4624c36b6795d1d99dcf0547af5443d";
    }

    /*****************************微信END**************************/

    /*****************************应用宝START**************************/
    public static String getQQAPPID(){
        return "1104672605";
    }
    public static String getQQAPPKEY(){
        return "VyVIrcNgTwwwYZNS";
    }
    /*****************************应用宝END**************************/



}
