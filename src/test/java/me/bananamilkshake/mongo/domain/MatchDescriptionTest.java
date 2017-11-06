package me.bananamilkshake.mongo.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.bananamilkshake.mongo.domain.aggregation.match.MatchDescription;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class MatchDescriptionTest {

	@Test
	public void shouldBeSerializedToJson() throws JsonProcessingException {
		// given
		final String user = "RO";
		final LocalDateTime date = LocalDateTime.of(2017, 10, 7, 0, 0);
		final ObjectMapper objectMapper = new ObjectMapper();

		// when
		final String serialized = objectMapper.writeValueAsString(new MatchDescription(user, date));

		// then
		final String expected = "{ \"user\" : \"RO\", \"validFrom\" : { \"$lte\" : ISODate(\"2017-10-07\" ) }, \"validTo\" : { \"$gte\" : ISODate(\"2017-10-07\" ) } }";
		assertThat(serialized).isEqualToIgnoringWhitespace(expected);
	}
}