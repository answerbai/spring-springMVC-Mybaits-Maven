package com.remoteDebug;

public class ClassModifier {
	private static final int CONSTANT_POOL_COUNT_INDEX = 8;
	private static final int CONSTANT_Utf8_info = 1;
	private static final int[] CONSTANT_ITEM_LENGTH = { -1, -1, -1, 5, 5, 9, 9, 3, 3, 5, 5, 5, 5 };
	private static final int u1 = 1;
	private static final int u2 = 2;
	private byte[] classByte;

	public ClassModifier(byte[] classByte) {
		this.classByte = classByte;
	}

	public byte[] modifyUTF8Constant(String oldStr, String newStr) {
		int cpc = getConstantPoolCount();
		int offset = CONSTANT_POOL_COUNT_INDEX + u2;
		System.out.println("原始byte："+classByte.toString());
		for(byte b:classByte){
			System.out.println(String.valueOf(b));
			
		}
		System.out.println("+++++++++++++++++++");
		for (int i = 0; i < cpc; i++) {
			int tag = ByteUtils.bytes2Int(classByte, offset, u1);
			if (tag == CONSTANT_Utf8_info) {
				int len = ByteUtils.bytes2Int(classByte, offset + u1, u2);
				offset += (u1 + u2);
				String str = ByteUtils.bytes2String(classByte, offset, len);
				System.out.println("str="+str);
				if (str.equalsIgnoreCase(oldStr)) {
					byte[] strBytes = ByteUtils.string2Bytes(newStr);
					byte[] strLen = ByteUtils.int2Bytes(newStr.length(), u2);
					classByte = ByteUtils.bytesReplace(classByte, offset - u2, u2, strLen);
					classByte = ByteUtils.bytesReplace(classByte, offset, len, strBytes);
					System.out.println("修改后的byte1："+ByteUtils.bytes2String(classByte, 0, classByte.length));
					return classByte;
				} else {
					offset += len;
				}
			} else {
				offset += CONSTANT_ITEM_LENGTH[tag];
			}
		}
		System.out.println("modifyUTF8Constant---->CONSTANT_Utf8_info = "+CONSTANT_Utf8_info);
		System.out.println("修改后的byte2："+classByte.toString());
		return classByte;
	}

	public int getConstantPoolCount() {
		return ByteUtils.bytes2Int(classByte, CONSTANT_POOL_COUNT_INDEX, u2);
	}
}
