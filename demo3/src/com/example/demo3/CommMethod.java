package com.example.demo3;

import android.content.Context;
import android.util.Log;
import android.os.Handler;
import android.os.Message;
import java.io.DataOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class CommMethod {
	static String TAG = "CubicManager";
	String PHONEFACTORY_CLASS_NAME = "com.android.internal.telephony.PhoneFactory";
	String PHONE_CLASS_NAME = "com.android.internal.telephony.Phone";
	String CUBIC_MANAGER_CLASS_NAME = "android.app.CubicManager";
	protected static final int EVENT_SET_IMEI_DONE = 0;
	protected static final int EVENT_SET_BTADDR_DONE = 1;

	protected static final int ADC_TYPE_VADC = 0;
	protected static final int ADC_TYPE_MPP1 = 1;
	protected static final int ADC_TYPE_MPP2 = 2;
	protected static final int ADC_TYPE_MPP3 = 3;
	protected static final int ADC_TYPE_MPP4 = 4;

	private Context mContext = null;
	private LoadMethodEx methodEx = null;
	private Object mObjPhone = null;
	private Object mObjCubicManager = null;
	Process process = null;
	DataOutputStream os = null;
	
    public CommMethod(Context context)
    {
            mContext = context;
            mObjCubicManager = (Object)mContext.getSystemService("cubicmanager");
        	methodEx = new LoadMethodEx();
        	mObjPhone = methodEx.loadReflectMethod(PHONEFACTORY_CLASS_NAME,null,"getDefaultPhone",new Object[0]); 
    }
    
    private class LoadMethodEx {
        public Object loadReflectMethod(String cName,Object obj,  String MethodName, Object[] params) {
            Object retObject = null;
            Object obj_tmp = obj;
            try {
                Class<?> cls = Class.forName(cName);
                if(obj == null)
                {
    	            Constructor<?> ct = cls.getConstructor(new Class<?>[0]);
    	            obj_tmp = ct.newInstance(new Object[0]);    
    	        }
                Class<?> paramTypes[] = this.getParamTypes(cls, MethodName);
                
                Method meth = cls.getMethod(MethodName, paramTypes);
                meth.setAccessible(true);
                
                retObject = meth.invoke(obj_tmp, params);  
            } catch (Exception e) {
                System.err.println(e);
            }
            
            return retObject;
        }
        
        private Class<?>[] getParamTypes(Class<?> cls, String mName) {
            Class<?>[] cs = null;
            
            Method[] mtd = cls.getDeclaredMethods();    
            for (int i = 0; i < mtd.length; i++) {
                if (!mtd[i].getName().equals(mName)) {
                    continue;
                }
                
                cs = mtd[i].getParameterTypes();
            }
            return cs;
        }
    }

    static private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EVENT_SET_IMEI_DONE:
                	Log.d(TAG,"set imei sccessfully!");
                    break;
                case EVENT_SET_BTADDR_DONE:
                	Log.d(TAG,"set bt addr sccessfully!");
                	break;  	
                default:
                    break;
            }
        }
    };

	//[wangliwei]2015-09-08
	public void setOfficialName(String officialName) 
    {
    	methodEx.loadReflectMethod(CUBIC_MANAGER_CLASS_NAME,mObjCubicManager, "setOfficialName",new Object[]{new String(officialName)});
    }
	
    public void setModelNumber(String modelNumber) 
    {
    	methodEx.loadReflectMethod(CUBIC_MANAGER_CLASS_NAME,mObjCubicManager, "setModelNumber",new Object[]{new String(modelNumber)});
    }
    
    public void setSerialNumber(String serialNumber) 
    {
    	methodEx.loadReflectMethod(CUBIC_MANAGER_CLASS_NAME,mObjCubicManager, "setSerialNumber",new Object[]{new String(serialNumber)});
    }
    
    public void setWifiMacAddr(String wifiMacAddr) 
    {
    	methodEx.loadReflectMethod(CUBIC_MANAGER_CLASS_NAME,mObjCubicManager, "setWifiMacAddr",new Object[]{new String(wifiMacAddr)});
    }
    
    public void setIMEI(String imei) 
    {
    	methodEx.loadReflectMethod(PHONE_CLASS_NAME,mObjPhone, "setIMEI",new Object[]{new String(imei),mHandler.obtainMessage(EVENT_SET_IMEI_DONE)});
    }
    
    public void setBtAddr(String btAddr) 
    {
    	methodEx.loadReflectMethod(PHONE_CLASS_NAME,mObjPhone, "setBtAddr",new Object[]{new String(btAddr),mHandler.obtainMessage(EVENT_SET_BTADDR_DONE)});
    }
    
	public int set_gpio_export(int gpioPort) {
		if (gpioPort < 940 && gpioPort > 930) {
			try {
				methodEx.loadReflectMethod(CUBIC_MANAGER_CLASS_NAME,
						mObjCubicManager, "set_gpio_export",
						new Object[] { gpioPort });
			} catch (Exception e) {
				Log.e("error",
						"Unexpected error - Here is what I know:"
								+ e.getMessage());
			}
		} else {
			return -1;
		}
		return 0;
	}

	public int set_gpio_unexport(int gpioPort) {
		if (gpioPort <= 938 && gpioPort >= 936) {
			try {
				methodEx.loadReflectMethod(CUBIC_MANAGER_CLASS_NAME,
						mObjCubicManager, "set_gpio_unexport",
						new Object[] { gpioPort });
			} catch (Exception e) {
				Log.e("error",
						"Unexpected error - Here is what I know:"
								+ e.getMessage());
			}
		} else {
			return -1;
		}
		return 0;
	}

	public int set_gpio_direction(String direction, int gpioPort) {
		if ((direction.equals("out") || direction.equals("in"))
				&& (gpioPort <= 938 && gpioPort >= 936)) {
			methodEx.loadReflectMethod(CUBIC_MANAGER_CLASS_NAME,
					mObjCubicManager, "set_gpio_direction",
					new Object[] { direction });
		} else {
			return -1;
		}
		return 0;
	}

	public String get_gpio_direction(int gpioPort) {
		Object direction;
		if (gpioPort <= 938 && gpioPort >= 936) {
			direction = methodEx.loadReflectMethod(CUBIC_MANAGER_CLASS_NAME,
					mObjCubicManager, "get_gpio_direction", null);
		} else {
			return "";
		}
		return direction.toString();
	}

	public int set_gpio_val(int value, int gpioPort) {
		if ((value == 0 || value == 1) && (gpioPort <= 938 && gpioPort >= 936)) {
			methodEx.loadReflectMethod(CUBIC_MANAGER_CLASS_NAME,
					mObjCubicManager, "set_gpio_val", new Object[] { value });
		} else {
			return -1;
		}
		return 0;
	}

	public int get_gpio_val(int gpioPort) {
		if (gpioPort <= 938 && gpioPort >= 936) {
			methodEx.loadReflectMethod(CUBIC_MANAGER_CLASS_NAME,
					mObjCubicManager, "get_gpio_value", null);
		} else {
			return -1;
		}
		return 0;
	}

	public int i2c_set_i2c_address(int i2c_addr) {
		if ((i2c_addr >= 0 || i2c_addr <= 255)) {
			methodEx.loadReflectMethod(CUBIC_MANAGER_CLASS_NAME,
					mObjCubicManager, "i2c_set_slave_addr",
					new Object[] { i2c_addr });
		} else {
			return -1;
		}
		return 0;
	}

	public int i2c_set_reg(int reg_addr, int reg_length) {
		if ((reg_addr >= 0 && reg_length >= 0 && (reg_length + reg_addr) <= 255)) {
			methodEx.loadReflectMethod(CUBIC_MANAGER_CLASS_NAME,
					mObjCubicManager, "i2c_set_reg_addr",
					new Object[] { reg_addr });
			methodEx.loadReflectMethod(CUBIC_MANAGER_CLASS_NAME,
					mObjCubicManager, "i2c_set_reg_length",
					new Object[] { reg_length });
		} else {
			return -1;
		}
		return 0;
	}

	public int i2c_read_array(int i2c_addr, int reg_Addr, int[] i2c_data,
			int data_length) {
		if ((i2c_addr >= 0 || i2c_addr <= 255)) {
			methodEx.loadReflectMethod(CUBIC_MANAGER_CLASS_NAME,
					mObjCubicManager, "i2c_read_array", new Object[] {
							i2c_addr, reg_Addr, i2c_data, data_length });
		} else {
			return -1;
		}
		return 0;
	}

	public int i2c_read_direct(int i2c_addr, int reg_Addr) {
		Object i2c_data;
		if ((i2c_addr >= 0 || i2c_addr <= 255)) {
			i2c_data = methodEx.loadReflectMethod(CUBIC_MANAGER_CLASS_NAME,
					mObjCubicManager, "i2c_read_direct", new Object[] {
							i2c_addr, reg_Addr });
		} else {
			return -1;
		}
		return Integer.parseInt(i2c_data.toString());
	}

	public int i2c_write_array(int i2c_addr, int reg_Addr, int[] i2c_data,
			int data_length) {
		if ((i2c_addr >= 0 || i2c_addr <= 255)) {
			methodEx.loadReflectMethod(CUBIC_MANAGER_CLASS_NAME,
					mObjCubicManager, "i2c_write_array", new Object[] {
							i2c_addr, reg_Addr, i2c_data, data_length });
		} else {
			return -1;
		}
		return 0;
	}

	public int i2c_write_direct(int i2c_addr, int reg_Addr, int data) {
		if ((i2c_addr >= 0 || i2c_addr <= 255)) {
			methodEx.loadReflectMethod(CUBIC_MANAGER_CLASS_NAME,
					mObjCubicManager, "i2c_write_direct", new Object[] {
							i2c_addr, reg_Addr, data });
		} else {
			return -1;
		}
		return 0;
	}

	public int i2c_set_timeout(int timeout) {
		if ((timeout >= 0 && timeout <= 255)) {
			methodEx.loadReflectMethod(CUBIC_MANAGER_CLASS_NAME,
					mObjCubicManager, "i2c_set_timeout",
					new Object[] { timeout });
		} else {
			return -1;
		}
		return 0;
	}

	public int i2c_set_retries(int i2c_retries) {
		if ((i2c_retries >= 0 && i2c_retries <= 255)) {
			methodEx.loadReflectMethod(CUBIC_MANAGER_CLASS_NAME,
					mObjCubicManager, "i2c_set_retries",
					new Object[] { i2c_retries });
		} else {
			return -1;
		}
		return 0;
	}

	public int i2c_send_single_cmd(int i2c_addr, int cmd) {
		if ((i2c_addr >= 0 || i2c_addr <= 255)) {
			methodEx.loadReflectMethod(CUBIC_MANAGER_CLASS_NAME,
					mObjCubicManager, "i2c_single_command", new Object[] {
							i2c_addr, cmd });
		} else {
			return -1;
		}
		return 0;
	}

	public int i2c_send_multi_cmd(int i2c_addr, int[] cmd, int length) {
		if ((i2c_addr >= 0 || i2c_addr <= 255)) {
			methodEx.loadReflectMethod(CUBIC_MANAGER_CLASS_NAME,
					mObjCubicManager, "i2c_multiple_command", new Object[] {
							i2c_addr, cmd, length });
		} else {
			return -1;
		}
		return 0;
	}

	public String adc_get_vadc_vol() {
		Object vadc_vol;
		int i = ADC_TYPE_VADC;
		long lResult = 0;
		int iResult = 0;
		vadc_vol = methodEx.loadReflectMethod(CUBIC_MANAGER_CLASS_NAME,
				mObjCubicManager, "adc_get_voltage", new Object[] { i });
		Log.d(TAG, "cubic_adc adc_get_vadc_vol:" + vadc_vol.toString());
		lResult = Long.parseLong(vadc_vol.toString());
		Log.d(TAG, "cubic_adc adc_get_vadc_vol long = :" + lResult);
		iResult = (int)(lResult & 0xFFFFFFFF);
		Log.d(TAG, "cubic_adc adc_get_vadc_vol new int = :" + iResult);
		return iResult + "";
	}

	public String adc_get_mpp1_vol() {
		Object vmpp1_vol;
		int i = ADC_TYPE_MPP1;
		long lResult = 0;
		int iResult = 0;
		vmpp1_vol = methodEx.loadReflectMethod(CUBIC_MANAGER_CLASS_NAME,
				mObjCubicManager, "adc_get_voltage", new Object[] { i });
		Log.d(TAG, "cubic_adc adc_get_mpp1_vol:" + vmpp1_vol.toString());
		lResult = Long.parseLong(vmpp1_vol.toString());
		Log.d(TAG, "cubic_adc adc_get_vmpp1_vol long = :" + lResult);
		iResult = (int)(lResult & 0xFFFFFFFF);
		Log.d(TAG, "cubic_adc adc_get_vmpp1_vol new int = :" + iResult);
		return iResult + "";
	}

	public String adc_get_mpp2_vol() {
		Object vmpp2_vol;
		int i = ADC_TYPE_MPP2;
		long lResult = 0;
		int iResult = 0;
		vmpp2_vol = methodEx.loadReflectMethod(CUBIC_MANAGER_CLASS_NAME,
				mObjCubicManager, "adc_get_voltage", new Object[] { i });
		Log.d(TAG, "cubic_adc adc_get_mpp2_vol:" + vmpp2_vol.toString());
		lResult = Long.parseLong(vmpp2_vol.toString());
		Log.d(TAG, "cubic_adc adc_get_vmpp2_vol long = :" + lResult);
		iResult = (int)(lResult & 0xFFFFFFFF);
		Log.d(TAG, "cubic_adc adc_get_vmpp2_vol new int = :" + iResult);
		return iResult + "";
	}

	public String adc_get_mpp3_vol() {
		Object vmpp3_vol;
		int i = ADC_TYPE_MPP3;
		long lResult = 0;
		int iResult = 0;
		vmpp3_vol = methodEx.loadReflectMethod(CUBIC_MANAGER_CLASS_NAME,
				mObjCubicManager, "adc_get_voltage", new Object[] { i });
		lResult = Long.parseLong(vmpp3_vol.toString());
		Log.d(TAG, "cubic_adc adc_get_vmpp3_vol long = :" + lResult);
		iResult = (int)(lResult & 0xFFFFFFFF);
		Log.d(TAG, "cubic_adc adc_get_vmpp3_vol new int = :" + iResult);
		return iResult + "";
	}

	public String adc_get_mpp4_vol() {
		Object vmpp4_vol;
		int i = ADC_TYPE_MPP4;
		long lResult = 0;
		int iResult = 0;
		vmpp4_vol = methodEx.loadReflectMethod(CUBIC_MANAGER_CLASS_NAME,
				mObjCubicManager, "adc_get_voltage", new Object[] { i });
		lResult = Long.parseLong(vmpp4_vol.toString());
		Log.d(TAG, "cubic_adc adc_get_vmpp4_vol long = :" + lResult);
		iResult = (int)(lResult & 0xFFFFFFFF);
		Log.d(TAG, "cubic_adc adc_get_vmpp4_vol new int = :" + iResult);
		return iResult + "";
	}

}
