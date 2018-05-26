package com.n26.challenge;

import com.n26.challenge.transaction.TransactionRepository;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.n26.challenge.utils.DateUtils.getCurrentTimestamp;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Application.class })
public class ApiTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TransactionRepository repository;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .build();
    }

    @After
    public void tearDown() {
        this.repository.clearTransactions();
    }

    @Test
    public void addTransactionSuccessfullyTest() throws Exception {
        assertEquals(0, this.repository.getTransactions().size());

        JSONObject request = new JSONObject()
                .put("amount", 1)
                .put("timestamp", getCurrentTimestamp());

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request.toString()))
                .andDo(print())
                .andExpect(status().isCreated());

        assertEquals(1, this.repository.getTransactions().size());
    }

    @Test
    public void addOldTransactionTest() throws Exception {
        assertEquals(0, this.repository.getTransactions().size());

        JSONObject request = new JSONObject()
                .put("amount", 1)
                .put("timestamp", getCurrentTimestamp() - 61_000);

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request.toString()))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertEquals(0, this.repository.getTransactions().size());
    }
}
