package in.mkp.rayzorpaypaymentgateway;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.razorpay.Checkout;
import com.razorpay.ExternalWalletListener;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements PaymentResultWithDataListener, ExternalWalletListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private AlertDialog.Builder alertDialogBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Preload payment resources
         */
        Checkout.preload(getApplicationContext());

        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final AppCompatActivity activity = this;

        final Checkout checkout = new Checkout();

        Checkout.sdkCheckIntegration(activity);

        checkout.setKeyID("<YOUR SECRET KEY WHICH YOU WILL GET FROM RAZORPAY DASHBOARD>");
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Testing");
            options.put("description", "Testing App");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            // to set theme color
            options.put("theme.color", "#5a3789");
            options.put("image", "https://razorpay.com/docs/build/browser/static/razorpay-docs-dark.6f09b030.svg");
            options.put("currency", "INR");
            options.put("amount", "<AMOUNT>");
            options.put("order_id", "<ORDER ID GENERATED FROM API>");//from response of API.
            JSONObject preFill = new JSONObject();
            preFill.put("email", "<USER EMAIL>");
            preFill.put("contact", "<USER MOBILE>");
            options.put("prefill", preFill);


            checkout.open(activity, options);
            Log.w("razorpay", "" + options);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onExternalWalletSelected(String s, PaymentData paymentData) {
        try {
            alertDialogBuilder.setMessage("External Wallet Selected:\nPayment Data: " + paymentData.getData());
            alertDialogBuilder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //after successful
    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        Log.w(TAG, "onPaymentSuccess");
        Log.w("Order_ID", paymentData.getOrderId());
        Log.w("razorpaySignature", "" + paymentData.getSignature());
    }

    //if some errors
    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Log.w("PaymentData", "" + paymentData.getOrderId());
        Log.w("PaymentData", "" + paymentData.getPaymentId());
        Log.w("PaymentData", "" + paymentData.getSignature());
        Log.w("PaymentError", "" + s);
        Log.w("Error_code", "" + i);
    }
}