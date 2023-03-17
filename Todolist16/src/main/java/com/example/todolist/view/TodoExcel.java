package com.example.todolist.view;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.example.todolist.entity.Task;
import com.example.todolist.entity.Todo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TodoExcel extends AbstractXlsxView {
    private final int COL_B = 1;
    private final int COL_C = 2;
    private final int COL_D = 3;
    private final int COL_E = 4;
    private final int COL_F = 5;
    private final int COL_G = 6;
    private final int COL_H = 7;
    private final int COL_I = 8;

    private final int TODO_START_ROW = 5;

    private final int[] COLUMN_WIDTH = { 0, 256 * 24, 256 * 12, 256 * 12, 256 * 14, 256 * 12,
            256 * 24, 256 * 14, 256 * 12 };
    private final String[] HEADER_1 = { "", "　Todo", "", "", "", "", "　Task", "", "" };
    private final String[] HEADER_2 = { "", "件名", "重要度", "緊急度", "期限", "完了", "件名", "期限", "完了" };
    private final String[] HEADER_3 = { "", "Todo数", "完了", "未完了", "完了率", "Task数", "完了", "未完了",
            "完了率" };

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook,
                                      HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        // ダウンロードファイル名をセット
        String fileName = (String)model.get("fileName");
        response
            .setHeader("Content-Disposition",
                "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");

        // フォンサイズ 16
        CellStyle fontSize16_bold = workbook.createCellStyle();
        Font font16_bold = workbook.createFont();
        font16_bold.setFontName("MS Pゴシック");
        font16_bold.setFontHeightInPoints((short)16);
        font16_bold.setBold(true);
        fontSize16_bold.setFont(font16_bold);

        // 網掛け + 罫線 上右下左 + 文字中央寄せ + フォント12
        CellStyle pattern_borderAll_fontSize12_center_bold = workbook.createCellStyle();
        pattern_borderAll_fontSize12_center_bold.setFillPattern(FillPatternType.LESS_DOTS);
        pattern_borderAll_fontSize12_center_bold.setBorderTop(BorderStyle.HAIR);
        pattern_borderAll_fontSize12_center_bold.setBorderRight(BorderStyle.HAIR);
        pattern_borderAll_fontSize12_center_bold.setBorderBottom(BorderStyle.HAIR);
        pattern_borderAll_fontSize12_center_bold.setBorderLeft(BorderStyle.HAIR);
        pattern_borderAll_fontSize12_center_bold.setAlignment(HorizontalAlignment.CENTER);

        Font font12_bold = workbook.createFont();
        font12_bold.setFontName("MS Pゴシック");
        font12_bold.setFontHeightInPoints((short)12);
        font12_bold.setBold(true);

        pattern_borderAll_fontSize12_center_bold.setFont(font12_bold);

        // 網掛け + 罫線 上左 + フォント12
        CellStyle pattern_borderLeftCorner_fontSize12_bold = workbook.createCellStyle();
        pattern_borderLeftCorner_fontSize12_bold.setFillPattern(FillPatternType.LESS_DOTS);
        pattern_borderLeftCorner_fontSize12_bold.setBorderTop(BorderStyle.HAIR);
        pattern_borderLeftCorner_fontSize12_bold.setBorderLeft(BorderStyle.HAIR);

        pattern_borderLeftCorner_fontSize12_bold.setFont(font12_bold);

        // 網掛け + 罫線 上下 + フォント12
        CellStyle pattern_borderTopBttom_fontSize12_bold = workbook.createCellStyle();
        pattern_borderTopBttom_fontSize12_bold.setFillPattern(FillPatternType.LESS_DOTS);
        pattern_borderTopBttom_fontSize12_bold.setBorderTop(BorderStyle.HAIR);
        pattern_borderTopBttom_fontSize12_bold.setBorderBottom(BorderStyle.HAIR);

        pattern_borderTopBttom_fontSize12_bold.setFont(font12_bold);

        // 網掛け + 罫線 上右 + フォント12
        CellStyle pattern_borderRightCorner_fontSize12_bold = workbook.createCellStyle();
        pattern_borderRightCorner_fontSize12_bold.setFillPattern(FillPatternType.LESS_DOTS);
        pattern_borderRightCorner_fontSize12_bold.setBorderTop(BorderStyle.HAIR);
        pattern_borderRightCorner_fontSize12_bold.setBorderRight(BorderStyle.HAIR);

        pattern_borderRightCorner_fontSize12_bold.setFont(font12_bold);

        // 罫線 上右下左 + フォント12
        CellStyle borderAll_fontSize12 = workbook.createCellStyle();
        borderAll_fontSize12.setBorderTop(BorderStyle.HAIR);
        borderAll_fontSize12.setBorderRight(BorderStyle.HAIR);
        borderAll_fontSize12.setBorderBottom(BorderStyle.HAIR);
        borderAll_fontSize12.setBorderLeft(BorderStyle.HAIR);

        Font font12 = workbook.createFont();
        font12.setFontName("MS Pゴシック");
        font12.setFontHeightInPoints((short)12);
        borderAll_fontSize12.setFont(font12);

        // 罫線 上右下左 + 文字中央寄せ + フォント12
        CellStyle borderAll_fontSize12_center = workbook.createCellStyle();
        borderAll_fontSize12_center.setBorderTop(BorderStyle.HAIR);
        borderAll_fontSize12_center.setBorderRight(BorderStyle.HAIR);
        borderAll_fontSize12_center.setBorderBottom(BorderStyle.HAIR);
        borderAll_fontSize12_center.setBorderLeft(BorderStyle.HAIR);
        borderAll_fontSize12_center.setAlignment(HorizontalAlignment.CENTER);

        // ここで新しいFontを作成してCellStyleへセットしないとborderAll_fontSize12と同じものと認識される模様
        // POIの不具合?borderAll_fontSi
        Font _font12 = workbook.createFont();
        _font12.setFontName("MS Pゴシック");
        _font12.setFontHeightInPoints((short)12);

        borderAll_fontSize12_center.setFont(_font12);

        // フォント12 + 下線 + 太字
        CellStyle fontSize12_underline_bold = workbook.createCellStyle();
        Font font12_underline_bold = workbook.createFont();
        font12_underline_bold.setFontName("MS Pゴシック");
        font12_underline_bold.setFontHeightInPoints((short)12);
        font12_underline_bold.setUnderline(Font.U_SINGLE);
        font12_underline_bold.setBold(true);
        fontSize12_underline_bold.setFont(font12_underline_bold);

        // 罫線 上右下左 + 文字中央寄せ + フォント12 + 赤 + 太字 + %
        CellStyle borderAll_fontSize12_center_red_bold_percent = workbook.createCellStyle();
        borderAll_fontSize12_center_red_bold_percent.setBorderTop(BorderStyle.HAIR);
        borderAll_fontSize12_center_red_bold_percent.setBorderRight(BorderStyle.HAIR);
        borderAll_fontSize12_center_red_bold_percent.setBorderBottom(BorderStyle.HAIR);
        borderAll_fontSize12_center_red_bold_percent.setBorderLeft(BorderStyle.HAIR);
        borderAll_fontSize12_center_red_bold_percent.setAlignment(HorizontalAlignment.CENTER);

        Font font12_red_bold = workbook.createFont();
        font12_red_bold.setFontName("MS Pゴシック");
        font12_red_bold.setFontHeightInPoints((short)12);
        font12_red_bold.setColor(IndexedColors.RED.getIndex());
        font12_red_bold.setBold(true);

        DataFormat format = workbook.createDataFormat();
        short percent = format.getFormat("0.0%");
        borderAll_fontSize12_center_red_bold_percent.setDataFormat(percent);

        borderAll_fontSize12_center_red_bold_percent.setFont(font12_red_bold);

        // シート作成
        Sheet sheet = workbook.createSheet("ToDo List");
        // 枠線を消去
        sheet.setDisplayGridlines(false);
        // 列幅設定
        for (int col = COL_B; col <= COL_I; col++) {
            sheet.setColumnWidth(col, COLUMN_WIDTH[col]);
        }

        // タイトル設定
        setCellValue(sheet, 1, COL_B, "ToDo List", fontSize16_bold);

        // 見出し文字設定
        for (int col = COL_B; col <= COL_I; col++) {
            setCellValue(sheet, 3, col, HEADER_1[col]);
            setCellValue(sheet, 4, col, HEADER_2[col], pattern_borderAll_fontSize12_center_bold);
        }

        // 見出しのスタイル設定
        getCell(sheet, 3, COL_B).setCellStyle(pattern_borderLeftCorner_fontSize12_bold);
        getCell(sheet, 3, COL_C).setCellStyle(pattern_borderTopBttom_fontSize12_bold);
        getCell(sheet, 3, COL_D).setCellStyle(pattern_borderTopBttom_fontSize12_bold);
        getCell(sheet, 3, COL_E).setCellStyle(pattern_borderTopBttom_fontSize12_bold);
        getCell(sheet, 3, COL_F).setCellStyle(pattern_borderTopBttom_fontSize12_bold);
        getCell(sheet, 3, COL_G).setCellStyle(pattern_borderLeftCorner_fontSize12_bold);
        getCell(sheet, 3, COL_H).setCellStyle(pattern_borderTopBttom_fontSize12_bold);
        getCell(sheet, 3, COL_I).setCellStyle(pattern_borderRightCorner_fontSize12_bold);

        // Todo取得
        @SuppressWarnings("unchecked")
        List<Todo> todoList = (List<Todo>)model.get("todoList");

        // セルにセット
        int row = TODO_START_ROW - 1;
        // すべてのTodoに対して以下を実行
        for (Todo todo : todoList) {
            // 1行下へ
            ++row;

            // Todoをセット
            setCellValue(sheet, row, COL_B, todo.getTitle(), borderAll_fontSize12);
            setCellValue(sheet, row, COL_C, (todo.getImportance() == 1 ? "★★★" : "★"),
                borderAll_fontSize12_center);
            setCellValue(sheet, row, COL_D, (todo.getUrgency() == 1 ? "★★★" : "★"),
                borderAll_fontSize12_center);
            setCellValue(sheet, row, COL_E,
                (todo.getDeadline() == null ? "-" : "" + todo.getDeadline()),
                borderAll_fontSize12_center);
            setCellValue(sheet, row, COL_F, (todo.getDone().equals("Y") ? "■" : "□"),
                borderAll_fontSize12_center);
            setCellValue(sheet, row, COL_G, "", borderAll_fontSize12_center);
            setCellValue(sheet, row, COL_H, "", borderAll_fontSize12_center);
            setCellValue(sheet, row, COL_I, "", borderAll_fontSize12_center);

            // すべてのTaskに対して以下を実行
            for (Task task : todo.getTaskList()) {
                // 1行下へ
                ++row;

                // Taskをセット
                setCellValue(sheet, row, COL_B, "", borderAll_fontSize12_center);
                setCellValue(sheet, row, COL_C, "", borderAll_fontSize12_center);
                setCellValue(sheet, row, COL_D, "", borderAll_fontSize12_center);
                setCellValue(sheet, row, COL_E, "", borderAll_fontSize12_center);
                setCellValue(sheet, row, COL_F, "", borderAll_fontSize12_center);
                setCellValue(sheet, row, COL_G, task.getTitle(), borderAll_fontSize12);
                setCellValue(sheet, row, COL_H,
                    (task.getDeadline() == null ? "-" : "" + task.getDeadline()),
                    borderAll_fontSize12_center);
                setCellValue(sheet, row, COL_I, (task.getDone().equals("Y") ? "■" : "□"),
                    borderAll_fontSize12_center);

            }
        }

        // Todoの最終行を退避
        int todoEndRow = row;

        // 集計結果見出し
        row += 2;
        setCellValue(sheet, row, COL_B, "　集計表　", fontSize12_underline_bold);

        row += 2;
        for (int col = COL_B; col <= COL_I; col++) {
            setCellValue(sheet, row, col, HEADER_3[col], pattern_borderAll_fontSize12_center_bold);
        }

        // 1行下
        ++row;

        // 集計結果
        if (row == 9) {
            // Todoがなかった場合
            setCellValue(sheet, row, COL_B, "-", borderAll_fontSize12_center);
            setCellValue(sheet, row, COL_C, "-", borderAll_fontSize12_center);
            setCellValue(sheet, row, COL_D, "-", borderAll_fontSize12_center);
            setCellValue(sheet, row, COL_E, "-", borderAll_fontSize12_center_red_bold_percent);

            setCellValue(sheet, row, COL_F, "-", borderAll_fontSize12_center);
            setCellValue(sheet, row, COL_G, "-", borderAll_fontSize12_center);
            setCellValue(sheet, row, COL_H, "-", borderAll_fontSize12_center);
            setCellValue(sheet, row, COL_I, "-", borderAll_fontSize12_center_red_bold_percent);

        } else {
            // Todo数
            getCell(sheet, row, COL_B).setCellFormula("C" + (row + 1) + " + D" + (row + 1));
            // 完了
            getCell(sheet, row, COL_C)
                .setCellFormula("COUNTIF(F" + (TODO_START_ROW + 1) + ":F" + (todoEndRow+1) + ",\"■\")");
            // 未完了
            getCell(sheet, row, COL_D)
                .setCellFormula("COUNTIF(F" + (TODO_START_ROW + 1) + ":F" + (todoEndRow+1) + ",\"□\")");
            // 完了率
            getCell(sheet, row, COL_E)
                .setCellFormula(
                    "IF(B" + (row + 1) + "=0,\"-\",C" + (row + 1) + "/B" + (row + 1) + ")");

            // Task数
            getCell(sheet, row, COL_F).setCellFormula("G" + (row + 1) + " + H" + (row + 1));
            // 完了
            getCell(sheet, row, COL_G)
                .setCellFormula("COUNTIF(I" + (TODO_START_ROW + 1) + ":I" + (todoEndRow+1) + ",\"■\")");
            // 未完了
            getCell(sheet, row, COL_H)
                .setCellFormula("COUNTIF(I" + (TODO_START_ROW + 1) + ":I" + (todoEndRow+1) + ",\"□\")");
            // 完了率
            getCell(sheet, row, COL_I)
                .setCellFormula(
                    "IF(F" + (row + 1) + "=0,\"-\",G" + (row + 1) + "/F" + (row + 1) + ")");

            // セルスタイル設定
            // Todo
            setCellStyle(sheet, row, COL_B, borderAll_fontSize12_center);
            setCellStyle(sheet, row, COL_C, borderAll_fontSize12_center);
            setCellStyle(sheet, row, COL_D, borderAll_fontSize12_center);
            setCellStyle(sheet, row, COL_E, borderAll_fontSize12_center_red_bold_percent);

            // Task
            setCellStyle(sheet, row, COL_F, borderAll_fontSize12_center);
            setCellStyle(sheet, row, COL_G, borderAll_fontSize12_center);
            setCellStyle(sheet, row, COL_H, borderAll_fontSize12_center);
            setCellStyle(sheet, row, COL_I, borderAll_fontSize12_center_red_bold_percent);

        }

        // サーバー側で再計算
        workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();

        // ファイルを開いたときに再計算
        // sheet.setForceFormulaRecalculation(true);
    }

    /**
     * 指定されたシート(行番号, 列番号）にセルスタイルを設定する<br>
     * 
     * @param sheet      シート
     * @param rowNumber  行番号
     * @param cellNumber 列番号
     * @param cellStyle  設定するセルスタイル
     */
    private void setCellStyle(Sheet sheet, int rowNumber, int cellNumber, CellStyle cellStyle) {
        Cell cell = getCell(sheet, rowNumber, cellNumber);
        cell.setCellStyle(cellStyle);
    }

    /**
     * 指定されたシート(行番号, 列番号）のセルに文字列、セルスタイルを設定する<br>
     * 
     * @param sheet      シート
     * @param rowNumber  行番号
     * @param cellNumber 列番号
     * @param text       設定する文字列
     * @param cellStyle  設定するセルスタイル
     */
    private void setCellValue(Sheet sheet, int rowNumber, int cellNumber, String text,
                              CellStyle cellStyle) {
        setCellValue(sheet, rowNumber, cellNumber, text);
        Cell cell = getCell(sheet, rowNumber, cellNumber);
        cell.setCellStyle(cellStyle);
    }

    /**
     * 指定されたシート(行番号, 列番号）のセルに文字列を設定する<br>
     * 
     * @param sheet      シート
     * @param rowNumber  行番号
     * @param cellNumber 列番号
     * @param text       設定する文字列
     */
    private void setCellValue(Sheet sheet, int rowNumber, int cellNumber, String text) {
        Cell cell = getCell(sheet, rowNumber, cellNumber);
        cell.setCellValue(text);
    }

    /**
     * 指定されたシート(行番号, 列番号）のセルを返す<br>
     * 呼び出された時点で該当するセルが存在しなければ生成して返す。
     * 
     * @param sheet      シート
     * @param rowNumber  行番号
     * @param cellNumber 列番号
     * @return 指定されたセル（存在しなければ生成して返す)
     */
    private Cell getCell(Sheet sheet, int rowNumber, int cellNumber) {
        // シートから行を取得（なければ作成）
        Row row = sheet.getRow(rowNumber);
        if (row == null) {
            row = sheet.createRow(rowNumber);
        }
        // 行からセルを取得（なければ作成)
        Cell cell = row.getCell(cellNumber);
        if (cell == null) {
            cell = row.createCell(cellNumber);
        }
        return cell;
    }

}