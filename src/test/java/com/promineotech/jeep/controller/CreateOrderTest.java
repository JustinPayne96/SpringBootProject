package com.promineotech.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.entity.Order;
import lombok.Getter;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = {
    "classpath:flyway/migrations/V1.0__Jeep_Schema.sql",
    "classpath:flyway/migrations/V1.1__Jeep_Data.sql"},
    config = @SqlConfig(encoding = "utf-8"))

class CreateOrderTest {
  @LocalServerPort
  private int serverPort;
  
  @Autowired
  @Getter
  private TestRestTemplate restTemplate;
  
  /*
   * 
   */
  @Test
  void testCreateOrderReturnsSuccess201() {
    
   String body = createOrderBody();
   String uri = String.format("http://localhost:%d/orders", serverPort);
   HttpHeaders headers = new HttpHeaders();
   headers.setContentType(MediaType.APPLICATION_JSON);
   HttpEntity<String> bodyEntity = new HttpEntity<>(body, headers);
  
   ResponseEntity<Order> response = restTemplate.exchange(uri,
       HttpMethod.POST, bodyEntity, Order.class);

   assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
   assertThat(response.getBody()).isNotNull();

   Order order = response.getBody();
   assertThat(order.getCustomer().getCustomerId()).isEqualTo("ASTERN_TORO");
   assertThat(order.getModel().getModelId()).isEqualTo(JeepModel.CHEROKEE);
   assertThat(order.getModel().getTrimLevel()).isEqualTo("Altitude");
   assertThat(order.getModel().getNumDoors()).isEqualTo(4);
   assertThat(order.getColor().getColorId()).isEqualTo("EXT_REDLINE");
   assertThat(order.getEngine().getEngineId()).isEqualTo("3_6_GAS");
   assertThat(order.getTire().getTireId()).isEqualTo("265_GOODYEAR");
   assertThat(order.getOptions()).hasSize(4);

  }

  public String createOrderBody() {
    // @formatter:off
   return "{\n"
        + "  \"customer\":\"ASTERN_TORO\",\n"
        + "  \"model\":\"CHEROKEE\",\n"
        + "  \"trim\":\"Altitude\",\n"
        + "  \"doors\":\"4\",\n"
        + "  \"color\":\"EXT_REDLINE\",\n"
        + "  \"engine\":\"3_6_GAS\",\n"
        + "  \"tire\":\"265_GOODYEAR\",\n"
        + "  \"options\":[\n"
        + "    \"TOP_MOPAR_POWER\",\n"
        + "    \"DOOR_BESTOP_ELEMENT_MIRROR\",\n"
        + "    \"DOOR_BODY_REAR\",\n"
        + "    \"DOOR_BODY_FRONT\"\n"
        + "  ]\n"
        + "}\n"
        + "";
    // @formatter:on
  }
  

}
