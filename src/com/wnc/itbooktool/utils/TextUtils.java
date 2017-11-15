package com.wnc.itbooktool.utils;

import com.wnc.string.PatternUtil;
import com.wnc.tools.TextFormatUtil;

public class TextUtils
{
    public static boolean isSequence( String clipBoardContent )
    {
        return !clipBoardContent.startsWith( "http" )
                && !TextFormatUtil.containsChinese( clipBoardContent )
                && PatternUtil.getAllPatternGroup( clipBoardContent,
                        "([a-zA-Z]+)" ).size() > 1;
    }

    public static boolean isSingleWord( String clipBoardContent )
    {
        return clipBoardContent.matches( "[a-zA-Z_\\-]+" );
    }
}

