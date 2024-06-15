package com.hnv99.framework.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hnv99.common.exception.DataNotFoundException;
import com.hnv99.system.domain.SysBill;
import com.hnv99.system.domain.SysSession;
import com.hnv99.system.service.impl.SysBillServiceImpl;
import com.hnv99.system.service.impl.SysSessionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for canceling timeout bills and releasing seat resources.
 */
@Component
public class CancelTimeoutBillUtil {

    @Autowired
    private SysBillServiceImpl sysBillService;

    @Autowired
    private SysSessionServiceImpl sysSessionService;

    /**
     * Cancel all timed-out unpaid orders and release seat resources.
     */
    @Bean(name = "cancelTimeoutBill")
    public void canelTimeoutBill() {
        // Retrieve all unpaid orders that have timed out
        List<SysBill> timeoutBillList = sysBillService.findTimeoutBill();
        // Iterate through the timeout orders using lambda expressions and process them
        timeoutBillList.forEach((timeoutBill) -> {
            // Update the order cancellation status
            timeoutBill.setCancelState(true);
            // Update the order information
            sysBillService.updateBill(timeoutBill);

            SysSession curSession = timeoutBill.getSysSession();
            if (curSession == null) {
                throw new DataNotFoundException("Session does not exist");
            }

            // Retrieve the current timeout order seat information
            String[] selectSeats = timeoutBill.getSeats().split(",");
            // Number of seats canceled
            int cancelSallNums = selectSeats.length;
            curSession.setSallNums(curSession.getSallNums() - cancelSallNums);

            // Map to store selected seats of the timeout order
            Map<String, Integer> selectedSeatsMap = new LinkedHashMap<>();
            for (int i = 0; i < selectSeats.length; i++) {
                String row = selectSeats[i].substring(selectSeats[i].indexOf("\"") + 1, selectSeats[i].indexOf("排"));
                Integer col = Integer.parseInt(selectSeats[i].substring(selectSeats[i].indexOf("排") + 1, selectSeats[i].indexOf("座")));
                selectedSeatsMap.put(row, col);
            }

            // Display the coordinates of the selected seats
            selectedSeatsMap.forEach((key, value) -> {
                System.out.println("key = " + key + " value=" + value);
            });

            // Cancel the seat reservation for the session
            String newSessionSeats = cancelTimeoutBillSessionSeats(curSession.getSessionSeats(), selectedSeatsMap);
            curSession.setSessionSeats(newSessionSeats);
            sysSessionService.updateSession(curSession);
        });
    }


    /**
     * Cancel seat reservation for the session.
     *
     * @param curSessionSeats   The current session seats information.
     * @param selectedSeatsMap The map containing selected seat information.
     * @return The updated session seats information.
     */
    public static String cancelTimeoutBillSessionSeats(String curSessionSeats, Map<String, Integer> selectedSeatsMap) {
        JSONObject curSessionSeatsJSON = JSONObject.parseObject(curSessionSeats);
        Map<String, Object> valueMap = new LinkedHashMap<>();
        valueMap.putAll(curSessionSeatsJSON);
        // Cancel the selected seats
        selectedSeatsMap.forEach((index, value) -> {
            ((JSONArray) valueMap.get(index)).set(value - 1, 0);
        });
        JSONObject newSessionSeatsJSON = new JSONObject(valueMap);
        return JSONObject.toJSONString(newSessionSeatsJSON);
    }
}
