package com.example.stefanr.archapp.helpers

import android.content.Context
import android.util.Log
import java.io.*


//this class used an outputStream to write Data into an given File

fun writeToFile(context: Context, fileName: String, data: String) {
    try {
        val outputStreamWriter = OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE))
        outputStreamWriter.write(data)
        outputStreamWriter.close()
    } catch (e: Exception) {
        Log.e("Error: ", "Cannot find file: " + e.toString());
    }
}

// this function reads data from a existing file
//what file is read is specified by the file String
fun read(context: Context, file: String): String {
    var str = ""
    try {
        val inputStream = context.openFileInput(file)
        if (inputStream != null) {
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val pStr = StringBuilder()
            var flag = false
            while (!flag) {
                var line = bufferedReader.readLine()
                flag = (line == null);
                if (line != null) pStr.append(line);
            }
            inputStream.close()
            str = pStr.toString()
        }
    } catch (e: FileNotFoundException) {
        Log.e("Error: ", "file not found: " + e.toString());
    } catch (e: IOException) {
        Log.e("Error: ", "cannot read file: " + e.toString());
    }
    return str
}

// helper function to check if a file is existing
fun exists(context: Context, filename: String): Boolean
{
    val file = context.getFileStreamPath(filename)

    return file.exists()
}