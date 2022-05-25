package jp.co.seattle.library.dto;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 貸出基本情報格納DTO
 */
@Configuration
@Data
public class HistoryInfo {

    private int bookId;

    private String title;

    private String lentDate;
    
    private String returnDate;
    
    public HistoryInfo() {

    }

    // コンストラクタ
    public HistoryInfo(int bookId, String title, String lentDate, String returnDate) {
        this.bookId = bookId;
        this.title = title;
        this.lentDate = lentDate;
        this.returnDate = returnDate;
    }

}