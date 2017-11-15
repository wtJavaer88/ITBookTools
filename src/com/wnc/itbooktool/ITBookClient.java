package com.wnc.itbooktool;

import com.wnc.itbooktool.dao.DictionaryDao;
import com.wnc.itbooktool.task.CBListener;

public class ITBookClient
{
    public static void main( String[] args )
    {
        new CBListener().start();
        DictionaryDao.init();
    }
}

