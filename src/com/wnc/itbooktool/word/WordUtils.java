package com.wnc.itbooktool.word;

import com.wnc.itbooktool.dao.DictionaryDao;

public class WordUtils
{
    public static DicWord findFromCache( String clipBoardContent )
    {
        for ( DicWord d : OptedDictData.getSeekWordList() )
        {
            if ( d != null && hasFindWord( clipBoardContent, d ) )
            {
                return d;
            }
        }
        return null;
    }

    public static boolean hasFindWord( String clipBoardContent, DicWord d )
    {
        return clipBoardContent.equalsIgnoreCase( d.getBase_word() )
                || clipBoardContent.equalsIgnoreCase( d.getWord_done() )
                || clipBoardContent.equalsIgnoreCase( d.getWord_er() )
                || clipBoardContent.equalsIgnoreCase( d.getWord_est() )
                || clipBoardContent.equalsIgnoreCase( d.getWord_ing() )
                || clipBoardContent.equalsIgnoreCase( d.getWord_past() )
                || clipBoardContent.equalsIgnoreCase( d.getWord_pl() )
                || clipBoardContent.equalsIgnoreCase( d.getWord_third() );
    }

    public static DicWord findDicWord( String clipBoardContent )
    {
        DicWord fw = findFromCache( clipBoardContent );
        if ( fw == null )
        {
            fw = DictionaryDao.findWeightWord( clipBoardContent );
        }
        if ( fw == null )
        {
            fw = DictionaryDao.findWord( clipBoardContent );
        }
        return fw;
    }
}
