package net.caidingke.common.algorithm;

import junit.framework.TestCase;
import net.caidingke.common.algorithm.BinarySearch;
import org.junit.Test;

/**
 * 二分法
 *
 * @author bowen
 * @create 2016-11-14 17:19
 */

public class BinarySearchTest extends TestCase{

    @Test
    public void testBinarSearch() {
        int [] arr = {2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21};
        System.out.println(BinarySearch.binarySearch(arr, 20l));
    }
}
