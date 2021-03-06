package com.akujasa.jasacenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Antonius on 13/05/2017.
 */

public class RegisterProcess extends AsyncTask {
    private String status;
    private Context context;
    private String id;
    public RegisterProcess(Context context){
        this.context = context;

    }

    @Override
    protected Object doInBackground(Object[] objects) {

        try{
            String nama = (String)objects[0];
            String alamat = (String)objects[2];
            String email = (String)objects[1];
            String nohp = (String)objects[3];
            String password = (String)objects[4];
            String ulangi_password = (String)objects[5];
            //emails = email;
            String link = "http://rilokukuh.com/admin-jasa/android_ksm_register.php";
            String data = URLEncoder.encode("ksm_nama","UTF-8")+"="+URLEncoder.encode(nama,"UTF-8");
            data += "&"+URLEncoder.encode("ksm_alamat","UTF-8")+"="+URLEncoder.encode(alamat,"UTF-8");
            data += "&"+URLEncoder.encode("ksm_email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8");
            data += "&"+URLEncoder.encode("ksm_nohp","UTF-8")+"="+URLEncoder.encode(nohp,"UTF-8");
            data += "&"+URLEncoder.encode("ksm_password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
            data += "&"+URLEncoder.encode("ksm_ulangi_password","UTF-8")+"="+URLEncoder.encode(ulangi_password,"UTF-8");
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line=reader.readLine())!=null){
                sb.append(line);
                break;
            }
            JSONObject jsonOBJ = new JSONObject(sb.toString());
            String status = jsonOBJ.getString("status");
            String message = jsonOBJ.getString("message");
            id = jsonOBJ.getString("id");
            return message;
        }
        catch (Exception e){
            return new String("Exception: "+e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        this.status = (String)o;

        if(status.equals("success")) {
            Toast.makeText(context,"Registrasi berhasil!",Toast.LENGTH_LONG).show();
            SharedPreferences sp = context.getSharedPreferences("pencari_info",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("pencari_id",id);
            editor.commit();
            Intent intentku = new Intent(context, MenuUtamaActivity.class);
            intentku.putExtra("pencari_id",id);
            context.startActivity(intentku);
            ((Activity)context).finish();
        }
        else if(status.equals("email sudah digunakan")){
            Toast.makeText(context,"Email sudah pernah digunakan!",Toast.LENGTH_SHORT).show();
        }
        else if(status.equals("password tidak cocok")){
            Toast.makeText(context,"Ulangi password salah!",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context,"Koneksi terganggu/tidak ada!",Toast.LENGTH_LONG).show();
        }
    }
}

