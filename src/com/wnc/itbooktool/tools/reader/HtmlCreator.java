package com.wnc.itbooktool.tools.reader;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.wnc.basic.BasicFileUtil;
import com.wnc.itbooktool.utils.MyAppParams;
import com.wnc.string.PatternUtil;

public class HtmlCreator
{
    static
    {
        createCssFile();
    }

    public void createHtml( Set<String> unKnown, Set<Integer> duplicateSet,
            List<Integer> inSentenceList, Map<Integer, Map> explainMap )
    {
        StringBuilder sb = new StringBuilder( "" );
        generatHead( sb );
        generateBody( unKnown, duplicateSet, inSentenceList, explainMap, sb );
        BasicFileUtil.writeFileString( MyAppParams.getHtmlPath(),
                sb.toString(), "GBK", false );
    }

    private void generateBody( Set<String> unKnown, Set<Integer> duplicateSet,
            List<Integer> inSentenceList, Map<Integer, Map> explainMap,
            StringBuilder sb )
    {
        sb.append( "<body>" );
        sb.append( "<div class='content-center'>" );
        sb.append( "<table class='tableui' cellpadding='4' cellspacing='2'>" );
        sb.append( "<tbody><tr><td></td><td align='center'><b>ID</b></td><td align='center'><b>TOPIC_WORD</b></td><td align='center'><b>MEAN_CN</b>"
                + "</td><td align='center'><b>TIME</b></td><td align='center'><b>WEIGHT</b></td></tr></tbody>" );
        String rowstyle = "";
        for ( int i = inSentenceList.size() - 1; i >= 0; i-- )
        {
            Integer key = inSentenceList.get( i );
            Map rowMap = (Map)explainMap.get( key );
            rowstyle = duplicateSet.contains( key ) ? "row-duplicate"
                    : "row-once";
            sb.append( String
                    .format(
                            "<tr class='"
                                    + rowstyle
                                    + "'><td>%d</td><td>%s</td> <td>%s</td> <td>%s</td> <td>%s</td> <td>%s</td></tr>",
                            (inSentenceList.size() - i),
                            String.valueOf( rowMap.get( "ID" ) ),
                            String.valueOf( rowMap.get( "TOPIC_WORD" ) ),
                            String.valueOf( rowMap.get( "MEAN_CN" ) ).replace(
                                    "\n", "</br>" ), PatternUtil
                                    .getLastPattern( String.valueOf( rowMap
                                            .get( "TIME" ) ),
                                            "\\d+-\\d+ \\d+:\\d+" ), String
                                    .valueOf( rowMap.get( "WEIGHT" ) ) ) );
        }
        sb.append( "</table>" );
        if ( unKnown.size() > 0 )
        {
            sb.append( "<h2>未知(" + unKnown.size() + "):"
                    + StringUtils.join( unKnown, ", " ) + "</h2>" );
        }
        sb.append( "</div>" );
        sb.append( "</body>" );
    }

    private void generatHead( StringBuilder sb )
    {
        sb.append( "<head><link rel='stylesheet' href='reader.css'></head>" );
    }

    /**
     * 新建样式文件
     */
    private static void createCssFile()
    {
        String s = ".content-center {margin:0 auto;text-align:center;width:90%;}\r\n";
        s += ".row-duplicate {font-color: red;background-color: #00a4b5;font-size: 16px;}\r\n";
        s += ".row-once {background-color: #eeeeee;font-size: 16px;}\r\n";
        s += ".tableui{border:#ccc 1px solid;}\r\n";
        s += "table td{ border:1px solid #ccc;}\r\n";
        BasicFileUtil.writeFileString( MyAppParams.getCssPath(), s, "GBK",
                false );
    }
}

