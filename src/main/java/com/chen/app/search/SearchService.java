package com.chen.app.search;


import com.chen.web.form.RentFilter;
import com.chen.web.util.ServiceMultipleResult;
import com.chen.web.util.ServiceResult;

import java.util.List;

/**
 */
public interface SearchService {
    /**
     * @param houseId
     */
    boolean index(Long houseId);

    /**
     * @param houseId
     */
    void remove(Long houseId);

    ServiceMultipleResult<Long> query(RentFilter rentFilter);

    ServiceResult<List<String>> suggest(String prefix);

    ServiceResult<Long> aggregateDistrictHouse(String cityEnName, String regionEnName, String district);

}
