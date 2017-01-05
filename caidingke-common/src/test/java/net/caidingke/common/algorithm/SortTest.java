package net.caidingke.common.algorithm;

import junit.framework.TestCase;
import net.caidingke.common.algorithm.Sort;
import org.junit.Test;

/**
 * 测试排序算法
 *
 * @author bowen
 * @create 2016-11-14 15:06
 */
public class SortTest extends TestCase{

    private int[] arr = {2, 4, 8, 6, 9};

    @Test
    public void testBubbleSort() {
        Sort.bubbleSort(arr);
        for (int i : arr) {
            System.out.println(i);
        }
    }
    @Test
    public void testInsertionSort() {
        Sort.insertionSort(arr);
        for (int i : arr) {
            System.out.println(i);
        }
    }

    @Test
    public void testMergeSortRecursive() {
        Sort.mergeSort(arr);
        for (int i : arr) {
            System.out.println(i);
        }
    }

    @Test
    public void testMergeSortIteration() {
        Sort.mergeSortIteration(arr);
        for (int i : arr) {
            System.out.println(i);
        }
    }
}
