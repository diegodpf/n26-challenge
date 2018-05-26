package com.n26.challenge;

import com.n26.challenge.transaction.Transaction;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.n26.challenge.utils.DateUtils.getCurrentTimestamp;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                .andExpect(status().isNoContent());

        assertEquals(0, this.repository.getTransactions().size());
    }

    @Test
    public void getStatisticsSuccessfullyTest() throws Exception {
        this.repository.addTransaction(new Transaction(10.25, getCurrentTimestamp()));
        this.repository.addTransaction(new Transaction(23.57, getCurrentTimestamp()));
        this.repository.addTransaction(new Transaction(36.34, getCurrentTimestamp()));
        this.repository.addTransaction(new Transaction(4.76, getCurrentTimestamp()));
        this.repository.addTransaction(new Transaction(5.77, getCurrentTimestamp()));
        this.repository.addTransaction(new Transaction(6, getCurrentTimestamp() - 61_000));

        MvcResult result = this.mockMvc.perform(get("/statistics")).andExpect(status().isOk()).andReturn();

        JSONObject response = new JSONObject(result.getResponse().getContentAsString());

        assertEquals(80.69, response.getDouble("sum"), 0);
        assertEquals(16.14, response.getDouble("avg"), 0);
        assertEquals(36.34, response.getDouble("max"), 0);
        assertEquals(4.76, response.getDouble("min"), 0);
        assertEquals(5, response.getInt("count"));
    }

    @Test
    public void getStatisticsWithTimeDelaySuccessfullyTest() throws Exception {
        this.repository.addTransaction(new Transaction(10, getCurrentTimestamp()));

        MvcResult result1 = this.mockMvc.perform(get("/statistics")).andExpect(status().isOk()).andReturn();
        JSONObject r1 = new JSONObject(result1.getResponse().getContentAsString());

        assertEquals(10, r1.getDouble("sum"), 0);
        assertEquals(10, r1.getDouble("avg"), 0);
        assertEquals(10, r1.getDouble("max"), 0);
        assertEquals(10, r1.getDouble("min"), 0);
        assertEquals(1, r1.getInt("count"));

        Thread.sleep(30_000);

        this.repository.addTransaction(new Transaction(20, getCurrentTimestamp()));

        MvcResult result2 = this.mockMvc.perform(get("/statistics")).andExpect(status().isOk()).andReturn();
        JSONObject r2 = new JSONObject(result2.getResponse().getContentAsString());

        assertEquals(30, r2.getDouble("sum"), 0);
        assertEquals(15, r2.getDouble("avg"), 0);
        assertEquals(20, r2.getDouble("max"), 0);
        assertEquals(10, r2.getDouble("min"), 0);
        assertEquals(2, r2.getInt("count"));

        Thread.sleep(30_000);
        this.repository.addTransaction(new Transaction(30, getCurrentTimestamp()));

        MvcResult result3 = this.mockMvc.perform(get("/statistics")).andExpect(status().isOk()).andReturn();
        JSONObject r3 = new JSONObject(result3.getResponse().getContentAsString());

        assertEquals(50, r3.getDouble("sum"), 0);
        assertEquals(25, r3.getDouble("avg"), 0);
        assertEquals(30, r3.getDouble("max"), 0);
        assertEquals(20, r3.getDouble("min"), 0);
        assertEquals(2, r3.getInt("count"));

        Thread.sleep(60_000);

        MvcResult result4 = this.mockMvc.perform(get("/statistics")).andExpect(status().isOk()).andReturn();
        JSONObject r4 = new JSONObject(result4.getResponse().getContentAsString());

        assertEquals(0, r4.getDouble("sum"), 0);
        assertEquals(0, r4.getDouble("avg"), 0);
        assertEquals(0, r4.getDouble("max"), 0);
        assertEquals(0, r4.getDouble("min"), 0);
        assertEquals(0, r4.getInt("count"));
    }
}
