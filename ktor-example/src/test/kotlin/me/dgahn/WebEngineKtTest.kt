package me.dgahn

import io.ktor.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpHeaders
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document

private const val PORT = 8081
@ExtendWith(RestDocumentationExtension::class)
class WebEngineKtTest {
    private val embeddedServer = embeddedServer(factory = Netty, port = 8081, module = Application::module)
        .apply { start() }
    lateinit var spec: RequestSpecification

    @BeforeEach
    fun setup(restDocumentation: RestDocumentationContextProvider) {
        this.spec = RequestSpecBuilder()
            .addFilter(
                RestAssuredRestDocumentation.documentationConfiguration(restDocumentation)
                    .operationPreprocessors()
                    .withRequestDefaults(
                        Preprocessors.prettyPrint()
                    )
                    .withResponseDefaults(
                        Preprocessors.prettyPrint()
                    )
            )
            .build()
    }

    @Test
    fun `id가 1인 Person을 조회할 수 있다`() {
        val id = 1L
        given(this.spec)
            .header(HttpHeaders.ACCEPT, "application/json")
            .filter(
                document(
                    "get-person-by-id",
                    pathParameters(
                        parameterWithName("id").description("person id, type long")
                    ),
                    responseFields(
                        fieldWithPath("id").description("person id"),
                        fieldWithPath("name").description("person name"),
                        fieldWithPath("email").description("person email"),
                        fieldWithPath("phones[].number").description("phone number"),
                        fieldWithPath("phones[].type").description("phone type")
                    )
            ))
            .`when`()
            .port(8081)
            .get("/persons/{id}", id)
            .then()
            .assertThat()
            .statusCode(200)
            .header(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8")
    }
}
