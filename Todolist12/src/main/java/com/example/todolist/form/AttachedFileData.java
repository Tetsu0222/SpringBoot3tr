package com.example.todolist.form;

import lombok.AllArgsConstructor;
import lombok.Data;

//1件分のファイルデータを表すクラス
@Data
@AllArgsConstructor
public class AttachedFileData {
    private Integer id;
    private String  fileName;		//アップロード時のファイル名
    private String  note;
    private boolean openInNewTab;
}