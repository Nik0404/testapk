package com.acroninspector.app.data.util.support

class AcronNoInternetException(functionName: String, cause: Throwable)
    : AcronSyncException(functionName, cause)