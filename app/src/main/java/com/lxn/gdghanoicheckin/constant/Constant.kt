package com.lxn.gdghanoicheckin.constant

object Constant {
    //body: https://script.google.com/macros/s/AKfycbxNDUIvBJ_wvOr-QGee6ZBMEhYK8S8iLDoiXkdyrhJBUVqzkEtaAb-nfFDAn8c0Iytcuw/
    //param: https://script.google.com/macros/s/AKfycbypTAsjZIFVVacUyVOxHrSMUtWw3kM3c0vZw4g2ERTigGBKrkYGRuUpSZ8pVyV1jGd4Gw/
    const val BASE_URL =
        "https://script.google.com/macros/s/AKfycbypTAsjZIFVVacUyVOxHrSMUtWw3kM3c0vZw4g2ERTigGBKrkYGRuUpSZ8pVyV1jGd4Gw/"
    const val LOADER_EXPIRED_TIME = 60
    const val SIZE_QR = 1792
    const val CHILD_NODE_FIREBASE = "GDGHaNoi"
}

enum class TypeCheckIn {
    NotEmail,
    NoAccount,
    NetworkError,
    Existed,
    Success,
    ApiError
}

//enum class TypeCheckIn(val value: String) {
//    Normal(value = "normal"),
//    Vip(value = "vip"),
//    IsExited(value = "IsExited")
//}