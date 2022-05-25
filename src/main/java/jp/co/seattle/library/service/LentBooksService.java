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

		String sql = "insert into lentbooks (bookid,lent_date) select " + bookId + ",now() where NOT EXISTS (select bookid from lentbooks where bookid=" + bookId + ")";

		jdbcTemplate.update(sql);
	}

	/**
	 * 貸し出しテーブルの書籍数を数える
	 * 
	 * @return 貸した書籍数
	 */

	public int lentBooks() {
		String sql = "select count (lent_date) from lentbooks";
		int lentBooks = jdbcTemplate.queryForObject(sql,int.class); 
		return lentBooks;
	}

	/**
	 * 貸し出し中の書籍は削除できない条件
	 * 
	 * @param bookId 書籍ID
	 * @return 書籍を削除したか確認
	 */

	public int deleteBookCheck(int bookId) {
		try {
			String sql = "select bookid from lentbooks where bookid =" + bookId +"and lentbooks.lent_date is not null";
			int deleteCheck = jdbcTemplate.queryForObject(sql,int.class);
			return deleteCheck;
		}catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 返却済みの書籍を再度借りた場合に貸出日を表示
	 * 
	 * @param bookId
	 * @return
	 */

	public int updateLent(int bookId) {
		try {
			String sql = "update lentbooks set lent_date = now(),return_date = null where bookid =" + bookId;
			int updateLentdate = jdbcTemplate.queryForObject(sql,int.class);
			return updateLentdate;
		}catch(Exception e) {
			return 0;
		}
	}

	/**
	 * 貸し出し済みの書籍を返した場合に返却日を表示
	 * 
	 * @param bookId
	 * @return
	 */

	public int updateReturn(int bookId) {
		try {
			String sql = "update lentbooks set lent_date = null,return_date = now() where bookid =" + bookId;
			int updateLentdate = jdbcTemplate.queryForObject(sql,int.class);
			return updateLentdate;
		}catch(Exception e) {
			return 0;
		}
	}

	/**
	 * 初めて借りるときの条件
	 * @param bookId
	 * @return
	 */

	public int lentCheck (int bookId) {
		try {
			String sql = "select bookid from lentbooks where bookid =" + bookId;
			int lentFirst = jdbcTemplate.queryForObject(sql,int.class);
			return lentFirst;
		}catch(Exception e) {
			return 0;
		}
	}

	/**
	 *2回目以降借りれる条件
	 * @param bookId
	 * @return
	 */

	public int lentSecondCheck (int bookId) {

			String sql = "SELECT COUNT(lent_date) FROM lentbooks WHERE bookid =" + bookId;	
			int lentSecondCheck = jdbcTemplate.queryForObject(sql,int.class);
			return lentSecondCheck;
		
	}


	/**
	 * 返せる条件
	 * @param bookId
	 * @return
	 */

	public int returnCheck (int bookId) {

			String sql = "SELECT COUNT(return_date) FROM lentbooks WHERE bookid =" + bookId;;
			int returnCheck = jdbcTemplate.queryForObject(sql,int.class);
			return returnCheck;
		}
	

}    