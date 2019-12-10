package dev.kxxcn.app_with.util.preference

object PreferenceUtils : PreferenceExtension() {

    /**
     * 일기 알림
     */
    var notifyDiary: Boolean
        get() = "notifyDiary".getBoolean(true)
        set(value) = "notifyDiary".putBoolean(value)

    /**
     * 일기 알림 진동
     */
    var notifyDiaryVibrate: Boolean
        get() = "notifyDiaryVibrate".getBoolean(true)
        set(value) = "notifyDiaryVibrate".putBoolean(value)

    /**
     * 일기 알림 소리
     */
    var notifyDiarySound: Boolean
        get() = "notifyDiarySound".getBoolean(true)
        set(value) = "notifyDiarySound".putBoolean(value)

    /**
     * 일정 알림
     */
    var notifyPlan: Boolean
        get() = "notifyPlan".getBoolean(true)
        set(value) = "notifyPlan".putBoolean(value)

    /**
     * 일정 알림 진동
     */
    var notifyPlanVibrate: Boolean
        get() = "notifyPlanVibrate".getBoolean(true)
        set(value) = "notifyPlanVibrate".putBoolean(value)

    /**
     * 일정 알림 소리
     */
    var notifyPlanSound: Boolean
        get() = "notifyPlanSound".getBoolean(true)
        set(value) = "notifyPlanSound".putBoolean(value)

    /**
     * 디데이 알림
     */
    var notifyDay: Boolean
        get() = "notifyDay".getBoolean(true)
        set(value) = "notifyDay".putBoolean(value)

    /**
     * 디데이 알림 진동
     */
    var notifyDayVibrate: Boolean
        get() = "notifyDayVibrate".getBoolean(true)
        set(value) = "notifyDayVibrate".putBoolean(value)

    /**
     * 디데이 알림 소리
     */
    var notifyDaySound: Boolean
        get() = "notifyDaySound".getBoolean(true)
        set(value) = "notifyDaySound".putBoolean(value)

    /**
     * 공지사항 알림
     */
    var notifyNotice: Boolean
        get() = "notifyNotice".getBoolean(true)
        set(value) = "notifyNotice".putBoolean(value)

    /**
     * 공지사항 알림 진동
     */
    var notifyNoticeVibrate: Boolean
        get() = "notifyNoticeVibrate".getBoolean(true)
        set(value) = "notifyNoticeVibrate".putBoolean(value)

    /**
     * 공지사항 알림 소리
     */
    var notifyNoticeSound: Boolean
        get() = "notifyNoticeSound".getBoolean(true)
        set(value) = "notifyNoticeSound".putBoolean(value)

    /**
     * FCM Token
     */
    var newToken: String?
        get() = "newToken".getString(null)
        set(value) = "newToken".putString(value)

    /**
     * 앱 첫 실행시간
     */
    var firstTime: Long
        get() = "KEY_FIRST_TIME".getLong(0L)
        set(value) = "KEY_FIRST_TIME".putLong(value)
}
