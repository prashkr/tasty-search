package com.kredx.tastysearch.index;

import java.util.List;

/**
 * Created by prashkr on 7/6/16.
 */
public interface Index {
    void buildIndex();
    List<Integer> searchIndex(String query);
}
