package com.acroninspector.app.data.util.support

class AcronDataProcessingException(functionName: String, cause: Throwable)
    : AcronSyncException(functionName, cause)