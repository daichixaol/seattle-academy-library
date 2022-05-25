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
 * 貸し出しコントローラー
 */
@Controller //APIの入り口
public class LendBookController {
	final static Logger logger = LoggerFactory.getLogger(LendBookController.class);
	@Autowired
	private LentBooksService lentbooksService;
	@Autowired
	private BooksService booksService;
	/**
	 * 対象書籍を貸し出しする
	 *
	 * @param locale ロケール情報
	 * @param bookId 書籍ID
	 * @param model モデル情報
	 * @return 遷移先画面名
	 */
	@Transactional
	@RequestMapping(value = "/rentBook", method = RequestMethod.POST)
	public String lendBook(
			Locale locale,
			@RequestParam("bookId") Integer bookId,
			Model model) {


		logger.info("Welcome lent! The client locale is {}.", locale);  

		int lentCheck = lentbooksService.lentCheck(bookId);
		int lentSecondCheck = lentbooksService.lentSecondCheck(bookId);

		if (lentCheck == 0) {
			lentbooksService.lendBook(bookId);
		}else if (lentSecondCheck == 0){
			lentbooksService.updateLent(bookId);
		}else if (lentSecondCheck > 0){
			model.addAttribute("alreadyLent","貸出済みです。");
		}

	//既に借りている書籍の数を確認
	//int beforelent = lentbooksService.lentBooks();


	model.addAttribute("bookDetailsInfo",booksService.getBookInfo(bookId));  

	//借りるボタンを押した後に借りている書籍の数を確認
	//int afterlent = lentbooksService.lentBooks();      

	//if(beforelent == afterlent){
	//model.addAttribute("alreadyLent","貸出済みです。");



	return "details";

}

}