package com.example.akb.edaalarm;

/**
 * Created by akb on 2018/1/30.
 */

public class Tools {
    public static String getelementstring(String allstring,String elementtype){
        return getmidstring(allstring,"<"+elementtype+">","</>");
    }
    public static String getmidstring(String allstr,String left,String right){
        int l=allstr.indexOf(left);
        int r=allstr.indexOf(right,l+left.length());
        if(l>=0 && r>=0 && (l+left.length())<r){
            return allstr.substring(l+left.length(),r);
        }
        return "";
    }


}

