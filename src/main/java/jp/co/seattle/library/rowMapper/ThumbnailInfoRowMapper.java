package jp.co.seattle.library.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import jp.co.seattle.library.dto.ThumbnailInfo;

@Configuration
public class ThumbnailInfoRowMapper implements RowMapper<ThumbnailInfo> {

	@Override
	public ThumbnailInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		// Query結果（ResultSet rs）を、オブジェクトに格納する実装
		ThumbnailInfo bookInfo = new ThumbnailInfo();
		bookInfo.setThumbnailName(rs.getString("thumbnail_name"));
		bookInfo.setThumbnailUrl(rs.getString("thumbnail_url"));
		return bookInfo;
	}

}