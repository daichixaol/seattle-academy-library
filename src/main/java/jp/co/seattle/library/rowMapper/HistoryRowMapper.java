package jp.co.seattle.library.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import jp.co.seattle.library.dto.HistoryInfo;

@Configuration
public class HistoryRowMapper implements RowMapper<HistoryInfo> {

	public HistoryInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		// Query結果（ResultSet rs）を、オブジェクトに格納する実装
		HistoryInfo historyInfo = new HistoryInfo();

		// bookInfoの項目と、取得した結果(rs)のカラムをマッピングする
		historyInfo.setBookId(rs.getInt("id"));
		historyInfo.setTitle(rs.getString("title"));
		historyInfo.setLentDate(rs.getString("lent_date"));
		historyInfo.setReturnDate(rs.getString("return_date"));
		return historyInfo;
	}

}