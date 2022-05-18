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

/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class SerchController {
	final static Logger logger = LoggerFactory.getLogger(SerchController.class);

	@Autowired
	private BooksService booksService;

	/**
	 * 書籍情報を検索する
	 * 
	 * @param locale ロケール情報
	 * @param title 書籍名
	 * @param model モデル情報
	 * @return 遷移先画面
	 */
	
	@Transactional
	@RequestMapping(value = "/searchBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
	public String searchBook(Locale locale,
			@RequestParam("title") String title,
			Model model) {
		logger.info("Welcome insertBooks.java! The client locale is {}.", locale);

		model.addAttribute("bookList", booksService.searchBook(title));
		return "home";


	}
}