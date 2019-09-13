package dev.kxxcn.app_with.data.remote;

/**
 * Created by kxxcn on 2018-08-20.
 */
public class APIPersistence {
    //:: Development
    // public static final String SERVER_URL = "http://221.152.105.76:80/with/";

    //:: Production
    // public static final String SERVER_URL = "http://kxxcn.cafe24.com/with/";
    public static final String SERVER_URL = "http://172.30.1.6:8080/with/";

    // public static final String NAVER_SERVER_URL = "https://openapi.naver.com/v1/";
    public static final String NAVER_SERVER_URL = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/";
    public static final String IMAGES_URL = SERVER_URL + "uploads/images/";
    public static final String THUMBS_URL = SERVER_URL + "uploads/thumbs/";
    public static final String EVENTS_URL = SERVER_URL + "uploads/events/";
    public static final String GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static final String GET_KEY = "getPairingKey.php";
    public static final String AUTHENTICATE_KEY = "authenticateKey.php";
    public static final String SIGN_UP = "signUp.php";
    public static final String SIGN_OUT = "signOut.php";
    public static final String IS_REGISTERED_USER = "isRegisteredUser.php";
    public static final String IS_LOCKED_USER = "isLockedUser.php";
    public static final String GET_DIARY = "getDiary.php";
    public static final String GET_PLAN = "getPlan.php";
    public static final String GET_IMAGE = "getImage.php";
    public static final String REGISTER_DIARY = "registerDiary.php";
    public static final String UPDATE_DIARY = "updateDiary.php";
    public static final String REGISTER_PLAN = "registerPlan.php";
    public static final String UPDATE_PLAN = "updatePlan.php";
    public static final String REGISTER_IMAGE = "uploadImage.php";
    public static final String REMOVE_DIARY = "removeDiary.php";
    public static final String REMOVE_PLAN = "removePlan.php";
    public static final String GET_NOTIFICATION_INFORMATION = "getNotificationInformation.php";
    public static final String UPDATE_RECEIVE_NOTIFICATION = "updateReceiveNotification.php";
    // public static final String CONVERT_COORD_TO_ADDRESS = "map/reversegeocode";
    public static final String CONVERT_COORD_TO_ADDRESS = "v2/gc";
    public static final String UPDATE_TOKEN = "updateToken.php";
    public static final String GET_TITLE = "getNickname.php";
    public static final String REGISTER_NICKNAME = "registerNickname.php";
    public static final String CHECK_MODE = "checkMode.php";
    public static final String CHECK_NEW_NOTICE = "checkNewNotice.php";
    public static final String GET_NOTICE = "getNotice.php";
    public static final String REGISTER_LOCK = "registerLock.php";
    public static final String UNREGISTER_LOCK = "unregisterLock.php";
    public static final String SYNC = "sync.php";
    public static final String SUBSCRIBE_IDS = "getSubscribeIds.php";
    public static final String FETCH_EVENTS = "fetchEvents.php";
    public static final String REGISTER_ENTRY = "registerEntry.php";

    public static final String FCM_MESSAGE = "message";
    public static final String FCM_TYPE = "type";
    public static final String FCM_NOTIFY = "notify";

    public static final String TYPE_AUTH = "authentication";
    public static final String TYPE_DIARY = "diary";
    public static final String TYPE_PLAN = "plan";
    public static final String TYPE_NOTICE = "notice";

    public static final int ID_NOTIFY = 0;
    public static final int DENIED_NOTIFICATION = 0;
}
