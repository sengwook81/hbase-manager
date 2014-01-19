package org.zero.apps.hbase.manager.support;

import org.apache.hadoop.hbase.util.Bytes.ByteArrayComparator;

public class GridColumnBytesComparator extends ByteArrayComparator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.hadoop.hbase.util.Bytes.ByteArrayComparator#compare(byte[],
	 * byte[])
	 */
	@Override
	public int compare(byte[] left, byte[] right) {
/*
		String strLeft = new String(left);
		String strRight = new String(right);
		// TODO Auto-generated method stub
		if (strLeft.equals(strRight)) {
			return 0;
		} else {
			return 1;
		}
		*/
		if (super.compare(left, right) == 0) {
			return 0;
		} else {
			return 1;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.hadoop.hbase.util.Bytes.ByteArrayComparator#compare(byte[],
	 * int, int, byte[], int, int)
	 */
	@Override
	public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
		// TODO Auto-generated method stub
		if (super.compare(b1, s1, l1, b2, s2, l2) == 0) {
			return 0;
		} else {
			return 1;
		}
	}
}
