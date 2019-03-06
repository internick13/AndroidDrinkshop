package galosoft.com.androiddrinkshop;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.szagurskii.patternedtextwatcher.PatternedTextWatcher;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import dmax.dialog.SpotsDialog;
import galosoft.com.androiddrinkshop.Retrofit.IDrinkShopAPI;
import galosoft.com.androiddrinkshop.Model.CheckUserResponse;
import galosoft.com.androiddrinkshop.Model.User;
import galosoft.com.androiddrinkshop.Utils.Common;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1000;
    private static final int REQUEST_PERMISION = 1001;
    Button btn_continue;
    IDrinkShopAPI mService;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case REQUEST_PERMISION:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission denied ", Toast.LENGTH_SHORT).show();
            }
                break;
            default:
                break;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }, REQUEST_PERMISION);


       // printKeyHash();

        btn_continue = findViewById(R.id.btn_continue);

        mService = Common.getAPI();

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoginPage(LoginType.PHONE);
            }
        });

        //Check session
        if(AccountKit.getCurrentAccessToken() != null) {
            final android.app.AlertDialog alertDialog = new SpotsDialog(MainActivity.this);
            alertDialog.show();
            alertDialog.setMessage("Please espere un momento");
            //auto login
            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(final Account account) {
                    mService.checkUserExists(account.getPhoneNumber().toString()).enqueue(new Callback<CheckUserResponse>() {
                        @Override
                        public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {
                            CheckUserResponse userResponse = response.body();
                            if(userResponse.isExists()) {
                                mService.getUserInformation(account.getPhoneNumber().toString()).enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        alertDialog.dismiss();
                                        Common.currentUser = response.body();
                                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } else {
                                alertDialog.dismiss();
                                showRegisterDialog(account.getPhoneNumber().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<CheckUserResponse> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "No se pudo...", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onError(AccountKitError accountKitError) {
                    Log.d("Error", accountKitError.getErrorType().getMessage());
                }
            });

        }

    }

    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("galosoft.com.androiddrinkshop",
                    PackageManager.GET_SIGNATURES);
            for(Signature signature: info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KEYHASH", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    private void startLoginPage(LoginType loginType) {

        Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder builder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(loginType,
                        AccountKitActivity.ResponseType.TOKEN);

        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,builder.build());
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            AccountKitLoginResult result = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);

            if(result.getError() != null) {
                Toast.makeText(this, "" + result.getError().getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
            } else if(result.wasCancelled()) {
                Toast.makeText(this, "Canceled...", Toast.LENGTH_SHORT).show();
            } else {
                if(result.getAccessToken() != null) {
                    final android.app.AlertDialog alertDialog = new SpotsDialog(MainActivity.this);
                    alertDialog.show();
                    alertDialog.setMessage("Please espere un momento");

                    //get user phone and check if exists
                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                        @Override
                        public void onSuccess(final Account account) {
                            mService.checkUserExists(account.getPhoneNumber().toString()).enqueue(new Callback<CheckUserResponse>() {
                                @Override
                                public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {
                                    CheckUserResponse userResponse = response.body();
                                    if(userResponse.isExists()) {
                                        mService.getUserInformation(account.getPhoneNumber().toString()).enqueue(new Callback<User>() {
                                            @Override
                                            public void onResponse(Call<User> call, Response<User> response) {

                                                alertDialog.dismiss();
                                                Common.currentUser = response.body();
                                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                                finish();
                                            }

                                            @Override
                                            public void onFailure(Call<User> call, Throwable t) {
                                                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    } else {
                                        alertDialog.dismiss();
                                        showRegisterDialog(account.getPhoneNumber().toString());
                                    }
                                }

                                @Override
                                public void onFailure(Call<CheckUserResponse> call, Throwable t) {
                                    Toast.makeText(MainActivity.this, "No se pudo...", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onError(AccountKitError accountKitError) {
                            Log.d("Error", accountKitError.getErrorType().getMessage());
                        }
                    });
                }
            }
        }
    }



    private void showRegisterDialog(final String phone) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("REGISTER");

        LayoutInflater inflater = this.getLayoutInflater();
        View register_layout = inflater.inflate(R.layout.register_layout, null);

        final MaterialEditText edt_name = register_layout.findViewById(R.id.edt_name);
        final MaterialEditText edt_address = register_layout.findViewById(R.id.edt_address);
        final MaterialEditText edt_birthdate = register_layout.findViewById(R.id.edt_birthdate);

        Button btn_register = register_layout.findViewById(R.id.btn_register);
        edt_birthdate.addTextChangedListener(new PatternedTextWatcher("####-##-##"));

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.create().dismiss();

                if(TextUtils.isEmpty(edt_address.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter your address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(edt_birthdate.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter your birthdate", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(edt_name.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }

                final android.app.AlertDialog waitingDialog = new SpotsDialog(MainActivity.this);
                waitingDialog.show();
                waitingDialog.setMessage("Please espere un momento");


                mService.registerNewUser(phone,
                                         edt_name.getText().toString(),
                                         edt_address.getText().toString(),
                                         edt_birthdate.getText().toString()).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        waitingDialog.dismiss();
                        User user = response.body();
                        if(TextUtils.isEmpty(user.getError_msg())) {
                            Toast.makeText(MainActivity.this, "User Register successfully", Toast.LENGTH_SHORT).show();
                            Common.currentUser = response.body();
                            //Start new activity

                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        waitingDialog.dismiss();
                    }
                });
            }


        });
        alertDialog.setView(register_layout);
        alertDialog.show();
    }


}
















