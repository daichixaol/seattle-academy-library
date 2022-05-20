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

import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.LentBooksService;

/**
 * 返却コントローラー
 */
@Controller //APIの入り口
public class ReturnBookController {
	final static Logger logger = LoggerFactory.getLogger(ReturnBookController.class);
	@Autowired
	private LentBooksService lentbooksService;
	@Autowired
	private BooksService booksService;
	/**
	 * 対象書籍を返却する
	 *
	 * @param locale ロケール情報
	 * @param bookId 書籍ID
	 * @param model モデル情報
	 * @return 遷移先画面名
	 */
	@Transactional
	@RequestMapping(value = "/returnBook", method = RequestMethod.POST)
	public String returnBook(
			Locale locale,
			@RequestParam("bookId") Integer bookId,
			Model model) {


		logger.info("Welcome lent! The client locale is {}.", locale);  
		
		int lentSecondCheck = lentbooksService.lentSecondCheck(bookId);
		
		if (lentSecondCheck > 0) {
			lentbooksService.updateReturn(bookId);
		}else if (lentSecondCheck == 0){
			model.addAttribute("notLent","貸出しされていません。");
		}
		
		
		
		//int beforelent = lentbooksService.lentBooks();

		//lentbooksService.returnBook(bookId);
		model.addAttribute("bookDetailsInfo",booksService.getBookInfo(bookId));  

		//int afterlent = lentbooksService.lentBooks();      

		//if(beforelent == afterlent){
			
		//}

		return "details";

	}

}