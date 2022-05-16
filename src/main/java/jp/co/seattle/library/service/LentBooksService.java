package jp.co.seattle.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * 貸し出し書籍サービス
 * 
 *  lentbooksテーブルに関する処理を実装する
 */
@Service
public class LentBooksService {

	final static Logger logger = LoggerFactory.getLogger(BooksService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;


	/**
	 * 書籍を貸し出しテーブルに追加する
	 * 
	 * @param bookId　書籍ID
	 */

	public void lendBook(int bookId) {

		String sql = "insert into lentbooks (bookid) select " + bookId + " where NOT EXISTS (select bookid from lentbooks where bookid=" + bookId + ")";

		jdbcTemplate.update(sql);
	}

	/**
	 * 貸し出し中の書籍の数を数える
	 * 
	 * @return lentBooks　遷移先画面
	 */

	public int lentBooks() {
		String sql = "select count (bookid) from lentbooks";
		int lentBooks = jdbcTemplate.queryForObject(sql,int.class); 
		return lentBooks;
	}

	/**
	 * 書籍を返却し、貸し出しテーブルから削除する
	 * 
	 * @param bookId　書籍ID
	 */

	public void returnBook(int bookId) {

		String sql = "delete from lentbooks where bookid =" + bookId;

		jdbcTemplate.update(sql);
	}

	public int deleteBookCheck(int bookId) {
		try {
			String sql = "select bookid from lentbooks where bookid =" + bookId;
			int deleteCheck = jdbcTemplate.queryForObject(sql,int.class);
			return deleteCheck;
		}catch (Exception e) {
			return 0;
		}
	}


}    