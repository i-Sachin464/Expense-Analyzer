package com.client.expenseanalyzer.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.MutableLiveData
import com.client.expenseanalyzer.model.SMS

/**
 * This class used for background task to read inbox SMS and post that list
 */
@Suppress("DEPRECATION")
class ReadSMSTask(
    private val cursor: Cursor?,
    private val data: MutableLiveData<List<SMS>>,
) :
    AsyncTask<String, Int, Int>() {

    override fun doInBackground(vararg params: String?): Int {
        val smsList = ArrayList<SMS>()
        if (cursor!!.moveToFirst()) {
            for (i in 0 until cursor.count) {
                val address: String =
                    cursor.getString(cursor.getColumnIndexOrThrow("address")).toString()
                val message: String =
                    cursor.getString(cursor.getColumnIndexOrThrow("body")).toString()
                val readstate: String =
                    cursor.getString(cursor.getColumnIndexOrThrow("read")).toString()
                val time: Long =
                    cursor.getString(cursor.getColumnIndexOrThrow("date")).toString().toLong()
                val type: String =
                    cursor.getString(cursor.getColumnIndexOrThrow("type")).toString()
                smsList.add(SMS(address, message, readstate, time, type))
                cursor.moveToNext()
            }
        }
        data.postValue(smsList)
        cursor.close()
        return 0;
    }

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun onPostExecute(result: Int?) {
        super.onPostExecute(result)
    }
}