package com.acroninspector.app.common.constants

object Constants {

    const val DEFAULT_INVALID_ID = -999
    const val DEFAULT_INVALID_ID_LIST = ""
    const val DEFAULT_INVALID_TYPE = -999
    const val DEFAULT_INVALID_STATUS = -999
    const val DEFAULT_INVALID_SCREEN = -999

    const val LOAD_DATA_FROM_SERVER = 1
    const val UPLOAD_DATA_TO_SERVER = 2
    const val CLEAR_ALL_DATA = 3

    const val UNANSWERED_LISTS = 4
    const val UNSCANNED_NFC_MARKS = 5

    const val REQUEST_CODE_NFC_SETTINGS = 1

    const val KEY_TASK_ID = "task_id"
    const val KEY_EQUIPMENT_ID = "equipment_id"
    const val KEY_EQUIPMENT_CLASS_ID = "equipment_class_id"
    const val KEY_NFC_EQUIPMENT_ID = "nfc_equipment_id"
    const val KEY_CHECK_LIST_ID = "check_list_id"
    const val KEY_DEFECT_CAUSE_ID = "defect_cause_id"
    const val KEY_DEFECT_NAME_ID = "defect_id"
    const val KEY_DEFECT_LOG_ID = "defect_log_id"
    const val KEY_ENTITY_ID = "entity_id"
    const val KEY_ENTITY_ID_LIST = "entity_id_list"
    const val KEY_ROUTE_ID = "route_id"
    const val KEY_COULD_EDIT = "could_edit"

    const val KEY_DEFECT_OBJECT = "defect_object"
    const val KEY_TASK_OBJECT = "task_object"
    const val KEY_ROUTE_OBJECT = "route_object"
    const val KEY_CHECK_LIST_OBJECT = "checklist_object"

    const val KEY_NFC_CODE = "nfc_code"
    const val NFC_KEY = "nfc_key"

    const val KEY_TASK_STATUS = "task_status"
    const val KEY_ROUTE_NAME = "route_name"
    const val KEY_ENTITY_TYPE = "entity_type"
    const val KEY_COMMENT = "comment"
    const val KEY_ENABLED_EDITING = "enable_editing"
    const val KEY_CREATING_DEFECT = "creating_defect"

    const val KEY_SEARCH_QUERY = "search_result"
    const val KEY_SEARCH_ENTITY = "search_entity"
    const val KEY_IS_FROM_SEARCH = "is_from_search"

    const val KEY_IS_AFTER_LOGOUT = "after_logout"

    const val KEY_FILE_URI = "file_uri"
    const val KEY_FILE_MEDIA_TYPE = "file_media_type"
    const val KEY_FILE_PATH = "file_path"

    const val KEY_EQUIPMENT_SCREEN = "equipment_screen"
    const val ROOT_EQUIPMENT_SCREEN = 0
    const val NESTED_EQUIPMENT_SCREEN = 1

    const val ENTITY_CHECK_LIST = 0
    const val ENTITY_ROUTE = 1
    const val ENTITY_TASK = 2
    const val ENTITY_DEFECT_LOG = 3
    const val ENTITY_EQUIPMENT = 4
    const val ENTITY_EQUIPMENT_LIST = 5


    const val MEDIA_TYPE_PHOTO = 0
    const val MEDIA_TYPE_VIDEO = 1
    const val MEDIA_TYPE_AUDIO = 2

    const val PHOTO_PREFIX = "IMG_"
    const val VIDEO_PREFIX = "VID_"
    const val AUDIO_PREFIX = "REC_"

    const val PHOTO_EXTENSION_JPG = ".jpg"
    const val VIDEO_EXTENSION_MP4 = ".mp4"
    const val AUDIO_EXTENSION_MP3 = ".mp3"

    const val EQUIPMENT_FOLDER = 0
    const val EQUIPMENT_ITEM = 1
    const val EQUIPMENT_DIVIDER = 2
    const val EQUIPMENT_SPACE = 3

    const val NO_ANSWER = ""
    const val ANSWER_NO = "0"
    const val ANSWER_YES = "1"

    const val ANSWER_MAYBE = 3
    const val ANSWER_UNACCEPTABLE = 4

    const val CRITICALITY_NO = 0
    const val CRITICALITY_NORMAL = 3
    const val CRITICALITY_TO_STOP = 2
    const val CRITICALITY_EMERGENCY = 1

    const val MENU_ITEM_CANCEL_ANSWER = 0
    const val MENU_ITEM_MEDIAFILE = 1
    const val MENU_ITEM_COMMENT = 2
    const val MENU_ITEM_DEFECT = 3

    const val DEFAULT_MIN_VALUE = -99999.0
    const val DEFAULT_MAX_VALUE = 99999.0

    const val ACRON_TRUE = "1"
    const val ACRON_FALSE = "0"
    const val ACRON_EMPTY = ""

    const val MAX_COMMENT_LENGTH = 500
    const val MAX_NFC_NAME_LENGTH = 150

    const val THEME_MODE = "theme_mode"
    const val NAV = "nav"
    const val NET = "net"
}
