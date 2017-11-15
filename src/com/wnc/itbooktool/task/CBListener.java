package com.wnc.itbooktool.task;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicFileUtil;
import com.wnc.itbooktool.utils.ClipBoardUtils;
import com.wnc.itbooktool.utils.MyAppParams;
import com.wnc.itbooktool.utils.TextUtils;
import com.wnc.itbooktool.word.DicWord;
import com.wnc.itbooktool.word.OptedDictData;
import com.wnc.itbooktool.word.WordUtils;
import com.wnc.run.RunCmd;

public class CBListener extends Thread
{
    String last = null;

    @Override
    public void run()
    {
        super.run();
        while ( true )
        {
            try
            {
                Thread.sleep( 500 );
                String clipboardString = ClipBoardUtils.getClipboardString();
                if ( clipboardString == null )
                {
                    continue;
                }

                if ( isValidStr( clipboardString ) )
                {
                    if ( TextUtils.isSequence( clipboardString ) )
                    {
                        System.out.println( "句子:" + clipboardString );
                        writeToBookLog( clipboardString );
                    } else if ( TextUtils.isSingleWord( clipboardString ) )
                    {
                        System.err.println( "单词:" + clipboardString );
                        DicWord findWord = WordUtils
                                .findDicWord( clipboardString );
                        if ( findWord != null )
                        {
                            findSuccess( clipboardString, findWord );
                        } else
                        {
                            findFailed( clipboardString );
                        }
                    }
                }
                last = clipboardString;
            } catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
    }

    private boolean isValidStr( String clipboardString )
    {
        return StringUtils.isNotBlank( last ) && !clipboardString.equals( last );
    }

    private void findFailed( String clipBoardContent )
    {
        System.out.println( "无法找到该单词" );
        writeToBookLog( clipBoardContent, null, 1 );
    }

    private void findSuccess( String clipBoardContent, DicWord findWord )
    {
        OptedDictData.addSeekWord( findWord );
        System.out.println( findWord.getCn_mean() );
        writeToBookLog( clipBoardContent, findWord, 1 );
        RunCmd.runCommand( getAlertText( clipBoardContent, findWord ) );
    }

    private String getAlertText( String clipBoardContent, DicWord findWord )
    {
        return String.format( "cmd /c %s \"%s\" \"%s\"",
                MyAppParams.getAlertExe(),
                findWord.getCn_mean().replaceAll( "['\"]", "" ),
                clipBoardContent );
    }

    private void writeToBookLog( String clipBoardContent )
    {
        writeToBookLog( clipBoardContent, null, 2 );
    }

    private void writeToBookLog( String clipBoardContent, DicWord object,
            int type )
    {
        JSONObject jobj = new JSONObject();
        jobj.put( "content", clipBoardContent );
        jobj.put( "type", type );
        if ( object != null )
        {
            jobj.put( "topic", object.getId() );
        }
        jobj.put( "time", BasicDateUtil.getCurrentDateTimeString() );

        BasicFileUtil.writeFileString( MyAppParams.getInstance()
                .getTodayLogPath(), jobj.toJSONString() + "\r\n", null, true );
    }
}

