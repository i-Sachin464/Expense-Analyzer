package com.client.expenseanalyzer.transaction

import android.database.Cursor
import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import com.client.expenseanalyzer.model.SMS
import com.client.expenseanalyzer.util.ReadSMSTask

/**
 * This Class handle read transaction SMS
 */
class TransactionManager {
    fun readSMS(cursor: Cursor,loadedMsgs:MutableLiveData<List<SMS>>) {
        var task: ReadSMSTask = ReadSMSTask(cursor, loadedMsgs)
        if (task.status == AsyncTask.Status.PENDING) {
            task.execute()
        }
    }
}