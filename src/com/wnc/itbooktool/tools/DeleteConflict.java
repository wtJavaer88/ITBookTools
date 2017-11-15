package com.wnc.itbooktool.tools;

import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

import com.wnc.itbooktool.utils.MyAppParams;

import db.DbExecMgr;

public class DeleteConflict
{
    public static void main( String[] args )
    {
        DbExecMgr.refreshCon( MyAppParams.sqliteName );
        Map selectAllSqlMap = DbExecMgr
                .getSelectAllSqlMap( "SELECT min(ID) MINID, TOPIC_WORD, COUNT(*) FROM DICTIONARY GROUP BY TOPIC_WORD HAVING COUNT(*) > 1" );
        for ( int i = 1; i <= selectAllSqlMap.size(); i++ )
        {
            Map rowMap = (Map)selectAllSqlMap.get( i );
            String string = rowMap.get( "TOPIC_WORD" ).toString();
            String minId = rowMap.get( "MINID" ).toString();
            System.out.println( string + " " + minId );
            try
            {
                DbExecMgr
                        .execOnlyOneUpdate( "DELETE FROM DICTIONARY WHERE TOPIC_WORD='"
                                + StringEscapeUtils.escapeSql( string )
                                + "' AND ID>" + minId );
            } catch ( SQLException e )
            {
                e.printStackTrace();
            }
        }
    }
}

