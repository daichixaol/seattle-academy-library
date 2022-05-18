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
	 *  貸し出しテーブルに書籍を追加
	 *  
	 * @param bookId 書籍ID
	 */

	public void lendBook(int bookId) {

		String sql = "insert into lentbooks (bookid) select " + bookId + " where NOT EXISTS (select bookid from lentbooks where bookid=" + bookId + ")";

		jdbcTemplate.update(sql);
	}

	/**
	 * 貸し出しテーブルの書籍数を数える
	 * 
	 * @return 貸した書籍数
	 */
	
	public int lentBooks() {
		String sql = "select count (bookid) from lentbooks";
		int lentBooks = jdbcTemplate.queryForObject(sql,int.class); 
		return lentBooks;
	}

	/**
	 * 返却した書籍を貸し出しテーブルから削除
	 * 
	 * @param bookId 書籍ID
	 */
	
	public void returnBook(int bookId) {

		String sql = "delete from lentbooks where bookid =" + bookId;

		jdbcTemplate.update(sql);
	}

	/**
	 * 貸し出し中の書籍は削除できないようにする
	 * 
	 * @param bookId 書籍ID
	 * @return 書籍を削除したか確認
	 */
	
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