package net.caidingke.common.algorithm;

import junit.framework.TestCase;
import net.caidingke.common.algorithm.BinaryTree;
import org.junit.Test;

/**
 * 二叉树 先 中 后序遍历
 *
 * @author bowen
 * @create 2016-11-14 17:18
 */

public class BinaryTreeTest extends TestCase {

    @Test
    public void testBinaryTree() {

        BinaryTree binaryTree = new BinaryTree();
        //TreeNode<String> node = binaryTree.init();
        BinaryTree.TreeNode node = binaryTree.init();
        System.out.println("先序遍历的情况");
        binaryTree.xianIterator(node);
        System.out.println("\n中序遍历的情况");
        binaryTree.zhongIterator(node);
        System.out.println("\n后序遍历的情况");
        binaryTree.houIterator(node);
    }
}
