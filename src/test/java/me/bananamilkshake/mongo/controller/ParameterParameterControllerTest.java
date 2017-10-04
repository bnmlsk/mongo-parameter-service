package me.bananamilkshake.mongo.controller;

import lombok.extern.slf4j.Slf4j;
import me.bananamilkshake.mongo.assembler.ParameterResponseAssembler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class ParameterParameterControllerTest {

	private ParameterController parameterController;

	@Mock
	private ParameterCreationDescriptionParser parameterCreationDescriptionParser;

	@Mock
	private ParameterResponseAssembler parameterResponseAssembler;

	@Before
	public void before() {
		parameterController = new ParameterController(parameterCreationDescriptionParser, parameterResponseAssembler);
	}

	@Test
	public void shouldReturnParametersForExistingType() {

		// given
		final String parameterName = "Flowers";
		final String parameters = "{ name: \"rose\", colour: \"yellow\" }";
		final String user = "BestFlowersInc";
		final LocalDate parameterDate = LocalDate.of(2017, Month.SEPTEMBER, 24);

		// when
		when(parameterResponseAssembler.getParameters(eq(parameterName), user, parameterDate)).thenReturn(ResponseEntity.ok(parameters));

		// then
		final ResponseEntity result = parameterController.getParameters(parameterName, user, parameterDate);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(parameters);
	}

	@Test
	public void shouldReturnUriToCreatedParameter() {

		// given
		final String newParameterName = "newParameter";
		final String pathToParameter = "/parameter/" + newParameterName;

		// when
		when(parameterResponseAssembler.createParameter(eq(newParameterName), eq(null), eq(null))).thenReturn(ResponseEntity.created(uri(pathToParameter)).build());

		// then
		final ResponseEntity result = parameterController.createParameter(newParameterName, null);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(result.getHeaders().getLocation().getPath()).isEqualTo(pathToParameter);
	}

	@Test
	public void shouldReturnAcceptedHttpStatusCodeOnSuccessfullParameterUpload() {

		// given
		final String parameterName = "Flowers";
		final String parameters = "{ name: \"rose\", colour: \"yellow\" }";

		// when
		when(parameterResponseAssembler.uploadParameters(eq(parameterName), eq(parameters))).thenReturn(ResponseEntity.accepted().build());

		// then
		final ResponseEntity result = parameterController.uploadParameters(parameterName, parameters);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
		assertThat(result.getBody()).isNull();
	}

	private URI uri(String path) {
		try {
			return new URI(path);
		} catch (URISyntaxException exception) {
			throw new RuntimeException(exception);
		}
	}
}