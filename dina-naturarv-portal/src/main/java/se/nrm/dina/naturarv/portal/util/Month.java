/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.naturarv.portal.util;

/**
 *
 * @author idali
 */
public enum Month {
    JAN("Jan", "jan", 1),
    FEB("Feb", "feb", 2),
    MAR("Mar", "mar", 3),
    APR("Apr", "apr", 4),
    MAY("May", "maj", 5),
    JUN("Jun", "jun", 6),
    JUL("Jul", "jul", 7),
    AUG("Aug", "aug", 8),
    SEP("Sep", "sep", 9),
    OCT("Oct", "okt", 10),
    NOV("Nov", "nov", 11),
    DEC("Dec", "dec", 12);

    private final String en;
    private final String sv;
    private final int numberOfMonth;

    Month(String en, String sv, int numberOfMonth) {
        this.en = en;
        this.sv = sv;
        this.numberOfMonth = numberOfMonth;
    }

    public int getNumberOfMonth() {
        return numberOfMonth;
    }

    public String getEnglish() {
        return en;
    }

    public String getSwedish() {
        return sv;
    }

    public static Month getMonth(int i) {
        switch (i) {
            case 1:
                return JAN;
            case 2:
                return FEB;
            case 3:
                return MAR;
            case 4:
                return APR;
            case 5:
                return MAY;
            case 6:
                return JUN;
            case 7:
                return JUL;
            case 8:
                return AUG;
            case 9:
                return SEP;
            case 10:
                return OCT;
            case 11:
                return NOV;
            case 12:
                return DEC;
            default:
                return JAN;
        }
    }

    public static Month getMonth(String mon, boolean isSwedish) {
        if (isSwedish) {
            switch (mon) {
                case "Jan":
                    return JAN;
                case "Feb":
                    return FEB;
                case "Mar":
                    return MAR;
                case "Apr":
                    return APR;
                case "May":
                    return MAY;
                case "Jun":
                    return JUN;
                case "Jul":
                    return JUL;
                case "Aug":
                    return AUG;
                case "Sep":
                    return SEP;
                case "Oct":
                    return OCT;
                case "Nov":
                    return NOV;
                case "Dec":
                    return DEC;
                default:
                    return JAN;
            }
        } else {
            switch (mon) {
                case "jan":
                    return JAN;
                case "feb":
                    return FEB;
                case "mar":
                    return MAR;
                case "apr":
                    return APR;
                case "maj":
                    return MAY;
                case "jun":
                    return JUN;
                case "jul":
                    return JUL;
                case "aug":
                    return AUG;
                case "sep":
                    return SEP;
                case "okt":
                    return OCT;
                case "nov":
                    return NOV;
                case "dec":
                    return DEC;
                default:
                    return JAN;
            }
        }
    }
}
