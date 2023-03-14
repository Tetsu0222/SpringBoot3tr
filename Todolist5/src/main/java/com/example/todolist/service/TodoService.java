package com.example.todolist.service;

import java.time.DateTimeException;
import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.example.todolist.form.TodoData;
import com.example.todolist.form.TodoQuery;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TodoService {
    // Todolist2のisValid()
    public boolean isValid(TodoData todoData, BindingResult result, boolean isCreate) {
        boolean ans = true;

        // 件名が全角スペースだけで構成されていたらエラー
//        String title = todoData.getTitle();
//        if (title != null) {
//            for (int i = 0; i < title.length(); i++) {
//                if (title.charAt(i) != '　') {
//                    break;
//                }
//                FieldError fieldError = new FieldError(
//                    result.getObjectName(),
//                    "title",
//                    "件名が全角スペースです");
//                result.addError(fieldError);
//                ans = false;
//            }
//        }

        String title = todoData.getTitle();
        if (title != null && !title.equals("")) {
            boolean isAllDoubleSpace = true;
            for (int i = 0; i < title.length(); i++) {
                if (title.charAt(i) != '　') {
                    isAllDoubleSpace = false;
                    break;
                }
            }
            if (isAllDoubleSpace) {
                FieldError fieldError = new FieldError(
                    result.getObjectName(),
                    "title",
                    "件名が全角スペースです");
                result.addError(fieldError);
                ans = false;
            }
        }

        // yyyy-mm-dd形式チェック、過去日付チェック
        String deadline = todoData.getDeadline();
        if (!deadline.equals("")) {
            LocalDate tody = LocalDate.now();
            LocalDate deadlineDate = null;
            try {
                deadlineDate = LocalDate.parse(deadline);
                // 過去日付チェックは新規登録の場合のみ
                if (isCreate) {
                    if (deadlineDate.isBefore(tody)) {
                        FieldError fieldError = new FieldError(
                            result.getObjectName(),
                            "deadline",
                            "期限を設定するときは今日以降にしてください");
                        result.addError(fieldError);
                        ans = false;
                    }
                }
            } catch (DateTimeException e) {
                FieldError fieldError = new FieldError(
                    result.getObjectName(),
                    "deadline",
                    "期限を設定するときはyyyy-mm-dd形式で入力してください");
                result.addError(fieldError);
                ans = false;
            }
        }
        return ans;
    }

    // Todolist4のisValid()
    public boolean isValid(TodoQuery todoQuery, BindingResult result) {
        boolean ans = true;

        // 期限:開始の形式をチェック
        String date = todoQuery.getDeadlineFrom();
        if (!date.equals("")) {
            try {
                LocalDate.parse(date);
            } catch (DateTimeException e) {
                // parseできない場合
                FieldError fieldError = new FieldError(
                    result.getObjectName(),
                    "deadlineFrom",
                    "期限：開始を入力するときはyyyy-mm-dd形式で入力してください");
                result.addError(fieldError);
                ans = false;
            }
        }
        // 期限:終了の形式をチェック
        date = todoQuery.getDeadlineTo();
        if (!date.equals("")) {
            try {
                LocalDate.parse(date);
            } catch (DateTimeException e) {
                // parseできない場合
                FieldError fieldError = new FieldError(
                    result.getObjectName(),
                    "deadlineTo",
                    "期限：終了を入力するときはyyyy-mm-dd形式で入力してください");
                result.addError(fieldError);
                ans = false;
            }
        }
        return ans;
    }
}
