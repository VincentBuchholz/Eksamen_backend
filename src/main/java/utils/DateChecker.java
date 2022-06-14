package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateChecker {

    public static boolean CHECK_DATES(String sDate, String eDate) throws ParseException {
        Date startDate;
        Date endDate;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Date d = new Date();
        String currDt = sdf.format(d);

        startDate = sdf.parse(sDate);
        endDate = sdf.parse(eDate);

        if ((d.after(startDate) && (d.before(endDate)))
                || (currDt.equals(sdf.format(startDate)) || currDt.equals(sdf
                .format(endDate)))) {
            return true;
        } else {
            return false;
        }
    }


    }

