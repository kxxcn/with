package dev.kxxcn.app_with.data.model.diary

import dev.kxxcn.app_with.data.model.nickname.ResponseNickname

class Detail(
        val diaryList: List<Diary>,
        val nickname: ResponseNickname,
        val profile: Profile? = null
)

class Profile(
        val myImage: String,
        val yourImage: String
)