package com.example.todolist.common;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;

import com.example.todolist.entity.AttachedFile;

//ヘルパーメソッドを保持するクラス
public class Utils {

	//フィールド
	private static final SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );

	//文字列をDate型へ変換 値がnullかチェック
	public static Date str2date( String s ) {
        if ( s == null || s.equals("") ) {
            return null;
        }
		long ms = 0;
		try {
			ms = sdf.parse( s ).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date( ms );
	}
	
	//文字列をDate型へ変換2
    public static Date str2dateOrNull( String s ) {
        Date date = null;
        try{
            date = new Date( sdf.parse( s ).getTime() );
        }catch( ParseException e ) {
        }
        return date;
    }

    //引数が全角SPACEかチェック
    public static boolean isAllDoubleSpace( String s ) {
        if ( s == null || s.equals("") ) {
            return true;
        }
        for ( int i = 0; i < s.length(); i++ ) {
            if ( s.charAt(i) != '　' ) {
                return false;
            }
        }
        return true;
    }

    //引数が"" or 半角SPACE/TABだけかチェック
    public static boolean isBlank( String s ) {
        if ( s == null || s.equals("") ) {
            return true;
        }
        for ( int i = 0; i < s.length(); i++ ) {
            if ( s.charAt(i) != ' ' && s.charAt( i ) != '\t' ) {
                return false;
            }
        }
        return true;
    }

    //引数が対応日付形式かチェック
    public static boolean isValidDateFormat( String s ) {
        if ( s == null || s.equals("") ) {
            return false;
        }
        try {
            LocalDate.parse( s );
            return true;
        } catch ( DateTimeException e ) {
            return false;
        }
    }

    //引数が今日以降かチェック
    public static boolean isTodayOrFurtureDate( String s ) {
        if ( s == null || s.equals("") ) {
            return false;
        }
        LocalDate deadlineDate = null;
        try {
            deadlineDate = LocalDate.parse( s );
            if (deadlineDate.isBefore( LocalDate.now() )) {
                return false;
            } else {
                return true;
            }
        } catch ( DateTimeException e ) {
            e.printStackTrace();
            return false;
        }
    }

    //日付データを文字列へ変換
    public static String date2str( Date date ) {
        String s = "";
        if ( date != null ) {
            s = sdf.format( date );
        }
        return s;
    }
    
    //日時データを文字列へ変換
    public static String date2str( Timestamp time ) {
        String s = "";
        if ( time != null ) {
            s = sdf.format( time );
        }
        return s;
    }
    
    //添付ファイルの格納ファイル名を作成する。
    public static String makeAttahcedFilePath( String path , AttachedFile af ) {
    	return path + "/" + af.getCreateTime() + "_" + af.getFileName();
    }
    
    //ファイルの拡張子からContentTypeを調べる。
    public static String ext2contentType( String ext ) {
        String contentType;
        if( ext == null ) {
            return "";
        }
        switch( ext.toLowerCase() ) {
            case "gif":
                contentType = "image/gif";
                break;
            case "jpg":
            case "jpeg":
                contentType = "image/jpeg";
                break;
            case "png":
                contentType = "image/png";
                break;
            case "pdf":
                contentType = "application/pdf";
                break;
            default:
                contentType = "";
                break;
        }
        return contentType;
    }
}