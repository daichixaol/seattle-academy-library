package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.ThumbnailService;

/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class EditBookController {
    final static Logger logger = LoggerFactory.getLogger(EditBookController.class);

    @Autowired
    private BooksService booksService;

    @Autowired
    private ThumbnailService thumbnailService;

    @RequestMapping(value = "/editBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8") //value＝actionで指定したパラメータ
    //RequestParamでname属性を取得
    public String editBook(Model model, Locale locale, @RequestParam("bookId") int bookId) {
    	model.addAttribute("bookInfo",booksService.getBookInfo(bookId));
        return "edit";
    }

    /**
     * 書籍情報を編集する
     * @param locale ロケール情報
     * @param title 書籍名
     * @param author 著者名
     * @param publisher 出版社
     * @param file サムネイルファイル
     * @param model モデル
     * @param Id id
     * @return 遷移先画面
     */
    @Transactional
    @RequestMapping(value = "/updateBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public String updateBook(Locale locale,
    		@RequestParam("bookId")int bookId,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("publisher") String publisher,
            @RequestParam("publishDate") String publish_date,
            @RequestParam("isbn") String isbn,
            @RequestParam("explanation") String explanation,
            @RequestParam("thumbnail") MultipartFile file,
            Model model) {
        logger.info("Welcome editBooks.java! The client locale is {}.", locale);

        // パラメータで受け取った書籍情報をDtoに格納する。
        BookDetailsInfo bookInfo = new BookDetailsInfo();
        bookInfo.setBookId(bookId);
        bookInfo.setTitle(title);
        bookInfo.setAuthor(author);
        bookInfo.setPublisher(publisher);
        bookInfo.setPublishDate(publish_date);
        bookInfo.setIsbn(isbn);
        bookInfo.setExplanation(explanation);
        // クライアントのファイルシステムにある元のファイル名を設定する
        String thumbnail = file.getOriginalFilename();

        if (!file.isEmpty()) {
            try {
                // サムネイル画像をアップロード
                String fileName = thumbnailService.uploadThumbnail(thumbnail, file);
                // URLを取得
                String thumbnailUrl = thumbnailService.getURL(fileName);

                bookInfo.setThumbnailName(fileName);
                bookInfo.setThumbnailUrl(thumbnailUrl);

            } catch (Exception e) {

                // 異常終了時の処理
                logger.error("サムネイルアップロードでエラー発生", e);
                model.addAttribute("bookDetailsInfo", bookInfo);
                return "edit";
            }
        }
        
 
        boolean validNecessary = (title.isEmpty() || author.isEmpty() || publisher.isEmpty() || publish_date.isEmpty());
        boolean validDate = publish_date.matches("[0-9]{8}$");
        boolean validIsbn1 = isbn.matches("^[0-9]{10}$");
        boolean validIsbn2 = isbn.matches("^[0-9]{13}$");
        
        if(validNecessary) {
        	model.addAttribute("necessaryError","必須項目を入力してください");
        }
        
        if (!validDate) {
        	model.addAttribute("dateError","出版日は半角数字のYYYYMMDD形式で入力してください");
        }
       if (!validIsbn1 && !validIsbn2) {
        	model.addAttribute("isbnError","ISBNの桁数または半角数字が正しくありません");	
        }
      
       if(validNecessary || !validDate || !validIsbn1 && !validIsbn2) {
    	   model.addAttribute("bookInfo",bookInfo);
    	  return "edit";
       }
       
        // 書籍情報を編集する
        booksService.editBook(bookInfo);
        int editId = bookInfo.getBookId();

        // TODO 登録した書籍の詳細情報を表示すよに実装
        model.addAttribute("bookDetailsInfo",booksService.getBookInfo(editId));
       
        //  詳細画面に遷移する
        return "details";
    }

}

