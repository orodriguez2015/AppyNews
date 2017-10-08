package com.appynews.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.appynews.model.dto.DatosUsuarioVO;

/**
 * Created by oscar on 22/08/16.
 */
public class TelephoneUtil {


    /**
     * Devuelve la info del dispositivo
     * @param context: Context
     * @return DatosUsuarioVO
     */
    public static DatosUsuarioVO getInfoDispositivo(Context context){

        DatosUsuarioVO salida = new DatosUsuarioVO();
        StringBuffer sb = new StringBuffer();
        TelephonyManager telephoneManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);


        sb.append("Número teléfono: ").append(telephoneManager.getLine1Number());
        sb.append(ConstantesDatos.COMMA);
        sb.append("Device id: ").append(telephoneManager.getDeviceId());
        sb.append(ConstantesDatos.COMMA);
        sb.append("Network operator: ").append(telephoneManager.getNetworkOperator());
        sb.append(ConstantesDatos.COMMA);
        sb.append("Sim Serial Number: ").append(telephoneManager.getSimSerialNumber());
        sb.append(ConstantesDatos.COMMA);
        sb.append("Voice mail number: " ).append(telephoneManager.getVoiceMailNumber());
        sb.append(ConstantesDatos.COMMA);
        sb.append("Device software version: " ).append(telephoneManager.getDeviceSoftwareVersion());
        sb.append(ConstantesDatos.COMMA);
        sb.append("Subscriber id: " ).append(telephoneManager.getSubscriberId());
        sb.append(ConstantesDatos.COMMA);
        sb.append("Sim operator: " ).append(telephoneManager.getSimOperator());
        sb.append(ConstantesDatos.COMMA);
        sb.append("Sim country iso: " ).append(telephoneManager.getSimCountryIso());
        sb.append(ConstantesDatos.COMMA);
        sb.append("getVoidceMailAlphaTag: ").append(telephoneManager.getVoiceMailAlphaTag());


        salida.setNumeroTelefono(telephoneManager.getLine1Number());
        salida.setImei(telephoneManager.getDeviceId());
        salida.setRegionIso(telephoneManager.getSimCountryIso());
        getEmailUsuario(context,salida);

        return salida;
    }


    /**
     * Recupera el correo electrónico asociado a la cuenta de google, si es que existe
     * @param context: Context
     * @param datos: DatosTelefonoVO
     * @return void
     */
    private static void getEmailUsuario(Context context, DatosUsuarioVO datos) {
        AccountManager accountManager = AccountManager.get(context);
        //Account[] accounts = accountManager.getAccounts();
        Account[] accounts = accountManager.getAccountsByType("com.google");

        for(int i=0;accounts!=null && i<accounts.length;i++) {
            LogCat.debug("  CUENTA: " + accounts[i].name + ", TIPO: " + accounts[i].type);

            if(accounts[i].type.equals(ConstantesDatos.EMAIL_GOOGLE)) {
                datos.setEmail(accounts[i].name);
                break;
            }
        }

        /*
        String main_data[] = {
                "data1", "is_primary", "data3", "data2", "data1", "is_primary", "photo_uri", "mimetype"
        };

        Object object = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        if (!TextUtils.isEmpty(((TelephonyManager) (object)).getLine1Number())) {

        }
        object = context.getContentResolver().query(Uri.withAppendedPath(android.provider.ContactsContract.Profile.CONTENT_URI, "data"), main_data, "mimetype=?", new String[]{
                "vnd.android.cursor.item/phone_v2"
        }, "is_primary DESC");

        if (object != null) {
            do {
                if (!((Cursor) (object)).moveToNext()) {
                    break;
                }

                String s1 = ((Cursor) (object)).getString(1);
                String s2 = ((Cursor) (object)).getString(2);

                LogCat.debug(" ====> s1: " + s1 + ", s2: " + s2);

                /*
                if (s.equals("vnd.android.cursor.item/phone_v2")) {
                    String s1 = ((Cursor) (object)).getString(4);
                    boolean flag1;
                    if (((Cursor) (object)).getInt(5) > 0) {
                        flag1 = true;
                    } else {
                        flag1 = false;
                    }

                }
            } while (true);
            ((Cursor) (object)).close();
        }
        return null;
        */
    }

    public static String getPhoneNumber(Activity activity){

        StringBuffer sb = new StringBuffer();
        TelephonyManager telephoneManager = (TelephonyManager)activity.getSystemService(Context.TELEPHONY_SERVICE);

        sb.append("Número teléfono: ").append(telephoneManager.getLine1Number());
        sb.append("Device id: ").append(telephoneManager.getDeviceId());
        sb.append("Network operator: ").append(telephoneManager.getNetworkOperator());
        sb.append("Sim Serial Number: ").append(telephoneManager.getSimSerialNumber());
        sb.append("Voice mail number: " ).append(telephoneManager.getVoiceMailNumber());
        sb.append("Device software version: " ).append(telephoneManager.getDeviceSoftwareVersion());
        sb.append("Subscriber id: " ).append(telephoneManager.getSubscriberId());

        return sb.toString();
    }

}
