package com.xiu.dubbo.enums;

/**
 * 银行卡种类
 * 
 * @author zhangguoguang
 *
 */
public enum DemoEnum {

	DEBIT_CARD("1", "demo-one"), //
	CREDIT_CARD("2", "demo-two");

	private DemoEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	private String code;
	private String desc;

	public String getCode() {
		return this.code;
	}

	public String getDesc() {
		return this.desc;
	}

	/**
	 * 
	 * judgeValue:判断是否枚举类型. <br/>
	 *
	 * @author zwh date: 2018年4月12日 上午11:32:03 <br/>
	 * @param code
	 * @return
	 * @since JDK 1.7
	 */
	public static DemoEnum judgeValue(String code) {
		DemoEnum[] currencys = DemoEnum.values();
		for (DemoEnum currency : currencys) {
			if (currency.getCode().equals(code)) {
				return currency;
			}
		}
		return null;
	}
}