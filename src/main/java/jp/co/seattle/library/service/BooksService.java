package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.dto.HistoryInfo;
import jp.co.seattle.library.rowMapper.BookDetailsInfoRowMapper;
import jp.co.seattle.library.rowMapper.BookInfoRowMapper;
import jp.co.seattle.library.rowMapper.HistoryRowMapper;

/**
 * 書籍サービス
 * 
 *  booksテーブルに関する処理を実装する
 */
@Service
public class BooksService {
	final static Logger logger = LoggerFactory.getLogger(BooksService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 書籍リストを取得する
	 *
	 * @return 書籍リスト
	 */
	public List<BookInfo> getBookList() {

		// TODO 取得したい情報を取得するようにSQLを修正
		List<BookInfo> getedBookList = jdbcTemplate.query(
				"SELECT id,title,author,publisher,publish_date,isbn,explanation,thumbnail_url FROM books order by title asc",
				new BookInfoRowMapper());

		return getedBookList;
	}

	/**
	 * 検索に部分一致した書籍のリストを取得する
	 * 
	 * @param title 書籍名
	 * @return 書籍リスト
	 */
	public List<BookInfo> searchBook(String title) {

		// TODO 取得したい情報を取得するようにSQLを修正
		List<BookInfo> getedBookList = jdbcTemplate.query(
				"SELECT id,title,author,publisher,publish_date,isbn,explanation,thumbnail_url FROM books where title like '%"+ title +"%' order by title asc",
				new BookInfoRowMapper());

		return getedBookList;
	}
	
	/**
	 * 貸出履歴リストを取得
	 * 
	 * @return 貸出リスト
	 */
	
	public List<HistoryInfo> HistoryList() {

		// TODO 取得したい情報を取得するようにSQLを修正
		List<HistoryInfo> getedHistoryList = jdbcTemplate.query(
				"SELECT lentbooks.bookid,books.id,books.title,lentbooks.lent_date,lentbooks.return_date FROM books left outer join lentbooks on books.id = lentbooks.bookid where lentbooks.lentid is not null or lentbooks.return_date is not null",	
			new HistoryRowMapper());

		return getedHistoryList;
	}
	
	/**
	 * 書籍IDに紐づく書籍詳細情報を取得する
	 *
	 * @param bookId 書籍ID
	 * @return 書籍情報
	 */
	public BookDetailsInfo getBookInfo(int bookId) {

		// JSPに渡すデータを設定する

		String sql = "SELECT *,CASE WHEN lent_date IS NULL THEN '貸し出し可' ELSE '貸し出し中' END AS status FROM books left outer join lentbooks on books.id = lentbooks.bookid where books.id ="+ bookId;
		BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());
		return bookDetailsInfo;
	}

	/**
	 * 書籍を登録する
	 *
	 * @param bookInfo 書籍情報
	 */
	public void registBook(BookDetailsInfo bookInfo) {

		String sql = "INSERT INTO books (title, author,publisher,publish_date,isbn,explanation,thumbnail_name,thumbnail_url,reg_date,upd_date) VALUES ('"
				+ bookInfo.getTitle() + "','" + bookInfo.getAuthor() + "','" + bookInfo.getPublisher() + "','" 
				+ bookInfo.getPublishDate() + "','"
				+ bookInfo.getIsbn() + "','"
				+ bookInfo.getExplanation() + "','"
				+ bookInfo.getThumbnailName() + "','"
				+ bookInfo.getThumbnailUrl() + "',"
				+ "now(),"
				+ "now())";

		jdbcTemplate.update(sql);
	}
	/**
	 * 
	 * @param bookId
	 */
	public void deleteBook(int bookId) {

		String sql = "DELETE FROM books WHERE id = " + bookId ;

		jdbcTemplate.update(sql);
	}
	
	public void deleteHistory(int bookId) {
		
		String  sql = "DELETE FROM lentbooks WHERE bookid = " + bookId ;	
		
		jdbcTemplate.update(sql);
	}

	/**
	 * 書籍IDに紐づく書籍詳細情報を取得する
	 *
	 * @param maxId 最新の書籍ID
	 * @return 最新の書籍情報
	 */

	public int maxId() {
		String sql = "SELECT Max(id) FROM books";
		int maxId = jdbcTemplate.queryForObject(sql,int.class); 
		return maxId;
	}

	/**
	 * 書籍IDに紐づく書籍情報編集
	 *
	 * @param bookInfo.getBookId();選択された書籍ID
	 * @return 選択された書籍情報
	 */

	public void editBook(BookDetailsInfo bookInfo) {

		String sql = "UPDATE books SET title ='"+ bookInfo.getTitle()
		+ "',author ='"+ bookInfo.getAuthor()
		+"',publisher ='"+ bookInfo.getPublisher()
		+"',thumbnail_name ='"+ bookInfo.getThumbnailName()
		+"',publish_date ='"+ bookInfo.getPublishDate()																		 
		+"',isbn ='"+ bookInfo.getIsbn() 
		+"',explanation ='"+ bookInfo.getExplanation() 
		+"',thumbnail_url ='"+ bookInfo.getThumbnailUrl() 
		+"',upd_date = now() WHERE Id ="+ bookInfo.getBookId();
		jdbcTemplate.update(sql);

	}

}
