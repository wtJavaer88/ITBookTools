package com.wnc.itbooktool.tools;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import com.wnc.itbooktool.utils.MyAppParams;
import com.wnc.tools.FileOp;

import db.DbExecMgr;

public class UpdateWordWeight
{
    public static void main( String[] args )
    {

        DbExecMgr.refreshCon( MyAppParams.sqliteName );

        List<String> readFrom = FileOp.readFrom( MyAppParams.workSpace
                + "aggrev.txt" );
        String[] split;
        for ( String string : readFrom )
        {
            try
            {
                split = string.split( " " );
                System.out.println( split[0] + " " + split[1] );
                DbExecMgr.execOnlyOneUpdate( "UPDATE DICTIONARY SET WEIGHT = "
                        + split[1] + " WHERE TOPIC_WORD='"
                        + StringEscapeUtils.escapeSql( split[0] ) + "'" );
            } catch ( SQLException e )
            {
                e.printStackTrace();
            }
        }
    }
}

