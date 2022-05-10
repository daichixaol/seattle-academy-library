package jp.co.seattle.library.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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
@Controller // APIの入り口
public class BulkregistController {
	final static Logger logger = LoggerFactory.getLogger(BulkregistController.class);

	@Autowired
	private BooksService booksService;

	@Autowired
	private ThumbnailService thumbnailService;
	/**
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/bulk", method = RequestMethod.GET) // value＝actionで指定したパラメータ
	// RequestParamでname属性を取得
	public String bulkregist(Model model) {
		return "bulk";
	}

	/**
	 * 書籍情報を登録する
	 * 
	 * @param locale    ロケール情報
	 * @param title     書籍名
	 * @param author    著者名
	 * @param publisher 出版社
	 * @param file      サムネイルファイル
	 * @param model     モデル
	 * @return 遷移先画面
	 * @throws IOException 
	 */

	@Transactional
	@RequestMapping(value = "/bulkregistBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
	public String bulkregistBook(Locale locale, @RequestParam("csvfile") MultipartFile file, Model model) throws IOException {

		List<String> listE = new ArrayList<String>();
		List<BookDetailsInfo> listNe = new ArrayList<BookDetailsInfo>();

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
			String line = "";
			int count = 0;
			while ((line = br.readLine()) != null) {

				final String[] split = line.split(",", -1);
				count++;

				BookDetailsInfo bookInfo = new BookDetailsInfo();

				boolean validNecessary = (split[0].isEmpty() || split[1].isEmpty() || split[2].isEmpty()
						|| split[3].isEmpty());
				boolean validDate = split[3].matches("[0-9]{8}$");
				boolean validIsbn1 = split[4].matches("^[0-9]{0,10}$");
				boolean validIsbn2 = split[4].matches("^[0-9]{0,13}$");


				if (validNecessary || !validDate || !validIsbn1 && !validIsbn2) {
					listE.add(count + "行目の書籍登録でエラーが起きました。");
				}
				bookInfo.setTitle(split[0]);
				bookInfo.setAuthor(split[1]);
				bookInfo.setPublisher(split[2]);
				bookInfo.setPublishDate(split[3]);
				bookInfo.setIsbn(split[4]);
				listNe.add(bookInfo);	
			}


			if (listNe.size() == 0) {
				model.addAttribute("nodata","ファイルが空です。");
				return "bulk";
			}

			if (listE.size()>0) {
				model.addAttribute("listE",listE);
				return "bulk";
			}
			for (BookDetailsInfo bookInfo : listNe){
				booksService.registBook(bookInfo);

			}
			model.addAttribute("bookList", booksService.getBookList());
			return "home";
		}
	}
}
