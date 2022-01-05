package com.example.tar.utils;

import android.text.format.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeHelper {

    Calendar today  = Calendar.getInstance();
    Calendar yesterday  = Calendar.getInstance();
    Calendar beforeYesterday  = Calendar.getInstance();

    public String getTimestampToTime(Long timeStamp){
        if(timeStamp!= null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            return dateFormat.format(new Date(timeStamp));
        }
        return "";
    }

    public String getTimeTodayYestFromMilli(long msgTimeMillis) {

        Calendar messageTime = Calendar.getInstance();
        messageTime.setTimeInMillis(msgTimeMillis);
        // get Currunt time
        Calendar now = Calendar.getInstance();

        final String strTimeFormate = "h:mm aa";
        final String strDateFormate = "dd/MM/yy";

        if (now.get(Calendar.DATE) == messageTime.get(Calendar.DATE) && ((now.get(Calendar.MONTH) == messageTime.get(Calendar.MONTH))) && ((now.get(Calendar.YEAR) == messageTime.get(Calendar.YEAR)))
        ) {
            return "" + DateFormat.format(strTimeFormate, messageTime);
            //return "Today";
        } else if (
                ((now.get(Calendar.DATE) - messageTime.get(Calendar.DATE)) == 1) && ((now.get(Calendar.MONTH) == messageTime.get(Calendar.MONTH))) &&
                        ((now.get(Calendar.YEAR) == messageTime.get(Calendar.YEAR)))
        ) {
//            return "yesterday at " + DateFormat.format(strTimeFormate, messageTime);
            return "Yesterday";
        } else {
            String mDay = DateFormat.format(strDateFormate, messageTime) + "";
//            return "date : " + DateFormat.format(strDateFormate, messageTime);
            return DateFormat.format(strDateFormate, messageTime) + "";
        }
    }
}
